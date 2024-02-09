/*
 * Copyright (c) 2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth.rar.cache;

import org.wso2.carbon.identity.oauth.cache.CacheKey;

import static org.wso2.carbon.identity.oauth.rar.util.AuthorizationDetailConstants.AUTHORIZATION_DETAILS;

/**
 * Authorization detail cache key.
 */
public class AuthorizationDetailCacheKey extends CacheKey {

    private static final long serialVersionUID = 616023678375902263L;
    private final String cacheKey;

    public AuthorizationDetailCacheKey(String cacheKey) {
        this.cacheKey = cacheKey + AUTHORIZATION_DETAILS;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AuthorizationDetailCacheKey)) {
            return false;
        }
        return this.cacheKey.equals(((AuthorizationDetailCacheKey) object).getCacheKey());
    }

    @Override
    public int hashCode() {
        return cacheKey.hashCode();
    }
}
