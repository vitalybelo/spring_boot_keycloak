package com.auth;

import net.minidev.json.JSONArray;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Содержит набор методов возвращающих учётные данные из утверждений id токена аутентификации
 */
public class AuthorizationService {

    private OidcUser user;
    private final List<String> rolesList = new ArrayList<>();
    private static final String RETURN_UNIT = null;

    /**
     * Конструктор считывает oauth2 учётку пользователя из контекста security в экземпляр класса OidcUser.
     * После этой инициализации, в конструкторе вызывается метод, который считывает список ролей из утверждений (claims)
     * id токена. Получить список - вызов метода getRolesList()
     */
    public AuthorizationService() {
        this.user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        extractRolesList();
    }

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return - экземпляр класса аутентификации Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return - экземпляр класса аутентификации Oauth2 Open Id Connect :: OidcUser с обширным набором методов
     */
    public OidcUser getOidcUser() {
        return user;
    }

    /**
     * Возвращает имя пользователя = логин (login name)
     * @return - строка регистрационного имени пользователя
     */
    public String getUserName() {
        return user.getPreferredUsername();
    }

    /**
     * Возвращает полное имя пользователя из профиля учётки keycloak = first name + second name
     * @return - строка полного имени пользователя
     */
    public String getFullName() {
        return user.getFullName();
    }

    /**
     * Возвращает список ролей назначенных для пользователя. Список ролей формируется при создании
     * экземпляра класса AuthenticationService, далее не обновляется. Дополнительное обновления списка
     * не требуется, так он единственный для пользователя. В случае необходимости, используйте метод обновления
     * списка refreshOidcUser(), при котором из контекста security обновляется principal пользователя
     * @return - List коллекция - список ролей, или пустой
     */
    public List<String> getRolesList() {
        return rolesList;
    }

    /**
     * Возвращает электронную почту пользователя из учётки если она есть, или null
     * @return - строковая переменная с почтой пользователя, или null если не задана
     */
    public String getUserEmail() {
        return user.getEmail();
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает номер телефона из учётки, если он задан как аттрибут пользователя
     * @return - строковая переменная с номером телефона, или null если не задана
     */
    public String getPhoneNumber() {
        return (user.containsClaim("phone") ? user.getClaimAsString("phone") : RETURN_UNIT);
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает название подразделения из учётки если оно задано как аттрибут пользователя
     * @return - строковая переменная подразделения, или null если не задана
     */
    public String getDepartment() {
        return (user.containsClaim("department") ? user.getClaimAsString("department") : RETURN_UNIT);
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает название должности из учётки если оно заданно как аттрибут пользователя
     * @return - строковая переменная должности, или null если не задана
     */
    public String getPosition() {
        return (user.containsClaim("position") ? user.getClaimAsString("position") : RETURN_UNIT);
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает признак - что баннер безопасности уже показывался пользователю при входе,
     * параметр должен быть задан как аттрибут пользователя в настройках keycloak
     * @return - true или false, или null если аттрибут не задан для пользователя
     */
    public Boolean isSecurityBannerViewed() {
        return (user.containsClaim("ban_viewed") ? user.getClaimAsBoolean("ban_viewed") : null);
    }

    public String getIpAddress() {
        return (user.containsClaim("ip_address") ? user.getClaimAsString("ip_address") : RETURN_UNIT);
    }

    /**
     * Обновляет oauth2 информацию пользователя из контекста security в экземпляр класса OidcUser.
     * Автоматически вызывается метод считывающий список ролей из утверждений (claims) id токена.
     * Получить список можно с помощью метода getRolesList()
     */
    public void refreshOidcUser() {
        user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        extractRolesList();
    }

    /**
     * Проверяет в учётке пользователя наличие запрашиваемой роли.
     * @param role строковое значение роли, которое проверяется на наличие в учетных данных пользователя
     * @return true - если роль присвоена этому пользователю, false - такой роли у пользователя нет
     */
    public boolean containsRole(CharSequence role)
    {
        if (user.containsClaim("realm_access")) {
            String roles = user.getClaimAsString("realm_access");
            return roles.contains(role);
        }
        return false;
    }

    /**
     * Проверяет в учётке пользователя наличие хотя-бы одной роли из запрашиваемого списка ролей
     * @param roles строковый массив содержащий список запрашиваемых у метода ролей на проверку
     * @return true - если найдена хотя бы одна роль из списка, false - ни одна роль не найдена
     */
    public boolean containsRoles(String[] roles) {
        if (user.containsClaim("realm_access")) {
            String roleString = user.getClaimAsString("realm_access");
            for (String role : roles) {
                if (roleString.contains(role)) return true;
            }
        }
        return false;
    }

    /**
     * Проверяет в учётке пользователя наличие всех запрашиваемых в списке параметров ролей
     * @param roles строковый массив, содержащий список запрашиваемых у метода ролей на проверку
     * @return true - только если найдены все роли из запрашиваемого списка, false - если хотя бы одна роль не найдена
     */
    public boolean containsAllRoles(String[] roles) {
        if (user.containsClaim("realm_access")) {
            String roleString = user.getClaimAsString("realm_access");
            for (String role : roles) {
                if (!roleString.contains(role)) return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Инициализирует свойство класса AuthenticationService - список ролей пользователя.
     * Список ролей доступен из метода List < String > getRolesList()
     */
    private void extractRolesList()
    {
        rolesList.clear();
        if (user.containsClaim("realm_access")) {
            Map<String, Object> rolesClaimMap = new HashMap<>(user.getClaimAsMap("realm_access"));
            JSONArray jsonArray = new JSONArray();
            if (rolesClaimMap.containsKey("roles")) {
                if (rolesClaimMap.get("roles") instanceof List) {
                    jsonArray.addAll((List) rolesClaimMap.get("roles"));
                    for (Object o : jsonArray)
                        rolesList.add(o.toString());
                }
            }
        }
    }

}