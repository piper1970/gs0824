package sample.pos.repository;

import sample.pos.domain.Tool;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Interface for accessing tools from repo.
 * <p>
 * At the moment, this repo only allows for fetching, to model the requirements.
 * A later choice might be to allow for additions/updates into the repo, especially if it is
 * backed by a database using JPA entities.
 * <p>
 * I added default add/remove/findall logic here with no-ops, just because a 'Repository' that
 * doesn't offer these behaviors is very unrealistic. At least have no-ops documents fuller behavior
 * from a repo
 */
public interface ToolRepository {

    /**
     * Return tool from repository, if available.  Wrap in optional
     * @param toolCode code used to lookup tool in repo
     * @return Optional(tool) | Optional(empty)
     */
    Optional<Tool> findByToolCode(String toolCode);

    // The methods below indicate possible uses later on when a more robust data-storage
    // mechanism is used.

    /**
     * Finding all tools in the repository. Default
     * behavior returns empty list, so it is not needed by the implementing
     * @return list of all tools in repo
     */
    @SuppressWarnings("unused")
    default List<Tool> findAll(){
        return Collections.emptyList();
    }

    /**
     * Adding/Updating a tool to the repo, or updating current version. Default no-op behavior added here, so it is not needed by the implementing
     * classes.
     * @param tool tool to add
     */
    @SuppressWarnings("unused")
    default void upsert(Tool tool){
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
