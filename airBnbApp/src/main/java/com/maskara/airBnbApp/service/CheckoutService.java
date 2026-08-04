package com.maskara.airBnbApp.service;

import com.maskara.airBnbApp.modal.Booking;
import org.springframework.stereotype.Service;


public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
