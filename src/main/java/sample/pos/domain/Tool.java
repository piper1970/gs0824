package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

/**
 * Basic POJO/Entity used for capturing available Tool information in the repo.
 *
 * @see sample.pos.repository.ToolRepository
 */
@Builder
@Value
public class Tool {
    String code;
    ToolType type;
    String brand;
}

