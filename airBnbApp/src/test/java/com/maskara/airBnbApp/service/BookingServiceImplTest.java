package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.dto.BookingDto;
import com.maskara.airBnbApp.exception.UnAuthorisedException;
import com.maskara.airBnbApp.modal.Booking;
import com.maskara.airBnbApp.modal.User;
import com.maskara.airBnbApp.modal.enums.BookingStatus;
import com.maskara.airBnbApp.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookingServiceImpl.initiatePayments() and addGuests().
 *
 * IMPORTANT: CheckoutService is fully mocked here, so these tests
 * never call the real Stripe API and do NOT require a valid Stripe key.
 * This verifies your own business logic (ownership check, status
 * transitions, expiry check) in isolation.
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock private GuestRepository guestRepository;
    @Mock private ModelMapper modelMapper;
    @Mock private BookingRepository bookingRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private CheckoutService checkoutService; // mocked -> no real Stripe call

    @InjectMocks
    private BookingServiceImpl bookingService;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;

    private User owner;
    private User otherUser;

    @BeforeEach
    void setUp() {
        // Inject the @Value field manually since we're not loading Spring context
        try {
            var field = BookingServiceImpl.class.getDeclaredField("frontendUrl");
            field.setAccessible(true);
            field.set(bookingService, "http://localhost:8080");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        owner = new User();
        owner.setId(4L);
        owner.setEmail("akhilesh@gmail.com");

        otherUser = new User();
        otherUser.setId(99L);
        otherUser.setEmail("someoneelse@gmail.com");
    }

    @AfterEach
    void tearDown() {
        if (securityContextHolderMock != null) {
            securityContextHolderMock.close();
        }
    }

    /** Stubs SecurityContextHolder so getCurrentUser() returns the given user. */
    private void mockCurrentUser(User user) {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    private Booking sampleBooking(User bookingOwner, BookingStatus status, LocalDateTime createdAt) {
        Booking booking = new Booking();
        booking.setId(14L);
        booking.setUser(bookingOwner);
        booking.setBookingStatus(status);
        booking.setAmount(BigDecimal.TEN);
        booking.setCreatedAt(createdAt);
        return booking;
    }

    // ---------------------------------------------------------------
    // initiatePayments() tests
    // ---------------------------------------------------------------

    @Test
    void initiatePayments_succeeds_whenOwnerMatchesAndNotExpired() {
        Booking booking = sampleBooking(owner, BookingStatus.RESERVED, LocalDateTime.now());

        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        when(checkoutService.getCheckoutSession(eq(booking), anyString(), anyString()))
                .thenReturn("https://checkout.stripe.com/fake-session-url");

        mockCurrentUser(owner);

        String sessionUrl = bookingService.initiatePayments(14L);

        assertEquals("https://checkout.stripe.com/fake-session-url", sessionUrl);
        assertEquals(BookingStatus.PAYMENTS_PENDING, booking.getBookingStatus());
        verify(bookingRepository).save(booking);
        verify(checkoutService).getCheckoutSession(eq(booking), anyString(), anyString());
    }

    @Test
    void initiatePayments_throwsUnauthorised_whenBookingBelongsToDifferentUser() {
        Booking booking = sampleBooking(owner, BookingStatus.RESERVED, LocalDateTime.now());

        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        mockCurrentUser(otherUser);

        UnAuthorisedException ex = assertThrows(UnAuthorisedException.class,
                () -> bookingService.initiatePayments(14L));

        assertTrue(ex.getMessage().contains("does not belong to this user"));
        verifyNoInteractions(checkoutService); // Stripe should never be called
    }

    @Test
    void initiatePayments_throwsIllegalState_whenBookingExpired() {
        Booking booking = sampleBooking(owner, BookingStatus.RESERVED,
                LocalDateTime.now().minusMinutes(15)); // created > 10 min ago

        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        mockCurrentUser(owner);

        assertThrows(IllegalStateException.class, () -> bookingService.initiatePayments(14L));
        verifyNoInteractions(checkoutService);
    }

    // ---------------------------------------------------------------
    // addGuests() ownership check (regression test for the proxy-equals bug)
    // ---------------------------------------------------------------

    @Test
    void addGuests_succeeds_whenSameUserIdOwnsBooking_evenIfDifferentUserInstance() {
        // Simulate a different User *object* (e.g. Hibernate proxy) with the same id
        User bookingOwnerInstance = new User();
        bookingOwnerInstance.setId(4L);
        bookingOwnerInstance.setEmail("akhilesh@gmail.com");

        Booking booking = sampleBooking(bookingOwnerInstance, BookingStatus.RESERVED, LocalDateTime.now());
        booking.setGuests(new java.util.HashSet<>());

        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(modelMapper.map(any(), eq(BookingDto.class))).thenReturn(new BookingDto());

        mockCurrentUser(owner); // same id (4L), different object instance

        assertDoesNotThrow(() -> bookingService.addGuests(14L, java.util.List.of()));
        assertEquals(BookingStatus.GUESTS_ADDED, booking.getBookingStatus());
    }

    @Test
    void addGuests_throwsUnauthorised_whenDifferentUserId() {
        Booking booking = sampleBooking(owner, BookingStatus.RESERVED, LocalDateTime.now());

        when(bookingRepository.findById(14L)).thenReturn(Optional.of(booking));
        mockCurrentUser(otherUser);

        assertThrows(UnAuthorisedException.class,
                () -> bookingService.addGuests(14L, java.util.List.of()));
    }
}