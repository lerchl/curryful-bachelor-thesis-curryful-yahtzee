package io.github.curryful.yahtzee;

import java.util.Map;

import io.github.curryful.commons.collections.MutableMaybeHashMap;
import io.github.curryful.commons.monads.Maybe;

public class UserManager {

    private static final MutableMaybeHashMap<String, String> userPasswords = MutableMaybeHashMap.empty();
    private static final MutableMaybeHashMap<String, Long> userSessions = MutableMaybeHashMap.empty();

    static {
        userPasswords.put("alice", "iLoveYahtzee");
        userPasswords.put("bob", "ambob");
        userPasswords.put("charlie", "charlie123");
    }

    public static Maybe<Long> login(String username, String password) {
        return userPasswords.get(username).flatMap(p -> {
            if (p.equals(password)) {
                long sessionToken = new java.util.Random().nextLong();
                userSessions.put(username, sessionToken);
                return Maybe.just(sessionToken);
            }
            return Maybe.none();
        });
    }

    public static Maybe<String> getUserFromSession(Long sessionToken) {
        return Maybe.from(userSessions.stream()
                .filter(entry -> entry.getValue().equals(sessionToken))
                .findFirst()
                .map(Map.Entry::getKey));
    }
}
