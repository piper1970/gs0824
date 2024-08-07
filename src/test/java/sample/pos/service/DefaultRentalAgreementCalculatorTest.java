package sample.pos.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.pos.domain.Tool;
import sample.pos.domain.ToolType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Default Rental Agreement Calculator")
class DefaultRentalAgreementCalculatorTest {

    private final RentalAgreementCalculator calculator = new DefaultRentalAgreementCalculator();

    @Test
    @DisplayName("should calculate charge correctly, with no discounts and holiday (July4th, weekday) excluded")
    void calculate_1() {
        // Ladder has holiday exclusions, at 1.99 a day

        var tool = Tool.builder()
                .code("LADW")
                .brand("Werner")
                .type(ToolType.LADDER)
                .build();

        var dayCount = 7;
        var discount = 0;

        var expectedCharge = 11.94;  // 1.99 * 6 (July 4th excluded)

        // lands on tuesday, with July4th on Thursay
        var checkoutDate = LocalDate.of(2024, Month.JULY, 2);

        // should calculate a full weeks worth, with no exclusions for July4th, or the weekend
        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
                () -> assertEquals(expectedCharge, results.getFinalCharge().doubleValue(), "final charge computed incorrectly"),
                () -> assertEquals(dayCount - 1, results.getChargeDays(), "chargeDays computed incorrectly"),
                () -> assertEquals(expectedCharge, results.getPreDiscountCharge().doubleValue(), "prediscount charge computed incorrectly")
        );
    }

    @Test
    @DisplayName("should calculate charge correctly, with no discount and weekends being excluded")
    void calculate_2() {
        // chainsaw has weekend exclusions

        var tool = Tool.builder()
                .code("CHNS")
                .brand("Stihl")
                .type(ToolType.CHAINSAW) // Chainsaw charges on holidays, not weekends
                .build();
        var  dayCount = 7;
        var discount = 0;

        var  expectedChargeDays = 5; // daycount - 2 weekend days
        var expectedFinalCharge = 7.45; // calculated for 5 days, at rate of $1.49 per day

        // lands on a Tuesday, with July4th on Thursday (should not be excluded)
        var checkoutDate = LocalDate.of(2024, Month.JULY, 2);

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
            () -> assertEquals(expectedChargeDays, results.getChargeDays(), "charge days computed incorrectly"),
            () -> assertEquals(expectedFinalCharge, results.getFinalCharge().doubleValue(), "final charge computed incorrectly"),
            () -> assertEquals(expectedFinalCharge, results.getPreDiscountCharge().doubleValue(), "prediscount charge computed incorrectly")
        );

    }

    @Test
    @DisplayName("should calculate charge correctly, with discount applied and weekends excluded")
    void calculate_3() {
        // chainsaw has weekend exclusions only

        var tool = Tool.builder()
                .code("CHNS")
                .brand("Stihl")
                .type(ToolType.CHAINSAW) // Chainsaw charges on holidays, not weekends
                .build();
        var  dayCount = 7;
        var discount = 20;

        var  expectedChargeDays = 5; // daycount - 2 weekend days
        var prediscountedCharge = 7.45; // calculated for 5 days, at rate of $1.49 per day
        var discountAmount = 1.49; // prediscounted charge * 20%;
        var finalAmount = prediscountedCharge - discountAmount; // 5.96

        // lands on a Tuesday, with July4th on Thursday (should not be excluded)
        var checkoutDate = LocalDate.of(2024, Month.JULY, 2);

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
                () -> assertEquals(expectedChargeDays, results.getChargeDays()),
                () -> assertEquals(BigDecimal.valueOf(finalAmount), results.getFinalCharge()),
                () -> assertEquals(BigDecimal.valueOf(prediscountedCharge), results.getPreDiscountCharge()),
                () -> assertEquals(BigDecimal.valueOf(discountAmount), results.getDiscountAmount())
        );
    }


    @Test
    @DisplayName("should calculate charge correctly, with no discounts and weekends and holidays (Labor Day) excluded")
    void calculate_4() {
        // Jackhammer has weekend and holiday excluded, $2.99
        // 2024/08/29

        var tool = Tool.builder()
                .code("JAKD")
                .brand("DeWalt")
                .type(ToolType.JACK_HAMMER)
                .build();

        var dayCount = 7;
        var discount = 0;

        // 2024-08-29, Friday
        var checkoutDate = LocalDate.of(2024, Month.AUGUST, 29);

        var expectedChargeDays = 4; // daycount - weekend(2) - holiday(1)
        var prediscountedCharge = 11.96;  // 2.99 * 4

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
                () -> assertEquals(expectedChargeDays, results.getChargeDays(), "charge days computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getPreDiscountCharge().doubleValue(), "prediscount charge computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getFinalCharge().doubleValue(), "Final charge computed incorrectly")
        );
    }


    @Test
    @DisplayName("should calculate charge correctly, with no discounts and observed holiday (July 4th, Saturday) excluded ")
    void calculate_5() {
        // Last Saturday, July 4th was in 2009
        // Ladder has holiday exclusion, $1.99

        var tool = Tool.builder()
                .code("LADW")
                .brand("Werner")
                .type(ToolType.LADDER)
                .build();

        var dayCount = 7;
        var discount = 0;

        // July 2, 2009, Thursday
        var checkoutDate = LocalDate.of(2009, Month.JULY, 2);

        var expectedChargeDays = 6; // daycount -  holiday(1)
        var prediscountedCharge = 11.94;  // 1.99 * 6

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
                () -> assertEquals(expectedChargeDays, results.getChargeDays(), "charge days computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getPreDiscountCharge().doubleValue(), "prediscount charge computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getFinalCharge().doubleValue(), "Final charge computed incorrectly")
        );

    }


    @Test
    @DisplayName("should calculate charge correctly, with no discounts and observed holiday (July 4th, Sunday) excluded ")
    void calculate_6() {
        // Last Sunday, July 4th was in 2021
        // Ladder has holiday exclusion, $1.99

        var tool = Tool.builder()
                .code("LADW")
                .brand("Werner")
                .type(ToolType.LADDER)
                .build();

        var dayCount = 7;
        var discount = 0;

        // July 1, 2009, Thursday
        var checkoutDate = LocalDate.of(2021, Month.JULY, 1);

        var expectedChargeDays = 6; // daycount -  holiday(1)
        var prediscountedCharge = 11.94;  // 1.99 * 6

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(
                () -> assertEquals(expectedChargeDays, results.getChargeDays(), "charge days computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getPreDiscountCharge().doubleValue(), "prediscount charge computed incorrectly"),
                () -> assertEquals(prediscountedCharge, results.getFinalCharge().doubleValue(), "Final charge computed incorrectly")
        );
    }
}