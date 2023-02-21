package com.service_8080.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class KeycloakRolesParser {

    private final String principal;

    public KeycloakRolesParser(Principal principal) {
        this.principal = principal.toString();
    }

    public List<String> getRoles()
    {
        int i_end;
        String roles = "roles";
        String realm = "realm_access";
        int i_beg = principal.indexOf(realm);

        if (i_beg > 0) {
            i_beg += realm.length();
            i_beg = principal.indexOf("roles", i_beg);
            if (i_beg > 0) {
                i_beg += roles.length();
                i_beg = principal.indexOf('[', i_beg);
                if (i_beg > 0) {
                    i_end = principal.indexOf(']', i_beg);
                    if (i_end > 0) {
                        String str = principal
                                .substring(i_beg + 1, i_end)
                                .toUpperCase()
                                .replace("\"", "");
                        return List.of(str.split(","));
                    }
                }
            }
        }
        return new ArrayList<>();
    }

}
