/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2ScopeConsentException;
import org.wso2.carbon.identity.oauth2.model.UserApplicationScopeConsentDO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 */
public class OAuthUserConsentedAuthorizationDetailsDAOImpl {

    private static final Log log = LogFactory.getLog(OAuthUserConsentedAuthorizationDetailsDAOImpl.class);

    public void addUserConsentForApplication(String userId, int tenantId,
                                             UserApplicationScopeConsentDO userConsent)
            throws IdentityOAuth2ScopeConsentException {

        if (log.isDebugEnabled()) {
            log.debug("Adding scope consents for userId : " + userId + " and appId : " + userConsent.getAppId() +
                    " and tenantId : " + tenantId + " for approved scopes : " +
                    userConsent.getApprovedScopes().stream().collect(Collectors.joining(", ")) + " and " +
                    "disapproved scopes : " + userConsent.getDeniedScopes().stream()
                    .collect(Collectors.joining(", ")) + ".");
        }
        try (Connection conn = IdentityDatabaseUtil.getDBConnection(true)) {
            String consentId = generateConsentId();
            deleteUserConsent(conn, userId, userConsent.getAppId(), tenantId);
            addUserConsentInformation(conn, userId, userConsent.getAppId(), tenantId, consentId);
            addUserConsentedScopes(conn, consentId, tenantId, userConsent);
            IdentityDatabaseUtil.commitTransaction(conn);
        } catch (SQLException e) {
            String msg = "Error occurred while adding scope consents for userId : " + userId + " and appId : " +
                    userConsent.getAppId() + " and tenantId : " + tenantId;
            throw new IdentityOAuth2ScopeConsentException(msg, e);
        }
    }


    private void addUserConsentInformation(Connection connection, String userId, String appId, int tenantId,
                                           String consentId) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(SQLQueries.INSERT_OAUTH2_USER_CONSENT)) {
            ps.setString(1, userId);
            ps.setString(2, appId);
            ps.setInt(3, tenantId);
            ps.setString(4, consentId);
            ps.execute();
        }
    }

    private void addUserConsentedScopes(Connection connection, String consentId, int tenantId,
                                        UserApplicationScopeConsentDO userConsentsToBeAdded)
            throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(SQLQueries.INSERT_OAUTH2_USER_CONSENTED_SCOPE)) {
            List<String> approvedScopes = userConsentsToBeAdded.getApprovedScopes();
            List<String> disapprovedScopes = userConsentsToBeAdded.getDeniedScopes();
            if (CollectionUtils.isNotEmpty(approvedScopes)) {
                for (String scope : approvedScopes) {
                    ps.setString(1, consentId);
                    ps.setString(2, scope);
                    ps.setInt(3, tenantId);
                    ps.setBoolean(4, true);
                    ps.addBatch();
                }
            }
            if (CollectionUtils.isNotEmpty(disapprovedScopes)) {
                for (String scope : disapprovedScopes) {
                    ps.setString(1, consentId);
                    ps.setString(2, scope);
                    ps.setInt(3, tenantId);
                    ps.setBoolean(4, false);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void deleteUserConsent(Connection connection, String userId, String appId, int tenantId)
            throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(SQLQueries.REMOVE_OAUTH2_USER_CONSENT_FOR_APP)) {
            ps.setString(1, userId);
            ps.setString(2, appId);
            ps.setInt(3, tenantId);
            ps.execute();
        }
    }

    private String generateConsentId() {

        return UUID.randomUUID().toString();
    }

    private String getConsentId(Connection connection, String userId, String appId, int tenantId) throws SQLException {

        String consentId = null;
        try (PreparedStatement ps = connection.prepareStatement(SQLQueries.GET_CONSENT_ID_FOR_CONSENT)) {
            ps.setString(1, userId);
            ps.setString(2, appId);
            ps.setInt(3, tenantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    consentId = rs.getString(1);
                }
            }
        }
        return consentId;
    }
}
