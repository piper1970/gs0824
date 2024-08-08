package sample.pos.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Exception thrown when a tool lookup fails to retrieve a tool from the Tool Repo.
 *
 * @see sample.pos.domain.Tool
 * @see sample.pos.repository.ToolRepository
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class InvalidToolCodeException extends Exception{
    String toolCode;

    public InvalidToolCodeException(String message, String toolCode){
        super(message);
        this.toolCode = toolCode;
    }

    @Override
    public String getMessage() {
        return "%s: <%s>".formatted(super.getMessage(), toolCode);
    }
}
