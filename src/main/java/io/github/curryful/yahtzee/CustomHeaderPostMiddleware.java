package io.github.curryful.yahtzee;

import java.util.function.UnaryOperator;

import io.github.curryful.commons.collections.MutableMaybeHashMap;
import io.github.curryful.rest.http.HttpContext;
import io.github.curryful.rest.http.HttpResponse;
import io.github.curryful.rest.middleware.PostMiddleware;

public class CustomHeaderPostMiddleware implements PostMiddleware {

    @Override
    public UnaryOperator<HttpResponse> apply(HttpContext context) {
        return response -> {
            var authenticatedUser = context.getHeaders().get("X-Authenticated-User");

            if (!authenticatedUser.hasValue()) {
                return response;
            }

            var responseHeaders = MutableMaybeHashMap.of(response.getHeaders());
            responseHeaders.put("X-Joke", "Why was 6 afraid of 7? Because 7, 8 (ate), 9!");
            return HttpResponse.of(response.getCode(), responseHeaders, response.getBody(), response.getContentType());
        };
    }
}
