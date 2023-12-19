package org.wso2.carbon.identity.oauth.rar.model;

import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;

import java.util.List;
import java.util.Map;

public class AuthorizationDetailContext {
    private OAuth2Parameters oAuth2Parameters;
    private List<AuthorizationDetails> authorizationDetails;
    private Map<String, Object> properties;

    public AuthorizationDetailContext() {
        // non parameterized constructor
    }

    public AuthorizationDetailContext(OAuth2Parameters oAuth2Parameters,
                                      List<AuthorizationDetails> authorizationDetails) {
        this.oAuth2Parameters = oAuth2Parameters;
        this.authorizationDetails = authorizationDetails;
    }

    public List<AuthorizationDetails> getAuthorizationDetails() {
        return authorizationDetails;
    }

    public void setAuthorizationDetails(List<AuthorizationDetails> authorizationDetails) {
        this.authorizationDetails = authorizationDetails;
    }

    public OAuth2Parameters getOAuth2Parameters() {
        return oAuth2Parameters;
    }

    public void setOAuth2Parameters(OAuth2Parameters oAuth2Parameters) {
        this.oAuth2Parameters = oAuth2Parameters;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

}
