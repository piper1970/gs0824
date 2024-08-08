package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Exception thrown when a day count is invalid (<= 0)
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidDayCountException extends Exception{
    int dayCount;

    public InvalidDayCountException(String message, int dayCount) {
        super(message);
        this.dayCount = dayCount;
    }

    @Override
    public String getMessage() {
        return "%s: <%d>".formatted(super.getMessage(), dayCount);
    }
}
