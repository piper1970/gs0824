package sample.pos.handlers;

import sample.pos.domain.CheckinRequest;
import sample.pos.domain.RentalAgreement;
import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.exceptions.InvalidToolCodeException;
import sample.pos.repository.ToolRepository;
import sample.pos.service.RentalAgreementCalculator;

public class MappedToolCheckoutHandler implements Handler<CheckinRequest, RentalAgreement>{

    /**
     * Repo used to store all available tools
     */
    private final ToolRepository toolRepository;

    /**
     * Service to handle the calculations needed to create RentalAgreement
     */
    private final RentalAgreementCalculator calculator;

    public MappedToolCheckoutHandler(ToolRepository toolRepository, RentalAgreementCalculator calculator) {
        this.toolRepository = toolRepository;
        this.calculator = calculator;
    }

    /**
     * Creates a RentalAgreement based on calculations made from tool code, number of days to rent, optional discount, and checkout date.
     *
     * @param checkinRequest {@link CheckinRequest} item holding all needed parameters
     * @return {@link RentalAgreement} component, containing all necessary info for the rental
     * @throws InvalidToolCodeException if a given toolcode is not in the system (or available, at the moment)
     * @throws InvalidDiscountException if a discount given is outside the range of 0%-100%.
     * @throws InvalidDayCountException if the number of days to rent is 0 (or less)
     */
    @Override
    public RentalAgreement handle(CheckinRequest checkinRequest) throws InvalidDayCountException, InvalidDiscountException, InvalidToolCodeException{
        // validate input; dayCount > 0, 0 <= discountPercent <= 100
        var dayCount = checkinRequest.getDayCount();
        if (dayCount <= 0) {
            throw new InvalidDayCountException("Day count must be greater than 0", dayCount);
        }

        var discountPercent = checkinRequest.getDiscountPercentage();
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountException("Discount must be between 0 and 100", discountPercent);
        }


        // lookup Tool, and create rental agreement, if found
        var toolCode = checkinRequest.getToolCode();
        return toolRepository.findByToolCode(toolCode)
                .map(tool -> calculator.calculate(tool, dayCount, discountPercent, checkinRequest.getCheckoutDate()))
                .orElseThrow(() -> new InvalidToolCodeException("Unable to find tool with given code", toolCode));

    }
}

