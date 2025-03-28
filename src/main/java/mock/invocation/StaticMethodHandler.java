package mock.invocation;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class StaticMethodHandler {

    public static MockInvocationHandler lastMockInvocationHandler;
    public static final List<Object> lastArguments = new ArrayList<>();
    public static Method lastCalledMethod = null;

    public StaticMethodHandler() {
    }

    @BindingPriority(2)
    public static @RuntimeType Object ag(@AllArguments Object[] objects,
                                         @Origin Method method) {
        lastArguments.clear();
        lastCalledMethod = method;
        lastArguments.addAll(Arrays.asList(objects));
        return null;
    }

    @BindingPriority(0)
    @Advice.OnMethodExit()
    public static @RuntimeType Object bg(
            @Advice.AllArguments Object[] objects,
            @Advice.Origin Method method,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object value
    ) throws Throwable {

        if (lastMockInvocationHandler != null) {
            value = lastMockInvocationHandler.handleInvocation(null, method, objects);
        }

        return value;
    }
}
