/*
 * #%L
 * insclix-app-survey-server
 * %%
 * Copyright (C) 2017 Insclix
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
package com.test.dao;

import com.test.domain.User;
import com.test.role.AdminRole;
import com.test.role.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository implements UserDetailsService {

    private final Map<String, UserDetails> users = new HashMap<>();
    private final Map<String, String> secrets = new HashMap<>();

    @Autowired
    ShaPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }

    public String getSecret(String key) {
        return secrets.getOrDefault(key, "");
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String key) {
        UserDetails user = users.get(key);
        return user != null ? user.getAuthorities() : Collections.emptyList();
    }

    @PostConstruct
    public void onPostConstruct() {
        String password = passwordEncoder.encodePassword("11", null);

        User user = new User("dolb90@gmail.com", Collections.singletonList(UserRole.INSTANCE));
        user.setPassword(password);
        users.put(user.getEmail(), user);
        secrets.put(user.getEmail(), "11");

        User user2 = new User("user2", Collections.singletonList(UserRole.INSTANCE));
        user2.setPassword(password);
        users.put(user2.getEmail(), user2);
        secrets.put(user2.getEmail(), "11");

        User admin = new User("admin", Collections.singletonList(AdminRole.INSTANCE));
        admin.setPassword(password);
        users.put(admin.getEmail(), admin);
        secrets.put(admin.getEmail(), "11");
    }
}
