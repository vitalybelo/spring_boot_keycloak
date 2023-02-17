package com.service_8080.controller;

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
    public String index() {
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
        model.addAttribute("username", principal.getName());
        return "customers2";
    }


}
