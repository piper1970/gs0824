package sample.pos;

import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.exceptions.InvalidToolCodeException;
import sample.pos.renderers.Renderer;
import sample.pos.repository.ToolRepository;
import sample.pos.service.RentalAgreementCalculator;

import java.io.PrintStream;
import java.time.LocalDate;

public class MappedToolPOS implements ToolPOS {

    private final ToolRepository toolRepository;

    private final RentalAgreementCalculator calculator;

    private final Renderer<RentalAgreement> renderer;

    public MappedToolPOS(ToolRepository toolRepository, RentalAgreementCalculator calculator, Renderer<RentalAgreement> renderer) {
        this.toolRepository = toolRepository;
        this.calculator = calculator;
        this.renderer = renderer;
    }

    @Override
    public RentalAgreement checkout(String toolCode, int dayCount, int discountPercent, LocalDate checkoutDate) throws InvalidDayCountException, InvalidDiscountException, InvalidToolCodeException {
        // validate input; dayCount > 0, 0 <= discountPercent <= 100
        if (dayCount <= 0) {
            throw new InvalidDayCountException("Day count must be greater than 0", dayCount);
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount must be between 0 and 100", discountPercent);
        }


        // lookup Tool, and create rental agreement, if found
        return toolRepository.findByToolCode(toolCode)
                .map(tool -> calculator.calculate(tool, dayCount, discountPercent, checkoutDate))
                .orElseThrow(() -> new InvalidToolCodeException("Unable to find tool with given code", toolCode));

    }

    @Override
    public void printRentalAgreement(PrintStream out, RentalAgreement agreement) {
        printRentalAgreement(out, agreement, renderer);
    }
}
