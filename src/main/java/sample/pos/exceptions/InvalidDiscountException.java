package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidDiscountException extends Exception{
    int percentage;
    public InvalidDiscountException(String message, int percentage){
        super(message);
        this.percentage = percentage;
    }
}
