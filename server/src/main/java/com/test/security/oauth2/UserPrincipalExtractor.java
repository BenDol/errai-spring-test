/*
 * #%L
 * insclix-app-budget-server
 * %%
 * Copyright (C) 2017 - 2018 Insclix
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.test.security.oauth2;

import com.test.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserPrincipalExtractor implements PrincipalExtractor {

    private static final Logger logger = LoggerFactory.getLogger(UserPrincipalExtractor.class);

    @Override
    public Object extractPrincipal(Map<String, Object> details) {
        String email = (String) details.get("email");
        return extractDetails(new User(email), details);
    }

    private User extractDetails(User user, Map<String, Object> details) {
        // Google specific user details
        try {
            user.setGivenName((String) details.get("given_name"));
            user.setFamilyName((String) details.get("family_name"));
            user.setLocale((String) details.get("locale"));
            user.setEmailVerified((Boolean) details.get("email_verified"));
            user.setGender((String) details.get("gender"));
            user.setPicture((String) details.get("picture"));
        } catch (Exception ex) {
            logger.debug("Detail extraction caused an exception: " + ex.getMessage());
        }
        return user;
    }
}
