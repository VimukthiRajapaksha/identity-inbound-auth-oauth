package org.wso2.carbon.identity.oauth.rar.core;

import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetailContext;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;
import org.wso2.carbon.identity.oauth.rar.model.ValidationResult;

public class AuthorizationDetailProcessorImpl implements AuthorizationDetailProcessor {

    @Override
    public ValidationResult validate(AuthorizationDetailContext authorizationDetailContext) {
        AuthorizationDetails authorizationDetails = authorizationDetailContext.getAuthorizationDetails();

        if (authorizationDetails.getActions().contains("initiate")) {
            return new ValidationResult(true);
        } else {
            return new ValidationResult(false);
        }
    }

    @Override
    public String getType() {
        return "payment_initiation";
    }

}
