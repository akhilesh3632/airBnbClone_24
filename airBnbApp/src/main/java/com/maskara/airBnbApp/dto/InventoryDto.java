package com.maskara.airBnbApp.dto;

import com.maskara.airBnbApp.modal.Hotel;
import com.maskara.airBnbApp.modal.Room;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryDto {

    private Long id;
    private LocalDate date;
    private Integer bookedCount;
    private Integer reservedCount;
    private Integer totalCount;
    private BigDecimal surgeFactor;
    private BigDecimal price; //base price * surgeFactor
//    private String city;
    private Boolean closed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
