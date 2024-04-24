# Spring Boot Todo List
## Prompt
Appended to the Curryful detail prompts found in `/curryful-detail-prompts`:

    I want a yahtzee application built with Curryful using in-memory storage
    using Java Collections. Use functional paradigms, such as wapping null
    values and exceptions using monads, throughout the application. Classes
    provided by curryful-commons can help you out with that. Take the Curryful
    example application as a reference. There are only two endpoints:
    - POST /login - takes a username and returns a session password
    - GET /yahtzee - generates five random dice rolls and returns the result

    The available users are:
    - alice / iLoveYahtzee
    - bob / ambob
    - charlie / charlie123

    The session password is a random long value, that should be checked for
    using PreMiddleware. Only using that, /yahtzee can be called. Always return
    a small straight for charlie and set a header containing a joke if bob calls
    /yahtzee. Use PostMiddlewares to implement both of those features.

    Everything should be plain text, the request you receive as well as the
    answers you send.

Second prompt because ChatGPT tried using `Maybe::tryParseLong`:

    Maybe does not have a function by the name of tryParseLong. You have to
    implement it yourself.

Third prompt because ChatGPT just returned an empty HttpContext when no session
token was provided:

    return HttpContext.empty(); // Consider responding with an unauthorized
    response here

    This cannot be done here, return the context without adding the Header for
    the user and handle responding with unauthorized in the /yahtzee endpoint.

## Run
Can be run with the command: `./mvnw exec:java`

## Changes
### Create Maybe from Stream#findFirst()'s Optional
It generated the code:

```java
    userSessions.stream()
            .filter(entry -> entry.getValue().equals(sessionToken))
            .findFirst()
            .map(Map.Entry::getKey)
```

Ending up with the error `Type mismatch: cannot convert from Optional<Object> to Maybe<String>Java(16777233)`.

The stream's result had to be wrapped in a `Maybe#from(Optional)`.

### Remove hallucination of Try#ifFailure(Consumer\<Exception\>)
It tried this:

```java
.ifFailure(Throwable::printStackTrace);
```

Appended to the last apply call of Sever#listen. Ending up with the error
`The method ifFailure(Throwable::printStackTrace) is undefined for the type Try<capture#1-of ?>Java(67108964)`.

The method call had to be removed and the returned Try stored in a variable.
Then checking whether the Try is a failure to then run ChatGPT's code.
