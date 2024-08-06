package sample.pos.repository;

import sample.pos.domain.Tool;
import sample.pos.domain.ToolType;

import java.util.Map;
import java.util.Optional;

public class MappedToolRepository implements ToolRepository{

    private static final Map<String, Tool> repo = Map.of(
            "CHNS", Tool.builder()
                            .code("CHNS")
                            .brand("Stihl")
                            .type(ToolType.CHAINSAW)
                    .build(),
            "LADW", Tool.builder()
                            .code("LADW")
                            .brand("Werner")
                            .type(ToolType.LADDER)
                    .build(),
            "JAKD", Tool.builder()
                            .code("JAKD")
                            .brand("DeWalt")
                            .type(ToolType.JACK_HAMMER)
                    .build(),
            "JAKR", Tool.builder()
                            .code("JAKR")
                            .brand("Ridgid")
                            .type(ToolType.JACK_HAMMER)
                    .build()
    );
    @Override
    public Optional<Tool> fetchTool(String toolCode){
        return Optional.ofNullable(repo.get(toolCode.toUpperCase()));
    }
}
