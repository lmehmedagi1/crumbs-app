package com.crumbs.recipeservice.utility;

public final class Constants {
    public final static String DEV_APP_URL = "http://localhost:3000";
    public final static String PRODUCTION_APP_URL = "https://crumbs-app.herokuapp.com";
    public final static String DEFAULT_TIMEZONE = "UTC";

    public final static String COOKIE_STRING = "refreshToken";
    public final static int REFRESH_TOKEN_EXPIRATION_TIME = 864_000_000; // 10 days
    public final static int ACCESS_TOKEN_EXPIRATION_TIME = 864_000_000; // 10 days
    public final static int VERIFICATION_TOKEN_EXPIRATION_TIME = 86_400_000; // 1 day

    private Constants() {}
}
