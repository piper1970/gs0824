package sample.pos.renderers;

import sample.pos.domain.RentalAgreement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RentalAgreementTextRenderer implements Renderer<RentalAgreement>{

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uu");

    private static final ThreadLocal<NumberFormat> NUMBER_FORMATTER = ThreadLocal.withInitial(() -> NumberFormat.getCurrencyInstance(Locale.US));

    /**
     * Renders RentalAgreement parameter as a block of text, with Title: Value semantics for each field.
     *
     * <pre>
     *     Tool code: ABCD
     *     Tool brand: Ladder
     *     ...
     *     Final charge: $12.99
     * </pre>
     *
     * @param rentalAgreement data to be rendered
     * @return formatted results
     */
    @Override
    public String render(RentalAgreement rentalAgreement) {
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
                """.formatted(rentalAgreement.getToolCode(),
                rentalAgreement.getToolType(), rentalAgreement.getToolBrand(),
                rentalAgreement.getRentalDays(),
                formatDate(rentalAgreement.getCheckoutDate()),
                formatDate(rentalAgreement.getDueDate()),
                formatBigDecimal(rentalAgreement.getDailyRentalCharge()),
                rentalAgreement.getChargeDays(),
                formatBigDecimal(rentalAgreement.getPreDiscountCharge()),
                rentalAgreement.getDiscountPercent(),
                formatBigDecimal(rentalAgreement.getDiscountAmount()),
                formatBigDecimal(rentalAgreement.getFinalCharge()));
    }

    private String formatBigDecimal(BigDecimal bd){
        // 12.125 -> $12.13
        return NUMBER_FORMATTER.get().format(bd.setScale(2, RoundingMode.HALF_UP));
    }

    private String formatDate(LocalDate date){
        // 2024-08-05 -> 08/05/24
        return date.format(DT_FORMATTER);
    }
}
