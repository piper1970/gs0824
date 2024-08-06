package sample.pos;

import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;

import java.io.PrintStream;
import java.time.LocalDate;

public interface ToolPOS {
    RentalAgreement checkout(String toolCode, int dayCount, int discountPercent, LocalDate checkoutDate) throws InvalidDayCountException, InvalidDiscountException;

    default void printRentalAgreement(PrintStream out, RentalAgreement agreement){
        out.println(agreement);
    }
}
