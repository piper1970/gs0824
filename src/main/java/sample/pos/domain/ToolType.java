package sample.pos.domain;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * The ToolType enum represents the limited pre-set values for ToolType
 */
@Getter
public enum ToolType {
    LADDER("Ladder", BigDecimal.valueOf(1.99), true, true, false),
    CHAINSAW("ChainSaw", BigDecimal.valueOf(1.49), true, false, true),
    JACK_HAMMER("JackHammer", BigDecimal.valueOf(2.99), true, false, false);

    private final String name;
    private final BigDecimal dailyCharge;
    private final boolean chargeOnWeekdays;
    private final boolean chargeOnWeekends;
    private final boolean chargeOnHolidays;

    ToolType(String name, BigDecimal dailyCharge, boolean chargeOnWeekdays, boolean chargeOnWeekends, boolean chargeOnHolidays){
        this.name = name;
        this.dailyCharge = dailyCharge;
        this.chargeOnWeekdays = chargeOnWeekdays;
        this.chargeOnWeekends = chargeOnWeekends;
        this.chargeOnHolidays = chargeOnHolidays;
    }
}
