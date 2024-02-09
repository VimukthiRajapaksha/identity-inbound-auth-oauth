package org.wso2.carbon.identity.oauth.rar.cache;

import org.wso2.carbon.identity.oauth.cache.CacheEntry;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;

import java.util.List;

public class AuthorizationDetailCacheEntry extends CacheEntry {
    private static final long serialVersionUID = -2675584899689152384L;

    private List<AuthorizationDetails> requestedAuthorizationDetails;
    private List<AuthorizationDetails> consentedAuthorizationDetails;

    public List<AuthorizationDetails> getRequestedAuthorizationDetails() {
        return requestedAuthorizationDetails;
    }

    public void setRequestedAuthorizationDetails(List<AuthorizationDetails> requestedAuthorizationDetails) {
        this.requestedAuthorizationDetails = requestedAuthorizationDetails;
    }

    public List<AuthorizationDetails> getConsentedAuthorizationDetails() {
        return consentedAuthorizationDetails;
    }

    public void setConsentedAuthorizationDetails(List<AuthorizationDetails> consentedAuthorizationDetails) {
        this.consentedAuthorizationDetails = consentedAuthorizationDetails;
    }
}
