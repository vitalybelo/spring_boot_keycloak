package com.service_8084.controller;

import org.authorities.KeycloakOidcUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;

@Controller
public class Controller8084 {

    @Autowired
    private HttpServletRequest request;

    @GetMapping(path = "/")
    public String index(Authentication authentication, Model model)
    {
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        model.addAttribute("username", userInfo.getUser().getFullName());
        model.addAttribute("roles", userInfo.getRolesList());
        return "external";
    }

    /**
     * при использовании spring security oauth2 - сервис сюда не попадает, запрос перехватывает SPRING SECURITY
     */
    @GetMapping("/logout")
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
    public String linkPage1(Authentication authentication, Model model)
    {
        System.out.println(authentication.getPrincipal());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        model.addAttribute("username", userInfo.getUser().getFullName());
        return "customers1";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Authentication authentication, Model model)
    {
        System.out.println(authentication.getPrincipal());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        if (userInfo.getRolesList().contains("GRANT") || userInfo.getRolesList().contains("USER")) {
            // TODO разрешенные действия для этой роли
            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Authentication authentication, Model model)
    {
        System.out.println(authentication.getPrincipal());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        if (userInfo.getRolesList().contains("DELETE")) {
            // TODO разрешенные действия для этой роли
            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers3";
        }
        return "denied";
    }

}
