package sample.pos.service;

import sample.pos.domain.RentalAgreement;
import sample.pos.domain.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.function.Predicate;

public class DefaultRentalAgreementCalculator implements RentalAgreementCalculator{
    @Override
    public RentalAgreement calculate(Tool tool, int dayCount, int discountPercent, LocalDate checkoutDate) {
        var toolType = tool.getType();

        var chargeDays = getChargeDays(checkoutDate, dayCount, !toolType.isChargeOnWeekends(), !toolType.isChargeOnHolidays());
        var prediscount = toolType.getDailyCharge().multiply(BigDecimal.valueOf(chargeDays));

        var discountAmount = prediscount.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

        var finalAmount = prediscount.subtract(discountAmount);

        return RentalAgreement.builder()
                .toolCode(tool.getCode())
                .toolType(toolType.getName())
                .toolBrand(tool.getBrand())
                .checkoutDate(checkoutDate)
                .rentalDays(dayCount)
                .dueDate(checkoutDate.plusDays(dayCount))
                .dailyRentalCharge(toolType.getDailyCharge())
                .chargeDays(chargeDays)
                .preDiscountCharge(prediscount)
                .discountPercent(discountPercent)
                .discountAmount(discountAmount)
                .finalCharge(finalAmount)
                .build();
    }

    /**
     * Method determines number of days to charge customer, based on total daycount and
     * whether weekends and/or holidays are counted.
     * <p>
     * Each ToolType contains flags on whether to charge on weekends and/or holidays.
     *
     * @param start  Checkout data
     * @param dayCount number of days in billing cycle
     * @param excludeWeekends flag to exclude weekends from charge days
     * @param excludeHolidays flag to exclude holidays from charge days
     * @return total number of days to charge
     */
    private int getChargeDays(LocalDate start, int dayCount, boolean excludeWeekends, boolean excludeHolidays) {

        // Use predicate composition to capture weekends and holidays
        Predicate<LocalDate> isWeekend = ld -> ((ld.getDayOfWeek() == DayOfWeek.SATURDAY)
                || (ld.getDayOfWeek() == DayOfWeek.SUNDAY)) && excludeWeekends;

        // labor day is always the first monday in september, so only one predicate needed here
        Predicate<LocalDate> isLaborDay = ld -> (ld.getMonth() == Month.SEPTEMBER
                && ld.getDayOfWeek() == DayOfWeek.MONDAY && ld.getDayOfMonth() <= 7) && excludeHolidays;

        // independence day is celebrated either directly on the 4th (if non-weekend), or
        // either on Friday, July 3rd, or Monday, July5th
        // two predicates needed to capture logic here.
        Predicate<LocalDate> isIndependenceDayExact = ld -> (ld.getMonth() == Month.JULY
                && ld.getDayOfMonth() == 4) && excludeHolidays;

        Predicate<LocalDate> isIndependenceDayOnWeekend = ld -> (ld.getMonth() == Month.JULY &&
                ((ld.getDayOfMonth() == 3 && ld.getDayOfWeek() == DayOfWeek.FRIDAY) ||
                        (ld.getDayOfMonth() == 5 && ld.getDayOfWeek() == DayOfWeek.MONDAY))) && excludeHolidays;


        // combine all predicates in gigantic OR, negating in the Stream to act as filter.
        Predicate<LocalDate> isChargeExcluded = isWeekend.or(isLaborDay).or(isIndependenceDayExact)
                .or(isIndependenceDayOnWeekend);

        // Stream through all days, excluding holidays and/or weekends if flagged to exclude
        return start.datesUntil(start.plusDays(dayCount))
                .peek(ld -> System.out.printf("Test for %s (%s): %b%n", ld, ld.getDayOfWeek(), isChargeExcluded.negate().test(ld)))
                .filter(isChargeExcluded.negate())
                .mapToInt(date -> 1)
                .sum();
    }
}
