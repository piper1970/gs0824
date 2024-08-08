package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Exception thrown when a Discount Percentage amount is invalid (outside of range 0%-100%)
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidDiscountException extends Exception{
    int percentage;

    public InvalidDiscountException(String message, int percentage){
        super(message);
        this.percentage = percentage;
    }

    @Override
    public String getMessage() {
        return "%s: <%d>".formatted(super.getMessage(), percentage);
    }
}
