package org.wso2.carbon.identity.oauth.rar.dao;

import org.apache.commons.lang.StringUtils;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;
import org.wso2.carbon.identity.oauth2.dao.OAuthUserConsentedScopesDAO;
import org.wso2.carbon.identity.oauth2.dao.OAuthUserConsentedScopesDAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AuthorizationDetailDAO {

    private final OAuthUserConsentedScopesDAOImpl oAuthUserConsentedScopesDAO;

    private static final String CREATE_AUTHORIZATION_DETAILS_BINDINGS =
            "INSERT INTO IDN_OAUTH2_AUTHORIZATION_DETAILS_BINDING (CONSUMER_KEY, AUTHORIZATION_DETAILS_TYPE, TENANT_ID) VALUES (?, ?, ?)";

    private static final String GET_AUTHORIZATION_DETAILS_BINDINGS =
            "SELECT AUTHORIZATION_DETAILS_TYPE FROM IDN_OAUTH2_AUTHORIZATION_DETAILS_BINDING WHERE CONSUMER_KEY=? AND TENANT_ID=?";

    private static final String DELETE_AUTHORIZATION_DETAILS_BINDINGS =
            "DELETE FROM IDN_OAUTH2_AUTHORIZATION_DETAILS_BINDING WHERE CONSUMER_KEY=? AND AUTHORIZATION_DETAILS_TYPE=? AND TENANT_ID=?";

    private static final String CREATE_CODE_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS (AUTHORIZATION_DETAILS_TYPE, AUTHORIZATION_DETAILS, CODE_ID, TENANT_ID) VALUES (?, ?, (SELECT CODE_ID FROM IDN_OAUTH2_AUTHORIZATION_CODE WHERE AUTHORIZATION_CODE=? AND TENANT_ID=?), ?)";

    private static final String GET_CODE_AUTHORIZATION_DETAILS = "SELECT IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.AUTHORIZATION_DETAILS " +
                    "FROM IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS " +
                    "INNER JOIN IDN_OAUTH2_AUTHORIZATION_CODE ON IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.CODE_ID = IDN_OAUTH2_AUTHORIZATION_CODE.CODE_ID " +
                    "WHERE IDN_OAUTH2_AUTHORIZATION_CODE.AUTHORIZATION_CODE=? AND IDN_OAUTH2_AUTHZ_CODE_AUTHORIZATION_DETAILS.TENANT_ID=?";

    private static final String CREATE_USER_CONSENTED_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_USER_CONSENTED_AUTHORIZATION_DETAILS (AUTHORIZATION_DETAILS_TYPE, AUTHORIZATION_DETAILS, CONSENT_ID, TENANT_ID) VALUES (?, ?, ?, ?)";

    private static final String CREATE_ACCESS_TOKEN_AUTHORIZATION_DETAILS =
            "INSERT INTO IDN_OAUTH2_ACCESS_TOKEN_AUTHORIZATION_DETAILS (AUTHORIZATION_DETAILS_TYPE, AUTHORIZATION_DETAILS, TOKEN_ID, TENANT_ID) VALUES (?, ?, ?, ?)";

    public AuthorizationDetailDAO() {
        this.oAuthUserConsentedScopesDAO = new OAuthUserConsentedScopesDAOImpl();
    }

    public void createAuthorizationDetailsBinding(final String consumerKey, final String authorizationDetailsType,
                                                  final int tenantId) throws SQLException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(CREATE_AUTHORIZATION_DETAILS_BINDINGS)) {
                prepStmt.setString(1, consumerKey);
                prepStmt.setString(2, authorizationDetailsType);
                prepStmt.setInt(3, tenantId);
                prepStmt.executeUpdate();
            }
        }
    }

    public String[] getAuthorizationDetailsTypesByConsumerKey(final String consumerKey, final int tenantId)
            throws SQLException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(GET_AUTHORIZATION_DETAILS_BINDINGS)) {
                prepStmt.setString(1, consumerKey);
                prepStmt.setInt(2, tenantId);
                try(ResultSet resultSet = prepStmt.executeQuery()) {
                    return extractAuthorizationDetailsTypes(resultSet);
                }
            }
        }
    }

    private String[] extractAuthorizationDetailsTypes(final ResultSet resultSet) throws SQLException {
        List<String> authorizationDetailsTypes = new ArrayList<>();
        while (resultSet.next()) {
            authorizationDetailsTypes.add(resultSet.getString(1));
        }
        return authorizationDetailsTypes.toArray(new String[authorizationDetailsTypes.size()]);
    }

    public void deleteAuthorizationDetailsBinding(final String consumerKey, final String authorizationDetailsType,
                                                  final int tenantId) throws SQLException {
        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(DELETE_AUTHORIZATION_DETAILS_BINDINGS)) {
                prepStmt.setString(1, consumerKey);
                prepStmt.setString(2, authorizationDetailsType);
                prepStmt.setInt(3, tenantId);
                prepStmt.executeUpdate();
            }
        }
    }

    public void createCodeAuthorizationDetails(String authorizationDetailsType, String authorizationDetails,
                                                    String authorizationCode, int tenantId) throws SQLException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(CREATE_CODE_AUTHORIZATION_DETAILS)) {
                prepStmt.setString(1, authorizationDetailsType);
                prepStmt.setString(2, authorizationDetails);
                prepStmt.setString(3, authorizationCode);
                prepStmt.setInt(4, tenantId);
                prepStmt.setInt(5, tenantId);
                prepStmt.executeUpdate();
            }
        }
    }

    public List<String> findConsentedAuthorizationDetailsByAuthzCode(String authorizationCode, int tenantId)
            throws SQLException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(GET_CODE_AUTHORIZATION_DETAILS)) {
                prepStmt.setString(1, authorizationCode);
                prepStmt.setInt(2, tenantId);
                try(ResultSet resultSet = prepStmt.executeQuery()) {
                    return extractAuthorizationDetails(resultSet);
                }
            }
        }
    }

    public void bindAuthorizationDetailsToConsent(String authorizationDetailsType, String authorizationDetails,
                                                          String userId, String appId, int tenantId)
            throws SQLException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            String consentId = oAuthUserConsentedScopesDAO.getConsentId(connection, userId, appId, tenantId);
            if (StringUtils.isNotBlank(consentId)) {
                this.createConsentedAuthorizationDetails(authorizationDetailsType, authorizationDetails, consentId, tenantId);
            }
        }
    }

    public void createConsentedAuthorizationDetails(String authorizationDetailsType, String authorizationDetails,
                                                    String consentId, int tenantId) throws SQLException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false)) {
            try (PreparedStatement prepStmt = connection.prepareStatement(CREATE_USER_CONSENTED_AUTHORIZATION_DETAILS)) {
                prepStmt.setString(1, authorizationDetailsType);
                prepStmt.setString(2, authorizationDetails);
                prepStmt.setString(3, consentId);
                prepStmt.setInt(4, tenantId);
                prepStmt.executeUpdate();
            }
        }
    }

    private List<String> extractAuthorizationDetails(final ResultSet resultSet) throws SQLException {
        List<String> authorizationDetailsTypes = new ArrayList<>();
        while (resultSet.next()) {
            authorizationDetailsTypes.add(resultSet.getString(1));
        }
        return authorizationDetailsTypes;
    }
}
