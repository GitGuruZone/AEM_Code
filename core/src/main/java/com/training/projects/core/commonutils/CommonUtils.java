package com.training.projects.core.commonutils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import java.util.HashMap;
import java.util.Map;

public final class CommonUtils {
    /**
     * constructor of CommonUtils class.
     */
    private CommonUtils() {
    }

    /**
     * this method return resolver.
     * @param resolverFactory
     * @param systemUser
     * @return {ResourceResolver}
     * @throws LoginException
     */
    public static ResourceResolver getResolver(
            final ResourceResolverFactory resolverFactory,
            final String systemUser)
            throws LoginException {
        Map<String, Object> map = new HashMap<>();
        map.put(ResourceResolverFactory.SUBSERVICE, systemUser);
        return resolverFactory.getServiceResourceResolver(map);
    }
}
