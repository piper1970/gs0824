package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a completed Rental Agreement for Tool Rental
 * Other than minor string formatting logic, this is a dumb POJO, expecting all calculations of derived fields
 * to be handled prior to instantiation of this class
 */
@Builder
@Value
public class RentalAgreement {
    String toolCode;
    String toolType;
    String toolBrand;
    int rentalDays;
    LocalDate checkoutDate;
    LocalDate dueDate;
    BigDecimal dailyRentalCharge;
    int chargeDays;
    BigDecimal preDiscountCharge;
    int discountPercent;
    BigDecimal discountAmount;
    BigDecimal finalCharge;
}



