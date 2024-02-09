package org.wso2.carbon.identity.oauth.rar.internal;

import org.wso2.carbon.identity.oauth.rar.core.AuthorizationDetailProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthorizationDetailProcessorFactory {
    private static AuthorizationDetailProcessorFactory singletonInstance;
    private final Map<String, AuthorizationDetailProcessor> supportedAuthorizationDetailTypes;

    private AuthorizationDetailProcessorFactory() {
        this.supportedAuthorizationDetailTypes = new HashMap<>();
    }

    public static synchronized AuthorizationDetailProcessorFactory getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new AuthorizationDetailProcessorFactory();
        }
        return singletonInstance;
    }

    public AuthorizationDetailProcessor getAuthorizationDetailProcessor(final String type) {
        //todo: handle if not available error
        return supportedAuthorizationDetailTypes.get(type);
    }

    public boolean isSupportedAuthorizationDetailType(final String type) {
        return supportedAuthorizationDetailTypes.containsKey(type);
    }

    public Set<String> getSupportedAuthorizationDetailTypes() {
        return supportedAuthorizationDetailTypes.keySet();
    }

    public void registerAuthorizationDetailProcessor(final String type,
                                                     final AuthorizationDetailProcessor authorizationDetailProcessor) {
        supportedAuthorizationDetailTypes.put(type, authorizationDetailProcessor);
    }

    public void registerAuthorizationDetailProcessor(final AuthorizationDetailProcessor authorizationDetailProcessor) {
        if (authorizationDetailProcessor != null) {
            this.registerAuthorizationDetailProcessor(authorizationDetailProcessor.getType(),
                    authorizationDetailProcessor);
        }
    }
}
