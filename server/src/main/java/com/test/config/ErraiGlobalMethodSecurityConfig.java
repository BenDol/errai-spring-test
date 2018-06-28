/*
 * #%L
 * errai-spring-test-server
 * %%
 * Copyright (C) 2017 - 2018 Pivotal Software, Inc.
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
package com.test.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@EnableGlobalMethodSecurity(proxyTargetClass=true,
    prePostEnabled = true,
    securedEnabled = true)
@Configuration
public class ErraiGlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    /**
     * <p>Avoid Spring security applied ROLE_ prefix to authorities. This is something
     * used to distinguish roles from other types of authorities in voters and
     * therefore not recommended by spring docs, but not using it since we are only
     * using roles and the ROLE_ prefix concept rather obscure for many users.</p>
     *
     * <p>See this reference: https://stackoverflow.com/questions/11539162/why-does-spring-securitys-rolevoter-need-a-prefix</p>
     */
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // Remove the ROLE_ prefix
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();

        //Remove the ROLE_ prefix from RoleVoter for @Secured and hasRole checks on methods
        accessDecisionManager.getDecisionVoters().stream()
                .filter(RoleVoter.class::isInstance)
                .map(RoleVoter.class::cast)
                .forEach(it -> it.setRolePrefix(""));

        return accessDecisionManager;
    }

    /**
     * We will provide our own {@link HttpSessionMethodSecurityInterceptor} to check the session for the
     * {@link org.springframework.security.core.context.SecurityContext}.
     */
    /*@Bean
    @Override
    public MethodInterceptor methodSecurityInterceptor() {
        HttpSessionMethodSecurityInterceptor methodSecurityInterceptor = new HttpSessionMethodSecurityInterceptor();

        methodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        methodSecurityInterceptor.setAfterInvocationManager(afterInvocationManager());
        methodSecurityInterceptor
                .setSecurityMetadataSource(methodSecurityMetadataSource());
        RunAsManager runAsManager = runAsManager();
        if (runAsManager != null) {
            methodSecurityInterceptor.setRunAsManager(runAsManager);
        }
        return methodSecurityInterceptor;
    }*/
}
