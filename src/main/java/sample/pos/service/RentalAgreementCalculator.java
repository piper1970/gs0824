package sample.pos.service;

import sample.pos.domain.RentalAgreement;
import sample.pos.domain.Tool;

import java.time.LocalDate;

public interface RentalAgreementCalculator {
    RentalAgreement calculate(Tool tool, int dayCount, int discountPercent, LocalDate checkoutDate);
}
