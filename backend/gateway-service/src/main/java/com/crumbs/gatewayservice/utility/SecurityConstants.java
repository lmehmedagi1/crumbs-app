package com.crumbs.gatewayservice.utility;

public final class SecurityConstants {
    public static final String SECRET = "e94a08b7f23735dcc5a14af50ccb5ba1617d0c3f299c0b133cc1c7a3c2474342a54cb2179207868bcadb1ce812f9fc757bfcc243d153fe9740a345a1e167b6f0";
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 10; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String[] PUBLIC_ROUTES = {
            "/notification-service",
            "/user-service/auth/**",
            "/user-service/account/**",
            "/recipe-service/diets",
            "/recipe-service/diets/public",
            "/recipe-service/recipes/user",
            "/recipe-service/recipes/top-monthly",
            "/recipe-service/recipes/recipe",
            "/recipe-service/recipes/top-daily",
            "/recipe-service/recipes/type",
            "/recipe-service/categories/type",
            "/recipe-service/ingredients/type",
            "/recipe-service/recipes/**",
            "/recipe-service/diets/user",
            "/review-service/reviews/topMonthly",
            "/review-service/reviews/topDaily",
            "/review-service/reviews/comments",
            "/review-service/reviews/rating",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html**",
            "/webjars/**",
            "/ws/**",
            "/user-service/swagger-ui/",
            "/user-service/swagger-ui/**",
            "/user-service/v2/api-docs",
            "/user-service/swagger-resources/**",
            "/user-service/swagger-ui.html**",
            "/user-service/webjars/**",
            "/user-service/ws/**"
    };
    private SecurityConstants() {}
}
