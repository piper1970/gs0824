package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a completed Rental Agreement for Tool Rental
 * Other than minor string formatting logic, this is a dumb POJO, expecting all calculations of derived fields
 * to be handled prior to instantiation of this class
 * <p>
 * The following fields are given, either as parameters, or values returned from the Tool Repo:
 * <ul>
 *     <li>toolCode</li>
 *     <li>tooType</li>
 *     <li>toolBrand</li>
 *     <li>rentalDays</li>
 *     <li>checkoutDate</li>
 *     <li>dailyRentalCharge</li>
 *     <li>discountPercent</li>
 * </ul>
 *
 * The following fields are calculated, based on the values above:
 * <ul>
 *     <li>dueDate</li>
 *     <li>chargeDays</li>
 *     <li>preDiscountCharge</li>
 *     <li>discountAmount</li>
 *     <li>finalCharge</li>
 * </ul>
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



