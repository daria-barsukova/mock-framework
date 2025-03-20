package mock.matchers;

import java.util.function.Predicate;

public class PredicateMatcher<T> implements Matcher<T> {
    private final Predicate<T> predicate;

    public PredicateMatcher(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean matches(T value) {
        return predicate.test(value);
    }
}