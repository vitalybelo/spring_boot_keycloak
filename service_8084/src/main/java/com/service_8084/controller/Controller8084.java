package com.service_8084.controller;

import com.service_8084.config.KeycloakOidcUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Для получения ролей и утверждений из идентификационного токена, контроллеры принимают на вход
 * совместимые (cast) классы аутентификации, которые можно использовать для чтения учётных данных
 * Principal - дает доступ к примитивному классу java.security, методы getName(), учётка как toString()
 * Authentication - класс Spring Security - возвращает principal + большой набор методов учётки пользователя
 * OAuth2AuthenticationToken - класс Spring Security Oauth2 - возвращает principal и учётку пользователя (аналог выше)
 * Также имеется дополнительный класс KeycloakOidcUserInfo - который возвращает роли в виде коллекции List
 * Констуктор класса KeycloakOidcUserInfo принимает любой из 3-х описанных выше параметров.
 */
@Controller
public class Controller8084 {

    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(Controller8084.class);

    @GetMapping(path = "/")
    public String index(Principal principal,
                        Authentication auth1,
                        OAuth2AuthenticationToken auth2,
                        Model model)
    {
        // ----------------------------------------------------------------------
        // Пример самостоятельного чтения ролей из principal
        // ----------------------------------------------------------------------
        OidcUser user = ((OidcUser) auth1.getPrincipal());
        if (user.hasClaim("realm_access")) {
            // проверяем есть у пользователя хотя бы одна роль
            String roles = user.getAttribute("realm_access").toString();
            if (roles.contains("Alex")) {
                // авторизация успешная - роль обнаружена
                logger.info(principal.getName() + " :: " + roles + " :: обнаружена роль Alex");
            } else {
                // авторизация провалена
                logger.info(principal.getName() + " :: " + roles);
            }
        } else {
            // пользователь без ролей, список ролей из claims пустой
            logger.info(principal.getName() + " :: роли не обнаружены");
        }

        // ----------------------------------------------------------------------
        // Пример чтения ролей из principal с помощью класса KeycloakOidcUserInfo
        // ----------------------------------------------------------------------
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(auth2);
        model.addAttribute("username", userInfo.getUser().getFullName());
        model.addAttribute("roles", userInfo.getRolesList());
        return "external";
    }

    /**
     * при использовании spring security oauth2 - сервис сюда не попадает,
     * запрос перехватывает SPRING SECURITY и перенаправляет на свою страницу front channel
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
        logger.info(authentication.getPrincipal().toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        model.addAttribute("username", userInfo.getUser().getFullName());
        return "customers1";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Authentication authentication, Model model)
    {
        logger.info(authentication.getPrincipal().toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        if (userInfo.getRolesList().contains("Grant") || userInfo.getRolesList().contains("User"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Authentication authentication, Model model)
    {
        logger.info(authentication.getPrincipal().toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(authentication);
        if (userInfo.getRolesList().contains("Delete"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers3";
        }
        return "denied";
    }

}
