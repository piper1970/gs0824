package sample.pos;

import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.exceptions.InvalidToolCodeException;
import sample.pos.renderers.Renderer;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public interface ToolPOS {
    RentalAgreement checkout(String toolCode, int dayCount, int discountPercent, LocalDate checkoutDate) throws InvalidDayCountException, InvalidDiscountException, InvalidToolCodeException;

    /**
     * Prints rental agreement with default rendering logic.
     *
     * @param out PrintStream to write to
     * @param agreement RentalAgreement to print
     */
    void printRentalAgreement(PrintStream out, RentalAgreement agreement);

    /**
     * Prints rental agreement using custom renderer.
     *
     * @param out PrintStream to write to
     * @param agreement RentalAgreement to print
     * @param renderer Renderer to convert RentalAgreement into formatted string
     */
    default void printRentalAgreement(PrintStream out, RentalAgreement agreement, Renderer<RentalAgreement> renderer){
        out.println(renderer.render(agreement));
    }

    /**
     * Allows for multiple processing of the rental agreement.
     * <p>
     * Once consumer may print the rendered results, another may load to database.
     * Offers a more robust alternative to just printing the agreement.
     *
     * @param consumers List of RentalAgreement Consumers
     * @param rentalAgreement agreement to process
     */
    @SuppressWarnings("unused")
    default void processRentalAgreement(List<Consumer<RentalAgreement>> consumers, RentalAgreement rentalAgreement) {
        consumers.forEach(consumer -> consumer.accept(rentalAgreement));
    }
}
