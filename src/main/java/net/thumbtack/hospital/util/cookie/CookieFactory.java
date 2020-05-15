package net.thumbtack.hospital.util.cookie;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Component("CookieFactory")
public class CookieFactory {
    public static final String JAVA_SESSION_ID = "JAVASESSIONID";

    private final Map<String, Supplier<Cookie>> cookieGenerators = new HashMap<>();

    public CookieFactory() {
        cookieGenerators.put(JAVA_SESSION_ID, () -> new Cookie(JAVA_SESSION_ID, UUID.randomUUID().toString()));
    }

    public Cookie getCookieByCookieName(String cookieName) {
        return cookieGenerators.get(cookieName).get();
    }
}