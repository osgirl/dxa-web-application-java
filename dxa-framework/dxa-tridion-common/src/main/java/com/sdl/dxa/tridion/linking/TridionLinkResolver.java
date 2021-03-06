package com.sdl.dxa.tridion.linking;

import com.sdl.web.api.linking.BinaryLinkImpl;
import com.sdl.web.api.linking.ComponentLinkImpl;
import com.sdl.web.api.linking.PageLinkImpl;
import com.sdl.webapp.tridion.linking.AbstractLinkResolver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * Tridion link resolver.
 *
 * @deprecated since PCA implementation added which supports mashup scenario.
 */
@Component
@Profile("cil.providers.active")
@Deprecated
public class TridionLinkResolver extends AbstractLinkResolver {

    @Override
    protected Function<ResolvingData, Optional<String>> _componentResolver() {
        return resolvingData -> Optional.ofNullable(
                new ComponentLinkImpl(resolvingData.getPublicationId()).getLink(resolvingData.getItemId()).getURL());
    }

    @Override
    protected Function<ResolvingData, Optional<String>> _pageResolver() {
        return resolvingData -> Optional.ofNullable(
                new PageLinkImpl(resolvingData.getPublicationId()).getLink(resolvingData.getItemId()).getURL());
    }

    @Override
    protected Function<ResolvingData, Optional<String>> _binaryResolver() {
        return resolvingData -> {
            String uri = resolvingData.getUri();

            String componentURI = uri.startsWith("tcm:") ? uri : ("tcm:" + uri);
            return Optional.ofNullable(
                    new BinaryLinkImpl(resolvingData.getPublicationId())
                            .getLink(componentURI, null, null, null, false)
                            .getURL());
        };
    }
}
