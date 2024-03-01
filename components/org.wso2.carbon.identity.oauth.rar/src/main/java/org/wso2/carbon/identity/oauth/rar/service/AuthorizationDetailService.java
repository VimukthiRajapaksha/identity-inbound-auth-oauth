package org.wso2.carbon.identity.oauth.rar.service;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.oauth.dto.OAuthConsumerAppDTO;
import org.wso2.carbon.identity.oauth.rar.cache.AuthorizationDetailCache;
import org.wso2.carbon.identity.oauth.rar.cache.AuthorizationDetailCacheEntry;
import org.wso2.carbon.identity.oauth.rar.cache.AuthorizationDetailCacheKey;
import org.wso2.carbon.identity.oauth.rar.dao.AuthorizationDetailDAO;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class AuthorizationDetailService {

    private static AuthorizationDetailService instance;
    private final AuthorizationDetailDAO authorizationDetailDAO;
    private final Gson gson;
    private static final Log LOG = LogFactory.getLog(AuthorizationDetailService.class);

    private AuthorizationDetailService() {
        this.authorizationDetailDAO = new AuthorizationDetailDAO();
        this.gson = new Gson();
    }

    public static synchronized AuthorizationDetailService getInstance() {
        if (instance == null) {
            instance = new AuthorizationDetailService();
        }
        return instance;
    }

    public String[] getAuthorizationDetailsTypesByConsumerKey(final String consumerKey) {
        try {
            return this.authorizationDetailDAO
                    .getAuthorizationDetailsTypesByConsumerKey(consumerKey, IdentityTenantUtil.getLoginTenantId());
        } catch (SQLException e) {
            LOG.error("Error retrieving authorization details types. Caused by, ", e);
        }
        return new String[0];
    }

    public void updateAuthorizationDetailsBindings(final OAuthConsumerAppDTO appToUpdate) {
        final String consumerKey = appToUpdate.getOauthConsumerKey();
        final String[] dbAuthorizationDetailsTypes = this.getAuthorizationDetailsTypesByConsumerKey(consumerKey);
        final String[] newAuthorizationDetailsTypes = appToUpdate.getAuthorizationDetailsTypes();

        unbindAuthorizationDetailsTypesFromApp(consumerKey,
                extractDeselectedAuthorizationDetailsTypes(dbAuthorizationDetailsTypes, newAuthorizationDetailsTypes));
        bindAuthorizationDetailsTypesToApp(consumerKey,
                extractSelectedAuthorizationDetailsTypes(dbAuthorizationDetailsTypes, newAuthorizationDetailsTypes));
    }

    private void bindAuthorizationDetailsTypesToApp(final String consumerAppId,
                                                    final String[] selectedAuthorizationDetailsTypes) {
        final int tenantId = IdentityTenantUtil.getLoginTenantId();
        Arrays.stream(selectedAuthorizationDetailsTypes).forEach(type -> {
            try {
                this.authorizationDetailDAO.createAuthorizationDetailsBinding(consumerAppId, type, tenantId);
            } catch (SQLException e) {
                LOG.error("Error occurred while binding authorization details types to SP. Caused by, ", e);
            }
        });
    }

    private void unbindAuthorizationDetailsTypesFromApp(final String consumerAppId,
                                                        final String[] deselectedAuthorizationDetailsTypes) {
        final int tenantId = IdentityTenantUtil.getLoginTenantId();
        Arrays.stream(deselectedAuthorizationDetailsTypes).forEach(type -> {
            try {
                this.authorizationDetailDAO.deleteAuthorizationDetailsBinding(consumerAppId, type, tenantId);
            } catch (SQLException e) {
                LOG.error("Error occurred while binding authorization details types to SP. Caused by, ", e);
            }
        });
    }

    private String[] extractDeselectedAuthorizationDetailsTypes(final String[] dbAuthorizationDetailsTypes,
                                                                final String[] newAuthorizationDetailsTypes) {
        return Arrays.stream(dbAuthorizationDetailsTypes)
                .filter(dbType -> !Arrays.asList(newAuthorizationDetailsTypes).contains(dbType))
                .toArray(String[]::new);
    }

    private String[] extractSelectedAuthorizationDetailsTypes(final String[] dbAuthorizationDetailsTypes,
                                                              final String[] newAuthorizationDetailsTypes) {
        return Arrays.stream(newAuthorizationDetailsTypes)
                .filter(newType -> !Arrays.asList(dbAuthorizationDetailsTypes).contains(newType))
                .toArray(String[]::new);
    }

    public void addRequestedAuthorizationDetailsToCache(final String sessionDataKey,
                                                        final List<AuthorizationDetails> validatedAuthorizationDetails) {

        AuthorizationDetailCacheEntry cacheEntry = new AuthorizationDetailCacheEntry();
        cacheEntry.setRequestedAuthorizationDetails(validatedAuthorizationDetails);

        AuthorizationDetailCache.getInstance().addToCache(new AuthorizationDetailCacheKey(sessionDataKey), cacheEntry);
    }

    public List<AuthorizationDetails> getRequestedAuthorizationDetailsFromCache(final String sessionDataKey) {
        AuthorizationDetailCacheEntry authorizationDetailCacheEntry = AuthorizationDetailCache.getInstance()
                .getValueFromCache(new AuthorizationDetailCacheKey(sessionDataKey));
        return authorizationDetailCacheEntry.getRequestedAuthorizationDetails();
    }

    public void persistConsentedAuthorizationDetails(String authorizationCode,
                                                     List<AuthorizationDetails> consentedAuthorizationDetails) {

        AuthorizationDetailCacheEntry authorizationDetailCacheEntry = AuthorizationDetailCache.getInstance()
                .getValueFromCache(new AuthorizationDetailCacheKey(authorizationCode));
        authorizationDetailCacheEntry
                .setConsentedAuthorizationDetails(consentedAuthorizationDetails);

        consentedAuthorizationDetails.forEach(authorizationDetail ->
        {
            try {
                this.authorizationDetailDAO.createConsentedAuthorizationDetails(
                        authorizationDetail.getType(), gson.toJson(authorizationDetail),
                        authorizationCode, IdentityTenantUtil.getLoginTenantId());
            } catch (SQLException e) {
                LOG.error("Error occurred while persisting consented authorization details. Caused by, ", e);
            }
        });
    }

}
