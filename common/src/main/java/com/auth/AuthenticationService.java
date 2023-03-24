package com.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Содержит набор методов возвращающих учётные данные из утверждений id токена аутентификации
 */
public class AuthenticationService {

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return - экземпляр объекта класса аутентификации Authentication
     */
    public Authentication getUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return - экземпляр объекта класса аутентификации Oauth2 Open Id Connect :: OidcUser с обширным набором методов
     */
    public OidcUser getUserOidc() {
        return (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getFullName() {
        OidcUser user = (OidcUser) SecurityContextHolder.getContext().getAuthentication();


    }



}
