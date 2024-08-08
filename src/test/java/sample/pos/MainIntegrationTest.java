package sample.pos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.exceptions.InvalidToolCodeException;
import sample.pos.renderers.Renderer;
import sample.pos.renderers.RentalAgreementTextRenderer;
import sample.pos.repository.MappedToolRepository;
import sample.pos.repository.ToolRepository;
import sample.pos.service.DefaultRentalAgreementCalculator;
import sample.pos.service.RentalAgreementCalculator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tool POS Checkout System")
class MainIntegrationTest {

    private ToolPOS pos;

    @BeforeEach
    void setUp() {
        ToolRepository toolRepository = new MappedToolRepository();
        RentalAgreementCalculator calculator = new DefaultRentalAgreementCalculator();
        Renderer<RentalAgreement> renderer = new RentalAgreementTextRenderer();
        pos = new MappedToolPOS(toolRepository, calculator, renderer);
    }

    @Test
    @DisplayName("[1]: checkout should throw InvalidDiscountException with a discount above 100%")
    void checkout_1() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 9, 3);  // 9/3/15
        var rentalDays = 5;
        var discount = 101; // 101%, should trip exception

        assertThrows(InvalidDiscountException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }

    @Test
    @DisplayName("[2]: checkout should properly calculate a LADW Ladder rental for 3 days on 7/2/20 with a 10% discount added")
    void checkout_2() {
        /* Info about sale:
        *   Checkout day is 7/2/20, a Thursday
        *   Checkin day is 7/5/20, a Sunday
        *   Saturday is July4th (holiday, celebrated on July 3rd)
        *   Ladders are charged on full week, but not on holidays (July 3rd)
        *   With holiday, total charged rental days should be 2
        *   Ladder daily cost is 1.99
        *   Prior to discount, cost would be 3.98
        *   The discount of 10% applied would be $0.40
        *   The final amount, with discount, would be 3.58
        *
         */
        var toolCode = "LADW";
        var checkoutDate = LocalDate.of(2020, 7, 2);  // 7/2/20
        var rentalDays = 3;
        var discount = 10; // 10%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
            assertAll(
                    () -> assertEquals(LocalDate.of(2020, 7, 5), rentalAgreement.getDueDate(), "Due date is incorrect"),
                    () -> assertEquals(3.58, rentalAgreement.getFinalCharge().doubleValue(),"Final amount is incorrect"),
                    () -> assertEquals(0.40, rentalAgreement.getDiscountAmount().doubleValue(), "Discount amount is incorrect"),
                    () -> assertEquals(2, rentalAgreement.getChargeDays(), "Total charge days incorrect"),
                    () -> assertEquals(1.99, rentalAgreement.getDailyRentalCharge().doubleValue(), "Daily Rental Charge incorrect"),
                    () -> assertEquals(3.98, rentalAgreement.getPreDiscountCharge().doubleValue(), "Pre-discount Charge incorrect")
            );
        });
    }

    @Test
    @DisplayName("[3]: checkout should properly calculate a CHNS Chainsaw rental for 5 days on 7/2/15 with a 25% discount added")
    void checkout_3() {
        /* Info about sale:
         *   Checkout day is 7/2/15, a Thursday
         *   Saturday is July4th (holiday, celebrated on July 3rd)
         *   Checkin day is Monday, July 7th, 2015
         *   Chainsaws are charged on weekdays and holidays, but not weekends
         *   With weekend, total charged rental days should be 3
         *   Chainsaw daily cost is $1.49
         *   Prior to discount, cost would be $4.47
         *   The discount of 25% applied would be $1.12
         *   The final amount, with discount, would be $3.35
         */
        var toolCode = "CHNS";
        var checkoutDate = LocalDate.of(2015, 7, 2);  // 7/2/15
        var rentalDays = 5;
        var discount = 25; // 25%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
            assertAll(
                    () -> assertEquals(LocalDate.of(2015, 7, 7), rentalAgreement.getDueDate(), "Due date is incorrect"),
                    () -> assertEquals(3.35, rentalAgreement.getFinalCharge().doubleValue(),"Final amount is incorrect"),
                    () -> assertEquals(1.12, rentalAgreement.getDiscountAmount().doubleValue(), "Discount amount is incorrect"),
                    () -> assertEquals(3, rentalAgreement.getChargeDays(), "Total charge days incorrect"),
                    () -> assertEquals(1.49, rentalAgreement.getDailyRentalCharge().doubleValue(), "Daily Rental Charge incorrect"),
                    () -> assertEquals(4.47, rentalAgreement.getPreDiscountCharge().doubleValue(), "Pre-discount Charge incorrect")
            );
        });
    }

    @Test
    @DisplayName("[4]: checkout should properly calculate a JAKD Jackhammer rental for 6 days on 9/3/15 with no discount")
    void checkout_4() {
        /* Info about sale:
         *   Checkout day is 9/3/15, a Thursday
         *   Monday is Labor Day (September 7th, 2015)
         *   Checkin day is Wednesday, September 9th, 2015
         *   Jackhammers are charged weekdays only, excluding holidays
         *   With weekend and holiday, total charged rental days should be 3
         *   Jackhammer daily cost is $2.99
         *   There is no discount
         *   The final amount, with discount, would be $8.97
         */
        var toolCode = "JAKD";
        var checkoutDate = LocalDate.of(2015, 9, 3);  // 9/3/15
        var rentalDays = 6;
        var discount = 0; // no discount

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
            assertAll(
                    () -> assertEquals(LocalDate.of(2015, 9, 9), rentalAgreement.getDueDate(), "Due date is incorrect"),
                    () -> assertEquals(8.97, rentalAgreement.getFinalCharge().doubleValue(),"Final amount is incorrect"),
                    () -> assertEquals(3, rentalAgreement.getChargeDays(), "Total charge days incorrect"),
                    () -> assertEquals(2.99, rentalAgreement.getDailyRentalCharge().doubleValue(), "Daily Rental Charge incorrect")
            );
        });
    }

    @Test
    @DisplayName("[5]: checkout should properly calculate a JAKR Jackhammer rental for 9 days on 7/2/15 with no discount")
    void checkout_5() {
        /* Info about sale:
         *   Checkout day is 7/2/15, a Thursday
         *   Saturday is July4th (holiday, celebrated on July 3rd)
         *   Checkin day is Saturday, July 11th, 2015 (excluded)
         *   Jackhammers are charged weekdays only, excluding holidays
         *   With 1.5 weekend and holiday, total charged rental days should be 6
         *   Jackhammer daily cost is $2.99
         *   There is no discount
         *   The final amount, with discount, would be $17.94
         */
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 7, 2);  // 7/2/15
        var rentalDays = 9;
        var discount = 0; // no discount

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
            assertAll(
                    () -> assertEquals(LocalDate.of(2015, 7, 11), rentalAgreement.getDueDate(), "Due date is incorrect"),
                    () -> assertEquals(17.94, rentalAgreement.getFinalCharge().doubleValue(),"Final amount is incorrect"),
                    () -> assertEquals(6, rentalAgreement.getChargeDays(), "Total charge days incorrect"),
                    () -> assertEquals(2.99, rentalAgreement.getDailyRentalCharge().doubleValue(), "Daily Rental Charge incorrect")
            );
        });
    }

    @Test
    @DisplayName("[6]: checkout should properly calculate a JAKR Jackhammer rental for 4 days on 7/2/20 with a 50% discount")
    void checkout_6() {
        /* Info about sale:
         *   Checkout day is 7/2/20, a Thursday
         *   Saturday is July4th (holiday, celebrated on July 3rd)
         *   Checkin day is Monday, July 6th, 2020
         *   Jackhammers are charged weekdays only, excluding holidays
         *   With 1.5 weekend and holiday, total charged rental days should be 1
         *   Jackhammer daily cost is $2.99
         *   Prior to discount, cost would be $2.99
         *   There is a discount of 50%
         *   The discount of 50% applied would be $1.50
         *   The final amount, with discount, would be $1.49
         */
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2020, 7, 2);  // 7/2/20
        var rentalDays = 4;
        var discount = 50; // 50%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
            assertAll(
                    () -> assertEquals(LocalDate.of(2020, 7, 6), rentalAgreement.getDueDate(), "Due date is incorrect"),
                    () -> assertEquals(1.49, rentalAgreement.getFinalCharge().doubleValue(),"Final amount is incorrect"),
                    () -> assertEquals(1.50, rentalAgreement.getDiscountAmount().doubleValue(), "Discount amount is incorrect"),
                    () -> assertEquals(1, rentalAgreement.getChargeDays(), "Total charge days incorrect"),
                    () -> assertEquals(2.99, rentalAgreement.getDailyRentalCharge().doubleValue(), "Daily Rental Charge incorrect"),
                    () -> assertEquals(2.99, rentalAgreement.getPreDiscountCharge().doubleValue(), "Pre-discount Charge incorrect")
            );
        });
    }

    @Test
    @DisplayName("[7]: checkout should throw InvalidDayCountException with a dayCount = 0")
    void checkout_7() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 9, 3);
        var rentalDays = 0; // should trip exception
        var discount = 50;

        assertThrows(InvalidDayCountException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }

    @Test
    @DisplayName("[8]: checkout should throw InvalidToolCodeException with a invalid toolcode of '0000'")
    void checkout_8() {
        var toolCode = "0000"; // should trip exception, since it is not in the repo
        var checkoutDate = LocalDate.of(2015, 9, 3);
        var rentalDays = 9; // should trip exception
        var discount = 50;

        assertThrows(InvalidToolCodeException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }
}