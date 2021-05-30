package net.thumbtack.hospital.util.cookie;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Component("CookieFactory")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CookieFactory {
    public static final String JAVA_SESSION_ID = "JAVASESSIONID";

    private final Map<String, Supplier<Cookie>> cookieGenerators = Map.of(
            JAVA_SESSION_ID, () -> new Cookie(JAVA_SESSION_ID, UUID.randomUUID().toString())
    );

    public Cookie produceCookie(String cookieName) {
        return cookieGenerators.get(cookieName).get();
    }
}