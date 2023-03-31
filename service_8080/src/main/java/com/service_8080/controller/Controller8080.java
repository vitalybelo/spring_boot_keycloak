package com.service_8080.controller;

import com.auth.AuthenticationService;
import com.auth.KeycloakOidcUserInfo;
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
public class Controller8080 {

    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(Controller8080.class);

    @GetMapping(path = "/")
    public String index(Principal principal, Model model)
    {
        AuthenticationService service = new AuthenticationService();
        Authentication authentication = service.getUserAuthentication();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        OidcUser user = service.getUserOidc();
        // ----------------------------------------------------------------------
        // Пример самостоятельного чтения ролей из principal
        // ----------------------------------------------------------------------
        if (user.containsClaim("realm_access")) {
            // проверяем есть у пользователя хотя бы одна роль
            String roles = user.getClaimAsString("realm_access");
            if (roles.contains("Admin")) {
                // авторизация успешная - роль обнаружена
                logger.info(principal.getName() + " :: " + roles + " :: обнаружена роль Admin");
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
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        model.addAttribute("username", userInfo.getUser().getFullName());
        model.addAttribute("roles", userInfo.getRolesList());
        return "external";
    }

    /**
     * при использовании spring security oauth2 - сервис сюда не попадает,
     * запрос перехватывает SPRING SECURITY и перенаправляет на свою страницу front channel
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
        logger.info(principal.toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("роль 1") || userInfo.getRolesList().contains("роль 2"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers1";
        }
        return "denied";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Principal principal, Model model)
    {
        logger.info(principal.toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("Admin"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Principal principal, Model model)
    {
        logger.info(principal.toString());
        KeycloakOidcUserInfo userInfo = new KeycloakOidcUserInfo(principal);
        if (userInfo.getRolesList().contains("Boss"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", userInfo.getUser().getFullName());
            return "customers3";
        }
        return "denied";
    }

}
