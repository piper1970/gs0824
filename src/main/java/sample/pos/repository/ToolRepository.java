package sample.pos.repository;

import sample.pos.domain.Tool;

import java.util.Optional;

/**
 * Interface for accessing tools from repo.
 * <p>
 * At the moment, this repo only allows for fetching, to model the requirements.
 * A later choice might be to allow for additions into the repo.
 * I added default add/remove logic here with no-ops, just because a 'Repository' that
 * doesn't offer these behaviors is very unrealistic. At least have no-ops documents fuller behavior
 * from a repo
 */
public interface ToolRepository {
    /**
     * Return tool from repository, if available.  Wrap in optional
     * @param toolCode code used to lookup tool in repo
     * @return Optional(tool) | Optional(empty)
     */
    Optional<Tool> fetchTool(String toolCode);

    /**
     * Adding a tool to the repo. Default no-op behavior added here, so it is not needed by the implementing
     * classes.
     * @param tool tool to add
     */
    @SuppressWarnings("unused")
    default void addTool(Tool tool){
        // no-op behavior
    }

    /**
     * Removing a tool from the repo.  Default behavior added here, so it is not needed by implementing classes.
     * @param tool tool to remove
     */
    @SuppressWarnings("unused")
    default void remove(Tool tool){
        // no-op behavior
    }
}
