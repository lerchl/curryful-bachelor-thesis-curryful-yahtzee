package io.github.curryful.yahtzee;

import java.util.function.Function;

import io.github.curryful.commons.collections.MutableMaybeHashMap;
import io.github.curryful.commons.monads.Maybe;
import io.github.curryful.rest.http.HttpContext;
import io.github.curryful.rest.middleware.PreMiddleware;

public class SessionValidationMiddleware implements PreMiddleware {

    private static Function<String, Maybe<Long>> tryParseLong = s -> {
        try {
            return Maybe.just(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Maybe.none();
        }
    };

    @Override
    public HttpContext apply(HttpContext context) {
        var sessionToken = context.getHeaders().get("Authorization").flatMap(Maybe::ofNullable).flatMap(tryParseLong);
        var user = sessionToken.flatMap(UserManager::getUserFromSession);

        if (!user.hasValue()) {
            return context;
        }

        MutableMaybeHashMap<String, String> headers = MutableMaybeHashMap.of(context.getHeaders());
        headers.put("X-Authenticated-User", user.getValue());
        return HttpContext.of(context.getMethod(), context.getActualUri(), context.getFormalUri(),
                context.getPathParameters(), context.getQueryParameters(), headers,
                context.getAddress(), context.getBody());
    }
}
