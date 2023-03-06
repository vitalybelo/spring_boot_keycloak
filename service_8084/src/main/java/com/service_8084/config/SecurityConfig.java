package com.service_8084.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * метод oauth2Login() добавляет OAuth2LoginAuthenticationFilter в цепочку фильтров. Этот фильтр перехватывает
 * запросы и применяет необходимую логику для аутентификации OAuth 2. Метод oauth2ResourceServer проверит связанный
 * токен JWT с нашим сервером Keycloak. Мы настраиваем доступ на основе полномочий и ролей в методе configure().
 * Эти ограничения гарантируют, что каждый запрос к /customers/* будет авторизован только в том случае, если
 * запрашивающий его является аутентифицированным пользователем с ролью USER.
 */

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/*").authenticated()
                .anyRequest().permitAll();
        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/");
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}
