package com.service_8084.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Метод возвращает список ролей для пользователя (определенного в Principal (java.security))
 * Для корректной работы метода необходимо в настройках сервера разрешить добавление ролей в идентификационный токен
 * Для этого выберите нужный вам REALM и далее:
 * Основное меню -> Client scopes -> roles -> Вкладка Settings -> Include in token scope -> ON
 * Здесь же: вкладка Scope -> добавляем в него все роли которые вы будете учитывать при работе
 * (если ничего не выбрано, по умолчанию будут переданы все роли включая default и так далее - много лишнего)
 */
public class KeycloakRolesParser {

    private final String principal;

    public KeycloakRolesParser(Principal principal) {
        this.principal = principal.toString();
    }

    public List<String> getRoles()
    {
        int i_end;
        String roles = "roles";
        String realm = "realm_access";
        int i_beg = principal.indexOf(realm);

        if (i_beg > 0) {
            i_beg += realm.length();
            i_beg = principal.indexOf("roles", i_beg);
            if (i_beg > 0) {
                i_beg += roles.length();
                i_beg = principal.indexOf('[', i_beg);
                if (i_beg > 0) {
                    i_end = principal.indexOf(']', i_beg);
                    if (i_end > 0) {
                        String str = principal
                                .substring(i_beg + 1, i_end)
                                .toUpperCase()
                                .replace("\"", "");
                        return List.of(str.split(","));
                    }
                }
            }
        }
        return new ArrayList<>();
    }

}
