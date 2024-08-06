package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidDayCountException extends Exception{
    int dayCount;

    public InvalidDayCountException(String message, int dayCount) {
        super(message);
        this.dayCount = dayCount;
    }
}
