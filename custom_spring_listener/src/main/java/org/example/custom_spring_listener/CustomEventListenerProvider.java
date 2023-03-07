package org.example.custom_spring_listener;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = Logger.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event)
    {
        log.info("-----------------------------------------------------------");
        log.infof("## NEW %s EVENT", event.getType());
        log.info("-----------------------------------------------------------");
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b)
    {
        log.info("-----------------------------------------------------------");
        log.infof("## NEW %s EVENT", adminEvent.getOperationType());
        log.info("-----------------------------------------------------------");
    }

    @Override
    public void close() {
    }

}