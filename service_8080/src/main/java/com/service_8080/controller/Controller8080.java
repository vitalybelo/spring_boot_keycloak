package com.service_8080.controller;

import com.service_8080.config.KeycloakTokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@org.springframework.stereotype.Controller
public class Controller8080 {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(path = "/")
    public String index(Principal principal, Model model) {
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", roles.getRolesArray());
        return "external";
    }

    @GetMapping("/logout") // не задействована
    public String logout() throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping(path = "/customers1")
    public String linkPage1(Principal principal, Model model)
    {
        System.out.println(principal);
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        if (roles.getRolesList().contains("USER")) {
            model.addAttribute("username", principal.getName());
            return "customers1";
        }
        return "denied";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        if (roles.getRolesList().contains("ADMIN")) {
            model.addAttribute("username", principal.getName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        if (roles.getRolesList().contains("BOSS")) {
            model.addAttribute("username", principal.getName());
            return "customers3";
        }
        return "denied";
    }

}
