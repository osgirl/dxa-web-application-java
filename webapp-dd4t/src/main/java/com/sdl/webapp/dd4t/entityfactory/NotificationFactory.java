package com.sdl.webapp.dd4t.entityfactory;

import com.sdl.webapp.common.ContentProviderException;
import com.sdl.webapp.common.model.Entity;
import com.sdl.webapp.common.model.entity.Notification;
import org.dd4t.contentmodel.ComponentPresentation;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory implements EntityFactory {

    private static final Class<?>[] SUPPORTED_ENTITY_TYPES = { Notification.class };

    @Override
    public Class<?>[] supportedEntityTypes() {
        return SUPPORTED_ENTITY_TYPES;
    }

    @Override
    public Entity createEntity(ComponentPresentation componentPresentation, Class<?> entityType) throws ContentProviderException {
        // TODO: Implement this
        return new Notification();
    }
}