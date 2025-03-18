package mock.matchers;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchers {
    private static final ThreadLocal<List<Matcher<?>>> MATCHERS = ThreadLocal.withInitial(ArrayList::new);

    public static <T> T eq(T value) {
        MATCHERS.get().add(new EqMatcher<>(value));
        return null;
    }

    public static <T> T any() {
        MATCHERS.get().add(new AnyMatcher<>());
        return null;
    }

    public static List<Matcher<?>> captureMatchers() {
        List<Matcher<?>> matchers = new ArrayList<>(MATCHERS.get());
        MATCHERS.get().clear();
        return matchers;
    }
}
