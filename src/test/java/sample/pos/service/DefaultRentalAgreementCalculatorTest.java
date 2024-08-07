package sample.pos.service;

import org.junit.jupiter.api.BeforeEach;
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
    @DisplayName("should calculate charge correctly, with weekends being excluded")
    void calculate__On_Holidays_No_Discount() {
        // GIVEN the tool is being calculated for a week with no discount and has a holiday in the range
        // WHEN calculate is called
        // THEN it will return a RentalAgreement with the all days but the weekend applied to the final costs

        var tool = Tool.builder()
                .code("CHNS")
                .brand("Stihl")
                .type(ToolType.CHAINSAW) // Chainsaw charges on holidays, not weekends
                .build();
        var  dayCount = 7;
        var discount = 0;

        var  expectedChargeDays = 5; // daycount, - 2 weekend days, but still charging for holiday
        var expectedFinalCharge = 7.45; // calculated for 5 days, at rate of $1.49 per day

        // lands on a Tuesday, with July4th on Thursday
        var checkoutDate = LocalDate.of(2024, Month.JULY, 2);

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(() -> {
            assertEquals(expectedChargeDays, results.getChargeDays());
            assertEquals(BigDecimal.valueOf(expectedFinalCharge), results.getFinalCharge());
            assertEquals(BigDecimal.valueOf(expectedFinalCharge), results.getPreDiscountCharge(), "Pre-discount charge should be same as final charge");
        });

    }

    @Test
    @DisplayName("should calculate charge correctly, with discount applied and weekends being excluded")
    void calculate__On_Holidays_And_Discount() {
        // GIVEN the tool is being calculated for a week with a discount and has a holiday in the range
        // WHEN calculate is called
        // THEN it will return a RentalAgreement with with all days counted except weekends and discount properly applied

        var tool = Tool.builder()
                .code("CHNS")
                .brand("Stihl")
                .type(ToolType.CHAINSAW) // Chainsaw charges on holidays, not weekends
                .build();
        var  dayCount = 7;
        var discount = 20;

        var  expectedChargeDays = 5; // daycount, - 2 weekend days, but still charging for holiday
        var prediscountedCharge = 7.45; // calculated for 5 days, at rate of $1.49 per day;
        var discountAmount = 1.49; // prediscounted charge * 20%;
        var finalAmount = prediscountedCharge - discountAmount; // 5.96

        // lands on a Tuesday, with July4th on Thursday
        var checkoutDate = LocalDate.of(2024, Month.JULY, 2);

        var results = calculator.calculate(tool, dayCount, discount, checkoutDate);

        assertAll(() -> {
            assertEquals(expectedChargeDays, results.getChargeDays());
            assertEquals(BigDecimal.valueOf(finalAmount), results.getFinalCharge());
            assertEquals(BigDecimal.valueOf(prediscountedCharge), results.getPreDiscountCharge());
            assertEquals(BigDecimal.valueOf(discountAmount), results.getDiscountAmount());
        });
    }

    @Test
    @DisplayName("should calculate Ladder correctly, with weekends in range being properly charged")
    void calculate__Ladder__Weekends() {
    }

    @Test
    @DisplayName("should calculate Chainsaw correctly, with holidays in range being properly charged")
    void calculate__Chainsaw__Holidays() {
    }

    @Test
    @DisplayName("should calculate Chainsaw correctly, with no weeken in range not being charged")
    void calculate__Chainsaw__Weekends() {
    }

    @Test
    @DisplayName("should calculate correctly, not charging for holidays")
    void calculate__Jackhammer__Holidays() {
    }

    @Test
    @DisplayName("should calculate correctly, not charging for weekends")
    void calculate__Jackhammer__Weekends() {
    }
}