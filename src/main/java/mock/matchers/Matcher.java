package mock.matchers;

public interface Matcher<T> {
    boolean matches(T value);
}
