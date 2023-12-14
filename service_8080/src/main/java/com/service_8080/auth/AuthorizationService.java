package com.service_8080.auth;

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

    private static final String ATTRIBUTE_PHONE = "phone";
    private static final String ATTRIBUTE_DEPARTMENT = "department";
    private static final String ATTRIBUTE_POSITION = "position";
    private static final String ATTRIBUTE_MIDDLE_NAME = "middle_name";
    private static final String ATTRIBUTE_FAMILY_NAME = "family_name";
    private static final String ATTRIBUTE_BANNER = "banner_viewed";
    private static final String ATTRIBUTE_ADDRESS = "ip_address";
    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES_CLAIMS = "roles";
    private static final String STRING_EMPTY = ""; // =null

    private OidcUser user;
    private final List<String> rolesList = new ArrayList<>();

    /**
     * Конструктор считывает oauth2 учётку пользователя из контекста security в экземпляр класса OidcUser.
     * После этой инициализации, в конструкторе вызывается метод, который считывает список ролей из утверждений (claims)
     * id токена. Получить список можно вызовом метода getRolesList()
     */
    public AuthorizationService() {
        this.user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        extractRolesList();
    }

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return экземпляр класса аутентификации Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Возвращает информацию о пользователе: id токен, утверждения, стандартную учётку, кастомные аттрибуты.
     * @return экземпляр класса аутентификации Oauth2 Open Id Connect :: OidcUser с обширным набором методов
     */
    public OidcUser getOidcUser() {
        return user;
    }

    /**
     * Возвращает имя пользователя = логин (login name)
     * @return строка регистрационного имени пользователя, или пустая строка если поле не было задано
     */
    public String getUserName() {
        if (user.getPreferredUsername() != null) {
            return user.getPreferredUsername();
        }
        return STRING_EMPTY;
    }

    /**
     * Возвращает фамилию пользователя из утверждений id токена
     * @return строка с фамилией пользователя, или пустая строка если поле не было задано
     */
    public String getFamilyName() {
        if (user.containsClaim(ATTRIBUTE_FAMILY_NAME)) {
            String familyName = user.getClaimAsString(ATTRIBUTE_FAMILY_NAME);
            if (familyName != null) return familyName;
        }
        return STRING_EMPTY;
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает параметр "отчество" из учётки, если он задан как аттрибут пользователя
     * @return строковая переменная с "отчеством" пользователя, или пустая строка если поле не было задано
     */
    public String getMiddleName() {
        if (user.containsClaim(ATTRIBUTE_MIDDLE_NAME)) {
            String middleName = user.getClaimAsString(ATTRIBUTE_MIDDLE_NAME);
            if (middleName != null) return middleName;
        }
        return STRING_EMPTY;
    }

    /**
     * Возвращает полное имя пользователя из профиля учётки keycloak = first name + second name
     * @return строка полного имени пользователя, или username строка в случае если первое имя = null
     */
    public String getFullName() {
        String givenName = user.getGivenName();
        if (givenName != null) {
            return givenName + " " + getMiddleName() + " " + getFamilyName();
        }
        return getUserName();
    }

    /**
     * Возвращает список ролей назначенных для пользователя. Список ролей формируется при создании
     * экземпляра класса AuthenticationService, далее не обновляется. Дополнительное обновления списка
     * не требуется, так он единственный для пользователя. В случае необходимости, используйте метод обновления
     * списка refreshOidcUser(), при котором из контекста security обновляется principal пользователя
     * @return List коллекция - список ролей, или пустой список если пользователю не назначены роли
     */
    public List<String> getRolesList() {
        return rolesList;
    }

    /**
     * Возвращает электронную почту пользователя из учётки если она есть, или null
     * @return строковая переменная с почтой пользователя, или пустая строка если поле не было задано
     */
    public String getUserEmail() {
        if (user.getEmail() != null) {
            return user.getEmail();
        }
        return STRING_EMPTY;
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает номер телефона из учётки, если он задан как аттрибут пользователя
     * @return строковая переменная с номером телефона, или пустая строка если поле не было задано
     */
    public String getPhoneNumber() {
        if (user.containsClaim(ATTRIBUTE_PHONE)) {
            String phoneNumber = user.getClaimAsString(ATTRIBUTE_PHONE);
            if (phoneNumber != null) return phoneNumber;
        }
        return STRING_EMPTY;
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает название подразделения из учётки если оно задано как аттрибут пользователя
     * @return строковая переменная подразделения, или пустая строка если поле не было задано
     */
    public String getDepartment() {
        if (user.containsClaim(ATTRIBUTE_DEPARTMENT)) {
            String department = user.getClaimAsString(ATTRIBUTE_DEPARTMENT);
            if (department != null) return department;
        }
        return STRING_EMPTY;
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает название должности из учётки если оно заданно как аттрибут пользователя
     * @return строковая переменная должности, или пустая строка если поле не было задано
     */
    public String getPosition() {
        if (user.containsClaim(ATTRIBUTE_POSITION)) {
            String position = user.getClaimAsString(ATTRIBUTE_POSITION);
            if (position != null) return position;
        }
        return STRING_EMPTY;
    }

    /**
     * Метод извлекает кастомный аттрибут пользователя из списка утверждений id токена
     * Возвращает признак - что баннер безопасности уже показывался пользователю при входе,
     * параметр должен быть задан как аттрибут пользователя в настройках keycloak
     * @return true или false, или null если аттрибут не задан для пользователя
     */
    public Boolean isSecurityBannerViewed() {
        return (user.containsClaim(ATTRIBUTE_BANNER) ? user.getClaimAsBoolean(ATTRIBUTE_BANNER) : null);
    }

    /**
     * Возвращает IP адрес, заданный для пользователя статически в учётной записи
     * @return строка с ip адресом, или пустая строка если поле не было задано
     */
    public String getIpAddress() {
        if (user.containsClaim(ATTRIBUTE_ADDRESS)) {
            String ipAddress = user.getClaimAsString(ATTRIBUTE_ADDRESS);
            if (ipAddress != null) return  ipAddress;
        }
        return STRING_EMPTY;
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
     * @return true если роль присвоена этому пользователю, false если такой роли у пользователя нет
     */
    public boolean containsRole(CharSequence role)
    {
        if (user.containsClaim(REALM_ACCESS)) {
            String roles = user.getClaimAsString(REALM_ACCESS);
            return roles.contains(role);
        }
        return false;
    }

    /**
     * Проверяет в учётке пользователя наличие хотя-бы одной роли из запрашиваемого списка ролей
     * @param roles строковый массив содержащий список запрашиваемых у метода ролей на проверку
     * @return true если найдена хотя бы одна роль из списка, false если ни одна роль не найдена
     */
    public boolean containsRoles(String[] roles) {
        if (user.containsClaim(REALM_ACCESS)) {
            String roleString = user.getClaimAsString(REALM_ACCESS);
            for (String role : roles) {
                if (roleString.contains(role)) return true;
            }
        }
        return false;
    }

    /**
     * Проверяет в учётке пользователя наличие всех запрашиваемых в списке параметров ролей
     * @param roles строковый массив, содержащий список запрашиваемых у метода ролей на проверку
     * @return true только если найдены все роли из запрашиваемого списка, false если хотя бы одна роль не найдена
     */
    public boolean containsAllRoles(String[] roles) {
        if (user.containsClaim(REALM_ACCESS)) {
            String roleString = user.getClaimAsString(REALM_ACCESS);
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
        if (user.containsClaim(REALM_ACCESS)) {
            Map<String, Object> rolesClaimMap = new HashMap<>(user.getClaimAsMap(REALM_ACCESS));
            JSONArray jsonArray = new JSONArray();
            if (rolesClaimMap.containsKey(ROLES_CLAIMS)) {
                if (rolesClaimMap.get(ROLES_CLAIMS) instanceof List) {
                    jsonArray.addAll((List) rolesClaimMap.get(ROLES_CLAIMS));
                    for (Object o : jsonArray)
                        rolesList.add(o.toString());
                }
            }
        }
    }

}