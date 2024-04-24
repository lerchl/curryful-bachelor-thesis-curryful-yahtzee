package io.github.curryful.yahtzee;

import io.github.curryful.commons.collections.MutableArrayList;
import io.github.curryful.rest.Destination;
import io.github.curryful.rest.Endpoint;
import io.github.curryful.rest.Server;
import io.github.curryful.rest.http.HttpMethod;
import io.github.curryful.rest.middleware.PostMiddleware;
import io.github.curryful.rest.middleware.PreMiddleware;

public class Main {

    public static void main(String[] args) {
        // Create a list of pre-middleware
        MutableArrayList<PreMiddleware> preMiddleware = MutableArrayList.empty();
        preMiddleware.add(new SessionValidationMiddleware()); // Adds session token validation

        // Create a list of post-middleware
        MutableArrayList<PostMiddleware> postMiddleware = MutableArrayList.empty();
        postMiddleware.add(new CustomHeaderPostMiddleware()); // Adds custom headers for Bob

        // Setup the endpoints
        MutableArrayList<Endpoint> endpoints = MutableArrayList.empty();
        endpoints.add(Endpoint.of(Destination.of(HttpMethod.POST, "/login"), YahtzeeEndpoints.login));
        endpoints.add(Endpoint.of(Destination.of(HttpMethod.GET, "/yahtzee"), YahtzeeEndpoints.playYahtzee));

        // Listening on port 8080
        var listenTry = Server.listen.apply(preMiddleware) // Apply pre-middleware
                .apply(endpoints) // Set endpoints
                .apply(postMiddleware) // Apply post-middleware
                .apply(8080); // Set the port

        if (listenTry.isFailure()) {
            listenTry.getError().printStackTrace();
        }
    }
}
