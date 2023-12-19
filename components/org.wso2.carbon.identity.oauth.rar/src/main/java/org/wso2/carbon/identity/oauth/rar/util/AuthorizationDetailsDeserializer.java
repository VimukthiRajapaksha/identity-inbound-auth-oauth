package org.wso2.carbon.identity.oauth.rar.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.wso2.carbon.identity.oauth.rar.model.AuthorizationDetails;

import java.io.IOException;

public class AuthorizationDetailsDeserializer extends StdDeserializer<AuthorizationDetails> {

    protected AuthorizationDetailsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AuthorizationDetails deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        return null;
    }
}
