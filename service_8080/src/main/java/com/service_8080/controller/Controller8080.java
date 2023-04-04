package com.service_8080.controller;

import com.auth.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * Для авторизации, используйте класс AuthorizationService(), с набором методов возвращающих учётные
 * данные пользователя, а также осуществляющие проверку наличия одной или списка ролей, частично или целиком.
 * В классе имеется два метода возвращающих экземпляры security классов аутентификации Authentication и OidcUser.
 * На вход контроллеров поступает экземпляр класса Principal (java.security): методы getName(), учётка как toString()
 * Имеется дополнительный класс KeycloakOidcUserInfo - который возвращает роли в виде коллекции List
 * KeycloakOidcUserInfo инициализируется экземплярами классов: Authentication, OAuth2AuthenticationToken, Principal.
 */
@Controller
public class Controller8080 {

    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(Controller8080.class);

    @GetMapping(path = "/")
    public String index(Principal principal, Model model)
    {
        AuthorizationService user = new AuthorizationService();
        // ----------------------------------------------------------------------
        // Пример чтения учётки с помощью класса AuthenticationService()
        // ----------------------------------------------------------------------
        model.addAttribute("username", user.getFullName());
        model.addAttribute("phonenumber", user.getPhoneNumber());
        model.addAttribute("position", user.getPosition());
        model.addAttribute("roles", user.getRolesList());
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
    public String linkPage1(Model model)
    {
        AuthorizationService user = new AuthorizationService();
        logger.info(user.getAuthentication().getPrincipal().toString());
        if (user.containsRoles(new String[]{"Роль 1", "Роль 2"}))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", user.getFullName());
            return "customers1";
        }
        return "denied";
    }

    @GetMapping(path = "/customers2")
    public String linkPage2(Model model)
    {
        AuthorizationService user = new AuthorizationService();
        logger.info(user.getAuthentication().getPrincipal().toString());
        if (user.containsRole("Admin"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", user.getFullName());
            return "customers2";
        }
        return "denied";
    }

    @GetMapping(path = "/customers3")
    public String linkPage3(Model model)
    {
        AuthorizationService user = new AuthorizationService();
        logger.info(user.getAuthentication().getPrincipal().toString());
        if (user.containsRole("Boss"))
        {   // TODO разрешенные действия для этой роли

            model.addAttribute("username", user.getFullName());
            return "customers3";
        }
        return "denied";
    }

}
