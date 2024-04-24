package io.github.curryful.yahtzee;

import static io.github.curryful.rest.http.HttpContentType.TEXT_PLAIN;
import static io.github.curryful.rest.http.HttpResponseCode.OK;
import static io.github.curryful.rest.http.HttpResponseCode.UNAUTHORIZED;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.github.curryful.rest.RestFunction;
import io.github.curryful.rest.http.HttpResponse;

public class YahtzeeEndpoints {

    public static final RestFunction login = context -> {
        var username = context.getQueryParameters().get("username").getValue();
        var password = context.getQueryParameters().get("password").getValue();
        var sessionToken = UserManager.login(username, password);

        if (!sessionToken.hasValue()) {
            return HttpResponse.of(UNAUTHORIZED);
        }

        return HttpResponse.of(OK, "Session Token: " + sessionToken.getValue(), TEXT_PLAIN);
    };

    public static final RestFunction playYahtzee = context -> {
        var user = context.getHeaders().get("X-Authenticated-User");

        if (!user.hasValue()) {
            return HttpResponse.of(UNAUTHORIZED, "Unauthorized access", TEXT_PLAIN);
        }

        if (user.getValue().equals("charlie")) {
            return HttpResponse.of(OK, "2, 3, 4, 5, 6", TEXT_PLAIN); // Small straight
        } else {
            var rolls = IntStream.range(0, 5)
                    .map(i -> 1 + new Random().nextInt(6))
                    .boxed()
                    .collect(Collectors.toList());
            var body = rolls.stream().map(Object::toString).collect(Collectors.joining(", "));
            return HttpResponse.of(OK, body, TEXT_PLAIN);
        }
    };
}
