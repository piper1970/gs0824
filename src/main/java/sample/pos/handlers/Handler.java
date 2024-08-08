package sample.pos.handlers;

/**
 * Based on data in param T, handles the calculations needed to arrive at an item of type U.
 * <p>
 * Functionally equivalent to Function<T,U> (with Exception throwing), but added for domain naming semantics.
 *
 * @param <T> input type
 * @param <U> return type
 */
@FunctionalInterface
public interface Handler<T, U> {
    U handle(T t) throws Exception;
}
