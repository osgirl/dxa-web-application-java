package com.sdl.webapp.dd4t.entityfactory;

import com.sdl.webapp.common.ContentProviderException;
import com.sdl.webapp.common.model.Entity;
import com.sdl.webapp.common.model.entity.YouTubeVideo;
import org.dd4t.contentmodel.ComponentPresentation;
import org.dd4t.contentmodel.Field;
import org.dd4t.contentmodel.GenericComponent;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.sdl.webapp.dd4t.entityfactory.FieldUtil.getStringValue;

@Component
public class YouTubeVideoFactory extends MediaItemFactory {

    private static final Class<?>[] SUPPORTED_ENTITY_TYPES = { YouTubeVideo.class };

    @Override
    public Class<?>[] supportedEntityTypes() {
        return SUPPORTED_ENTITY_TYPES;
    }

    @Override
    public Entity createEntity(ComponentPresentation componentPresentation, Class<?> entityType)
            throws ContentProviderException {
        final YouTubeVideo youTubeVideo = new YouTubeVideo();

        final GenericComponent component = componentPresentation.getComponent();
        final Map<String, Field> componentMetadata = component.getMetadata();

        youTubeVideo.setHeadline(getStringValue(componentMetadata, "headline"));
        youTubeVideo.setYouTubeId(getStringValue(componentMetadata, "youTubeId"));

        return createBaseEntity(componentPresentation, youTubeVideo);
    }
}