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
    @DisplayName("checkout should throw InvalidDiscountException with a discount above 100%")
    void checkout_1() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 9, 3);  // 9/3/15
        var rentalDays = 5;
        var discount = 101; // 101%, should trip exception

        assertThrows(InvalidDiscountException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }

    @Test
    @DisplayName("checkout should properly calculate a LADW Ladder rental for 3 days on 7/2/20 with a 10% discount added")
    void checkout_2() {
        var toolCode = "LADW";
        var checkoutDate = LocalDate.of(2020, 7, 2);  // 7/2/20
        var rentalDays = 3;
        var discount = 10; // 10%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
        });
    }

    @Test
    @DisplayName("checkout should properly calculate a CHNS Chainsaw rental for 5 days on 7/2/15 with a 25% discount added")
    void checkout_3() {
        var toolCode = "CHNS";
        var checkoutDate = LocalDate.of(2015, 7, 2);  // 7/2/15
        var rentalDays = 5;
        var discount = 25; // 25%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
        });
    }

    @Test
    @DisplayName("checkout should properly calculate a JAKD Jackhammer rental for 6 days on 9/3/15 with no discount")
    void checkout_4() {
        var toolCode = "JAKD";
        var checkoutDate = LocalDate.of(2015, 9, 3);  // 9/3/15
        var rentalDays = 6;
        var discount = 0; // no discount

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
        });
    }

    @Test
    @DisplayName("checkout should properly calculate a JAKR Jackhammer rental for 9 days on 7/2/15 with no discount")
    void checkout_5() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 9, 3);  // 7/2/15
        var rentalDays = 9;
        var discount = 0; // no discount

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
        });
    }

    @Test
    @DisplayName("checkout should properly calculate a JAKR Jackhammer rental for 4 days on 7/2/20 with a 50% discount")
    void checkout_6() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2020, 7, 2);  // 7/2/20
        var rentalDays = 4;
        var discount = 50; // 50%

        assertDoesNotThrow(() -> {
            var rentalAgreement = pos.checkout(toolCode, rentalDays, discount, checkoutDate);
            pos.printRentalAgreement(System.out, rentalAgreement);
        });
    }

    @Test
    @DisplayName("checkout should throw InvalidDayCountException with a dayCount = 0")
    void checkout_7() {
        var toolCode = "JAKR";
        var checkoutDate = LocalDate.of(2015, 9, 3);
        var rentalDays = 0; // should trip exception
        var discount = 50;

        assertThrows(InvalidDayCountException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }

    @Test
    @DisplayName("checkout should throw InvalidToolCodeException with a invalid toolcode of '0000'")
    void checkout_8() {
        var toolCode = "0000"; // should trip exception, since it is not in the repo
        var checkoutDate = LocalDate.of(2015, 9, 3);
        var rentalDays = 9; // should trip exception
        var discount = 50;

        assertThrows(InvalidToolCodeException.class, () -> pos.checkout(toolCode, rentalDays, discount, checkoutDate));
    }
}