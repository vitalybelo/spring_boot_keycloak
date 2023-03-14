package com.service_8080.config;

import net.minidev.json.JSONArray;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.security.Principal;
import java.util.*;

/**
 * Метод возвращает список ролей для пользователя (определенного в Principal (java.security))
 * Для корректной работы метода необходимо в настройках сервера разрешить добавление ролей в идентификационный токен
 * Для этого выберите нужный вам REALM и далее:
 * Основное меню -> Client scopes -> roles -> Вкладка Settings -> Include in token scope -> ON
 * Здесь же: вкладка Scope -> добавляем в него все роли которые вы будете учитывать при работе (не обязательно)
 * (если ничего не выбрано в scope описаный выше, по умолчанию будут переданы все роли включая 3 роли default)
 */
public class KeycloakOidcUserInfo {

    private final OidcUser user;
    private List<String> rolesList = new ArrayList<>();

    public KeycloakOidcUserInfo(Authentication auth) {
        this.user = (OidcUser) auth.getPrincipal();
        extractRolesList();
        // TODO - включить ,если парсинг json не работает или недоступен :: import net.minidev.json.JSONArray;
        // extractRolesLegacyParser();
    }

    public KeycloakOidcUserInfo(Principal principal) {
        Authentication auth = (Authentication) principal; // это нужно
        this.user = (OidcUser) auth.getPrincipal();
        extractRolesList();
        // TODO - включить ,если парсинг json не работает или недоступен :: import net.minidev.json.JSONArray;
        // extractRolesLegacyParser();
    }

    /**
     * @return - список ролей для пользователя
     */
    public List<String> getRolesList() {
        return rolesList;
    }

    /**
     * @return - указатель на айтентификационный класс OidcUser - вся инофрмация о текущем пользователе
     * Далее, используя этот матод, вам доступно большое количество методов класса OidcUser для получения данных
     */
    public OidcUser getUser() {
        return user;
    }

    /**
     * Инициализирует свойство - список ролей - для пользователя переданного в конструктор класса
     * Метод извлекает роли из principal утверждений claims = "realm_access", из json массива "roles"
     */
    public void extractRolesList()
    {
        rolesList.clear();
        if (user.hasClaim("realm_access")) {
            Map<String, Object> rolesClaimMap = new HashMap<>(user.getClaimAsMap("realm_access"));
            JSONArray jsonArray = new JSONArray();
            if (rolesClaimMap.containsKey("roles")) {
                if (rolesClaimMap.get("roles") instanceof List) {
                    jsonArray.addAll((List) rolesClaimMap.get("roles"));
                    for (Object o : jsonArray)
                        rolesList.add(o.toString()); //.toUpperCase());
                }
            }
        }
    }

    /**
     * Инициализирует свойство - список ролей - для пользователя переданного в конструктор класса
     * Метод переводит principal в строку и парсинт значения ролей из строки
     */
    public void extractRolesLegacyParser()
    {
        int i_end;
        String roles = "roles";
        String realm = "realm_access";
        String principal = user.toString();
        String[] rolesArray = new String[0];
        int i_beg = principal.indexOf(realm);

        if (i_beg > 0) {
            i_beg += realm.length();
            i_beg = principal.indexOf("roles", i_beg);
            if (i_beg > 0) {
                i_beg += roles.length();
                i_beg = principal.indexOf('[', i_beg);
                if (i_beg > 0) {
                    i_end = principal.indexOf(']', i_beg);
                    if (i_end > 0 && i_beg + 1 < i_end && i_end < principal.length()) {
                        rolesArray = principal
                                .substring(i_beg + 1, i_end)
                                .toUpperCase()
                                .replace("\"", "")
                                .split(",");
                    }
                }
            }
        }
        rolesList = Arrays.asList(rolesArray);
    }

}
