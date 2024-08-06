package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Tool {
    String code;
    ToolType type;
    String brand;
}

