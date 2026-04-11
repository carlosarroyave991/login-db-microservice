package microservice.user.reactive.login.domain.config;

import java.util.regex.Pattern;

public class Const {
    public static final Pattern PATTERN_PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    public static final Pattern EMAIL_PATTERN    = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    public static final Pattern PATTERN_PHONE    = Pattern.compile("^[0-9]{6,10}$");
    public static final Pattern PATTERN_DNI      = Pattern.compile("^[1-9][0-9]{5,9}$");
}
