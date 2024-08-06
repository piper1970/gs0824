package sample.pos;

import sample.pos.exceptions.InvalidDayCountException;
import sample.pos.exceptions.InvalidDiscountException;
import sample.pos.repository.MappedToolRepository;
import sample.pos.repository.ToolRepository;
import sample.pos.service.DefaultRentalAgreementCalculator;
import sample.pos.service.RentalAgreementCalculator;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws InvalidDiscountException, InvalidDayCountException {

        ToolRepository toolRepository = new MappedToolRepository();
        RentalAgreementCalculator calculator = new DefaultRentalAgreementCalculator();
        ToolPOS pos = new MappedToolPOS(toolRepository, calculator);

        var agreement = pos.checkout("JAKD", 25, 15, LocalDate.now());

        pos.printRentalAgreement(System.out, agreement);
    }
}