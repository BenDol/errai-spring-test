/*
 * #%L
 * insclix-app-budget-server
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
package com.test.security;

import com.expansel.errai.springsecurity.server.SpringSecurityAuthenticationService;
import com.test.domain.User;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Service
@Component
@SessionScope
public class SpringAuthenticationService extends SpringSecurityAuthenticationService implements AuthenticationService {

    private AuthenticationManager authenticationManager;
    private HttpSession session;

    @Autowired
    public SpringAuthenticationService(AuthenticationManager authenticationManager, HttpSession session) {
        super(authenticationManager, session);

        this.authenticationManager = authenticationManager;
        this.session = session;
    }

    @Override
    protected User userFromAuthentication(Authentication auth) {
        Object principal = auth.getPrincipal();
        User user;
        if (principal instanceof User) {
            user = (User) auth.getPrincipal();
        } else {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            Collection<? extends Role> erraiRoles = authoritiesToErraiRoles(authorities);
            user = new User(auth.getName(), erraiRoles);
        }

        if(user == null) {
            logout();
            throw new BadCredentialsException("This email is not a registered user");
        }

        return user;
    }

    @Override
    public User getUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            return userFromAuthentication(authentication);
        }
        return (User) User.ANONYMOUS;
    }

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && session != null) {
            try {
                SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
                if (securityContext != null) {
                    authentication = securityContext.getAuthentication();
                }
            } catch (IllegalStateException ex) {
                //logger.debug("Attempted to get SPRING_SECURITY_CONTEXT attribute from an invalidated session.", ex);
            }
        }
        return authentication;
    }
}
