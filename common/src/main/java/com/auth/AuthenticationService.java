package com.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationService {

    public Authentication getUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
