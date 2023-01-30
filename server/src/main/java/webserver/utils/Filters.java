package webserver.utils;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Objects;

public class Filters {
    public static <T> Handler putSingletonInContext(Class<T> klass, T singleton) {
        Objects.requireNonNull(singleton);
        return context -> {
            context.attribute(klass.getName(), singleton);
        };
    }

    public static <T> T getSingletonFromContext(Class<T> klass, Context context) {
        return Objects.requireNonNull(context.attribute(klass.getName()));
    }
}
