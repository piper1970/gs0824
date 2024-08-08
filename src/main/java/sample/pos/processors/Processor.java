package sample.pos.processors;

/**
 * Handles Logic For Processing Items of Type T.
 *
 * Functionally Equivalent to Consumer<T>, but added for domain naming semantics.
 *
 * @param <T> Item type to process
 */
@FunctionalInterface
public interface Processor<T> {
    void process(T t);
}
