package com.sdl.tridion.referenceimpl.dd4t.entityfactory;

import com.sdl.tridion.referenceimpl.common.ContentProviderException;
import com.sdl.tridion.referenceimpl.common.model.Entity;
import org.dd4t.contentmodel.ComponentPresentation;

/**
 * Entity factory.
 */
public interface EntityFactory {

    /**
     * Returns the entity types that this factory can create.
     *
     * @return The entity types that this factory can create.
     */
    Class<?>[] supportedEntityTypes();

    /**
     * Creates an entity of the specified type from a component presentation.
     *
     * @param componentPresentation The component presentation.
     * @param entityType The type of the entity to create.
     * @return The new entity.
     * @throws ContentProviderException If an error occurs and the entity cannot be created.
     */
    Entity createEntity(ComponentPresentation componentPresentation, Class<?> entityType) throws ContentProviderException;
}