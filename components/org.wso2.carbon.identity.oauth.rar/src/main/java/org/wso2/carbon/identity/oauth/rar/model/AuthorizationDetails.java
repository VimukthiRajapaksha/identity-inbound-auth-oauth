package org.wso2.carbon.identity.oauth.rar.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizationDetails {
    private String type;
    private List<String> locations;
    private List<String> actions;
    @JsonProperty("datatypes")
    private List<String> dataTypes;
    private List<String> identifier;
    private List<String> privileges;
    private Map<String, Object> details;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public List<String> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<String> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public List<String> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<String> identifier) {
        this.identifier = identifier;
    }

    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    @JsonAnyGetter
    public Map<String, Object> getDetails() {
        return details;
    }

    @JsonAnySetter
    public void setDetail(String key, Object value) {
        if (details == null) {
            setDetails(new HashMap<>());
        }
        details.put(key, value);
    }


    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AuthorizationDetails {" +
                "type='" + type + '\'' +
                ", locations=" + locations +
                ", actions=" + actions +
                ", datatypes=" + dataTypes +
                ", identifier=" + identifier +
                ", privileges=" + privileges +
                ", details=" + details +
                '}';
    }
}
