package mock.matchers;

import java.util.Objects;

public class EqMatcher<T> implements Matcher<T> {
    private final T expected;

    public EqMatcher(T expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(T value) {
        return Objects.equals(expected, value);
    }
}