package org.wso2.carbon.identity.oauth.rar.model;

import org.wso2.carbon.identity.oauth2.model.OAuth2Parameters;

import java.io.Serializable;

public class AuthorizationDetailContext implements Serializable {
    private static final long serialVersionUID = 4661476181077073508L;
    private OAuth2Parameters oAuth2Parameters;
    private AuthorizationDetails authorizationDetails;

    public AuthorizationDetailContext() {
        // non parameterized constructor
    }

    public AuthorizationDetailContext(OAuth2Parameters oAuth2Parameters,
                                      AuthorizationDetails authorizationDetails) {
        this.oAuth2Parameters = oAuth2Parameters;
        this.authorizationDetails = authorizationDetails;
    }

    public AuthorizationDetails getAuthorizationDetails() {
        return authorizationDetails;
    }

    public void setAuthorizationDetails(AuthorizationDetails authorizationDetails) {
        this.authorizationDetails = authorizationDetails;
    }

    public OAuth2Parameters getOAuth2Parameters() {
        return oAuth2Parameters;
    }

    public void setOAuth2Parameters(OAuth2Parameters oAuth2Parameters) {
        this.oAuth2Parameters = oAuth2Parameters;
    }

}
