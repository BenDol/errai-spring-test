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

import com.test.dao.UserRepository;
import com.test.domain.User;
import com.test.role.UserRole;
import org.jboss.errai.security.shared.api.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserAuthoritiesExtractor implements AuthoritiesExtractor {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
        User user = (User) userRepository.loadUserByUsername((String)map.get("email"));
        if (user != null) {
            List<String> roles = new ArrayList<>();
            roles.add(UserRole.NAME);

            for (Role role : user.getRoles()) {
                if(!roles.contains(role.getName())) {
                    roles.add(role.getName());
                }
            }
            return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
        }
        return null;
    }
}
