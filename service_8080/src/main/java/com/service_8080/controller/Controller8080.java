package com.service_8080.controller;

import com.service_8080.config.KeycloakOidcUserInfo;
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
    public String index(Principal principal, Model model)
    {
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        model.addAttribute("username", userInfo.getUser().getFullName());
        model.addAttribute("roles", userInfo.getRolesList());
        return "external";
    }

    /**
     * при использовании spring security oauth2 - сервис сюда не попадает, запрос перехватывает SPRING
     */
    @GetMapping("/logout") // не задействована
    public String logout() throws Exception {
        request.logout();
        return "redirect:/";
    }

    @GetMapping("/custom_logout")
    public String custom_logout() throws Exception {
        request.logout();
        //return "custom_logout";
        return "redirect:/";
    }

    @GetMapping(path = "/customers1")
    public String linkPage1(Principal principal, Model model)
    {
        System.out.println(principal);
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("USER")) {
            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers1";
        }
        return "denied";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("ADMIN")) {
            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Principal principal, Model model)
    {
        System.out.println(principal.toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("BOSS")) {
            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers3";
        }
        return "denied";
    }

}
