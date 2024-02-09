package org.wso2.carbon.identity.oauth.rar.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.oauth.rar.core.AuthorizationDetailProcessor;
import org.wso2.carbon.identity.oauth.rar.handler.RARAccessTokenResponseHandler;
import org.wso2.carbon.identity.oauth2.token.handlers.response.AccessTokenResponseHandler;

/**
 *
 */
@Component(name = "org.wso2.carbon.identity.oauth.rar.internal.RARServiceComponent", immediate = true)
public class RARServiceComponent {
    private static final Log LOG = LogFactory.getLog(RARServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        AccessTokenResponseHandler accessTokenResponseHandler = new RARAccessTokenResponseHandler();

        context.getBundleContext().registerService(AccessTokenResponseHandler.class.getName(),
                accessTokenResponseHandler, null);
        LOG.debug("RAR component is activated successfully");
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        LOG.debug("RAR component is deactivated successfully");
    }

    @Reference(
            name = "org.wso2.carbon.identity.oauth.rar.AuthorizationDetailProcessor",
            service = AuthorizationDetailProcessor.class,
            cardinality = ReferenceCardinality.MULTIPLE,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetAuthorizationDetailProcessor"
    )
    protected void setAuthorizationDetailProcessor(AuthorizationDetailProcessor authorizationDetailProcessor) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting the AuthorizationDetailProcessor: " + authorizationDetailProcessor.getClass().getName());
        }
        AuthorizationDetailProcessorFactory.getInstance()
                .registerAuthorizationDetailProcessor(authorizationDetailProcessor);
    }

    protected void unsetAuthorizationDetailProcessor(AuthorizationDetailProcessor authorizationDetailProcessor) {

        if (authorizationDetailProcessor == null) {
            LOG.warn("Null AuthorizationDetailProcessor received, hence not un-registering");
            return;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Un-setting authorizationDetailProcessor :" + authorizationDetailProcessor.getClass().getName());
        }

        AuthorizationDetailProcessorFactory.getInstance().registerAuthorizationDetailProcessor(null);
    }

}
