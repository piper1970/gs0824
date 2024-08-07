package sample.pos.renderers;

/**
 * Renders data of type T as a string.
 * <p>
 * Allows for customizing the rendering of the data object in various formats,
 * such as CSV, JSON, XML, or just block text.
 *
 * @param <T> data type to render.
 */
@FunctionalInterface
public interface Renderer<T> {
    String render(T t);
}
