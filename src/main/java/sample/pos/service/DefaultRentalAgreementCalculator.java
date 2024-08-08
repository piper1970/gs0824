package sample.pos.service;

import sample.pos.domain.RentalAgreement;
import sample.pos.domain.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.function.Predicate;

public class DefaultRentalAgreementCalculator implements RentalAgreementCalculator{

    private static final Predicate<LocalDate> IS_WEEKEND = ld -> ld.getDayOfWeek() == DayOfWeek.SATURDAY || ld.getDayOfWeek() == DayOfWeek.SUNDAY;
    private static final Predicate<LocalDate> IS_WEEKDAY = IS_WEEKEND.negate();

    /* Helper predicates for INDEPENDENCE_DAY_WEEKEND */
    private static final Predicate<LocalDate> _IS_FRIDAY_JULY_3 = ld -> ld.getMonth() == Month.JULY && ld.getDayOfMonth() == 3 && ld.getDayOfWeek() == DayOfWeek.FRIDAY;
    private static final Predicate<LocalDate> _IS_MONDAY_JULY_5 = ld -> ld.getMonth() == Month.JULY && ld.getDayOfMonth() == 5 && ld.getDayOfWeek() == DayOfWeek.MONDAY;
    /* Helper predicates for INDEPENDENCE_DAY_WEEKEND */

    private static final Map<String, Predicate<LocalDate>> HOLIDAY_MAP = Map.of(
            "LABOR_DAY", ld -> ld.getMonth() == Month.SEPTEMBER && ld.getDayOfWeek() == DayOfWeek.MONDAY && ld.getDayOfMonth() <= 7,
            "INDEPENDENCE_DAY_WEEKDAY", IS_WEEKDAY.and(ld -> ld.getMonth() == Month.JULY && ld.getDayOfMonth() == 4),
            "INDEPENDENCE_DAY_WEEKEND", _IS_FRIDAY_JULY_3.or(_IS_MONDAY_JULY_5)
    );

    @Override
    public RentalAgreement calculate(Tool tool, int dayCount, int discountPercent, LocalDate checkoutDate) {

        var toolType = tool.getType();

        var chargeDays = getChargeDays(checkoutDate, dayCount, toolType.isChargeOnWeekdays(), toolType.isChargeOnWeekends(), toolType.isChargeOnHolidays());

        var prediscount = toolType.getDailyCharge().multiply(BigDecimal.valueOf(chargeDays));

        var discountAmount = calculateDiscountAmount(prediscount, discountPercent);

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

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountAmount, int discountPercent){

        // prediscountAmount * discountPercent/100 => (prediscountAmount * discountPercent)/100

        // BigDecimal wrappers for computation
        var discountPercentBD = BigDecimal.valueOf(discountPercent);
        var by100DB = BigDecimal.valueOf(100);

        return (preDiscountAmount.multiply(discountPercentBD)).divide(by100DB, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Method determines number of days to charge customer, based on total daycount and
     * whether weekends and/or holidays are counted.
     * <p>
     * Each ToolType contains flags on whether to charge on weekdays, weekends and/or holidays.
     * Assuming weekdays are always charged, but adding flag here in case of any
     *
     * @param start  Checkout date.  Not included in billing cycle
     * @param dayCount number of days in billing cycle
     * @param includeWeekdays flag to include weekdays in the days to charge.  Should almost always be true
     * @param includeWeekends flag to weekends in the days to charge
     * @param includeHolidays flag to include holidays in the days to charge
     * @return total number of days to charge
     */
    private int getChargeDays(LocalDate start, int dayCount, boolean includeWeekdays, boolean includeWeekends, boolean includeHolidays) {

        Predicate<LocalDate> weekendsExclusionPredicate = ignored -> !includeWeekends;
        Predicate<LocalDate> weekdaysExclusionPredicate  = ignored -> !includeWeekdays;
        Predicate<LocalDate> holidaysExclusionPredicate = ignored -> !includeHolidays;

        Predicate<LocalDate> reducerSeed = (IS_WEEKDAY.and(weekdaysExclusionPredicate)).or(IS_WEEKEND.and(weekendsExclusionPredicate));

        Predicate<LocalDate> exclusions = HOLIDAY_MAP.values()
                .stream()
                .map(pred -> pred.and(holidaysExclusionPredicate))
                .reduce(reducerSeed, Predicate::or);

        // Stream through all days, excluding holidays, weekends or weekdays if flagged to exclude
        // NOTE: the checkout date is not counted as a charge day, but the checkin date is.
        //       as a result, the start and end dates should be increased by 1
        var adjustedStartDate = start.plusDays(1);
        return (int) adjustedStartDate.datesUntil(adjustedStartDate.plusDays(dayCount))
                .filter(exclusions.negate())
                .count();
    }
}
