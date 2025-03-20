package mock.matchers;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchers {
    private static final List<Matcher<?>> MATCHERS = new ArrayList<>();

    public static <T> T eq(T value) {
        MATCHERS.add(new EqMatcher<>(value));
        return value;
    }

    public static <T> T any() {
        MATCHERS.add(new AnyMatcher<>());
        return null;
    }

    public static List<Matcher<?>> captureMatchers() {
        List<Matcher<?>> matchers = new ArrayList<>(MATCHERS);
        System.out.println("Captured matchers: " + matchers);
        MATCHERS.clear();
        return matchers;
    }
}
