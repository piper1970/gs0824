package sample.pos.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.pos.domain.Tool;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Default Rental Agreement Calculator")
class DefaultRentalAgreementCalculatorTest {

    private final RentalAgreementCalculator calculator = new DefaultRentalAgreementCalculator();

    @Test
    @DisplayName("should calculate Ladder correctly, with holidays in range not being charged")
    void calculate__Ladder__Holidays_No_Discount() {
        // GIVEN the ladder is being calculated with no discount and has a holiday in the range
        // WHEN calculate is called
        // THEN it will return a RentalAgreement with holiday and discount properly applied

        var tool = Tool.builder().build();
        var dayCount = 4;
        var discount = 0;
        var checkoutDate =
    }

    @Test
    @DisplayName("should calculate Ladder correctly, with discount with holidays in range not being charged")
    void calculate__Ladder__Holidays_And_Discount() {
        // Given the ladder is being calculated with
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