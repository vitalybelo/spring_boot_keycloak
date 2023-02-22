package com.service_8084.controller;

import com.service_8084.config.KeycloakTokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class Controller8084 {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(path = "/")
    public String index(Principal principal, Model model) {
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        model.addAttribute("username", principal.getName());
        model.addAttribute("roles", roles.getRolesArray());
        return "external";
    }

    @GetMapping("/logout") // не задействована пока
    public String logout() throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping(path = "/customers1")
    public String linkPage1(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        model.addAttribute("username", principal.getName());
        return "customers1";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        KeycloakTokenParser roles = new KeycloakTokenParser(principal);
        if (roles.getRolesList().contains("GRANT")) {
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
        if (roles.getRolesList().contains("DELETE")) {
            model.addAttribute("username", principal.getName());
            return "customers3";
        }
        return "denied";
    }

}
