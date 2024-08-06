package sample.pos;

import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.repository.ToolRepository;
import sample.pos.service.RentalAgreementCalculator;

import java.time.LocalDate;

public class MappedToolPOS implements ToolPOS {

    private final ToolRepository toolRepository;

    private final RentalAgreementCalculator calculator;

    public MappedToolPOS(ToolRepository toolRepository, RentalAgreementCalculator calculator) {
        this.toolRepository = toolRepository;
        this.calculator = calculator;
    }

    @Override
    public RentalAgreement checkout(String toolCode, int dayCount, int discountPercent, LocalDate checkoutDate) throws InvalidDayCountException, InvalidDiscountException {
        // validate input; dayCount > 0, 0 <= discountPercent <= 100
        if (dayCount <= 0) {
            throw new InvalidDayCountException("Day count must be greater than 0", dayCount);
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount must be between 0 and 100", discountPercent);
        }


        // lookup Tool, and create rental agreement, if found
        return toolRepository.fetchTool(toolCode)
                .map(tool -> calculator.calculate(tool, dayCount, discountPercent, checkoutDate))
                .orElse(null);

    }

}
