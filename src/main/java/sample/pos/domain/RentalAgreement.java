package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a completed Rental Agreement for Tool Rental
 * Other than minor string formatting logic, this is a dumb POJO, expecting all calculations of derived fields
 * to be handled prior to instantiation of this class
 */
@Builder
@Value
public class RentalAgreement {

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("M/d/uu");

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

    @Override
    public String toString() {
        return """
                Tool code: %s
                Tool type: %s
                Tool brand: %s
                Rental days: %d
                Checkout date: %s
                Due date: %s
                Daily rental charge: %s
                Chargeable days: %d
                Pre-discount charge: %s
                Discount percent: %d%%
                Discount amount: %s
                Final Charge: %s
                """.formatted(toolCode, toolType, toolBrand, rentalDays,
                formatDate(checkoutDate), formatDate(dueDate), formatBigDecimal(dailyRentalCharge),
                chargeDays, formatBigDecimal(preDiscountCharge), discountPercent,
                formatBigDecimal(discountAmount),
                formatBigDecimal(finalCharge));
    }

    private String formatBigDecimal(BigDecimal bd){
        // 12.125 -> $12.13
        // NumberFormat not thread safe, so creating currency instance per call.
        return NumberFormat.getCurrencyInstance(Locale.US).format(bd.setScale(2, RoundingMode.HALF_UP));
    }

    private String formatDate(LocalDate date){
        // 2024-08-05 -> 08/05/24
        return date.format(DT_FORMATTER);
    }
}



