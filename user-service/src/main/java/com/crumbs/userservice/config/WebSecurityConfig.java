package com.crumbs.userservice.config;

import com.crumbs.userservice.jwt.JwtAuthenticationFilter;
import com.crumbs.userservice.jwt.JwtConfigAndUtil;
import com.crumbs.userservice.jwt.JwtTokenVerifyFilter;
import com.crumbs.userservice.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String[] PUBLIC_ROUTES = {
            "/account/login",
            "/account/register",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html**",
            "/webjars/**"
    };

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtConfigAndUtil jwtConfigAndUtil;

    @Autowired
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtConfigAndUtil jwtConfigAndUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtConfigAndUtil = jwtConfigAndUtil;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfigAndUtil))
                .addFilterAfter(new JwtTokenVerifyFilter(customUserDetailsService, jwtConfigAndUtil), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(PUBLIC_ROUTES).permitAll().anyRequest()
                .authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/images/**", "/static/**", "/css/**", "/js/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
