package org.wso2.carbon.identity.oauth.rar.core;

import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetailContext;
import org.wso2.carbon.identity.oauth.rar.model.ValidationResult;

public interface AuthorizationDetailProcessor {
    ValidationResult validate(AuthorizationDetailContext authorizationDetailContext);
}
