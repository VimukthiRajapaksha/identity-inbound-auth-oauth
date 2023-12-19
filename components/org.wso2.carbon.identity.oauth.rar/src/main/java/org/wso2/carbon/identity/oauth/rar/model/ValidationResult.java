package org.wso2.carbon.identity.oauth.rar.model;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    private boolean status;
    private String reason;
    private Map<String, Object> meta;

    public ValidationResult(final boolean status, final String reason, final Map<String, Object> meta) {
        this.status = status;
        this.reason = reason;
        this.meta = meta;
    }

    public ValidationResult(final String reason) {
        this(false, reason, new HashMap<>());
    }

    public boolean isValid() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }


}
