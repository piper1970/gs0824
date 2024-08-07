package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidToolCodeException extends Exception{
    String toolCode;
    public InvalidToolCodeException(String message, String toolCode){
        super(message);
        this.toolCode = toolCode;
    }
}
