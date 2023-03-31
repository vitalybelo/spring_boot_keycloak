package com.service_8084.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * метод oauth2Login() добавляет OAuth2LoginAuthenticationFilter в цепочку фильтров.
 * Этот фильтр перехватывает запросы и применяет необходимую логику для аутентификации OAuth 2.
 * Метод logout() настраивает логику выхода пользователя из Oidc и Keycloak по back channel
 * Метод oauth2ResourceServer проверит связанный токен JWT с нашим сервером Keycloak.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Метод .antMatchers("/**") - определяет uri которые будут защищены
     * этот uri является продолжением Valid redirect URIs - из настроек - Realms -> Client -> login-app
     * Метод .oauth2Login - конфигурирует вход в приложение с логином и паролем
     * Метод .logout - настраивает политику выхода из keycloak
     */
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .antMatchers("/**")
                .authenticated()
                .anyRequest()
                .permitAll();
        http.oauth2Login()
                .and()
                .logout()
                .addLogoutHandler(keycloakLogoutHandler)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");
        http.oauth2ResourceServer().jwt();
    }

}
