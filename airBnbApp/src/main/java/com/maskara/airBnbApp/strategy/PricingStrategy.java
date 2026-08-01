package com.maskara.airBnbApp.strategy;

import com.maskara.airBnbApp.modal.Inventory;


import java.math.BigDecimal;

public interface PricingStrategy {



    BigDecimal calculatePrice(Inventory inventory);
}
