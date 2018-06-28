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

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;

/**
 * Fix issues with the {@link SecurityContext} being cleared in the {@link SecurityContextHolder}.
 *
 * The SecurityContext is required for the authentication check in {@link #beforeInvocation(Object)}.
 *
 * For some reason there are cases where our thread is run after the filter chain has finished, which
 * means a better solution to this issue would be to ensure we check the methods before the chain completes.
 * However the way we need to manage the Errai services might make that difficult.
 *
 * @author Ben Dol
 */
public class HttpSessionMethodSecurityInterceptor extends MethodSecurityInterceptor {

    @Autowired
    private HttpSession httpSession;

    private boolean clearSecurityContext;

    /**
     * This method should be used to enforce security on a <code>MethodInvocation</code>.
     *
     * @param mi The method being invoked which requires a security decision
     *
     * @return The returned value from the method invocation (possibly modified by the
     * {@code AfterInvocationManager}).
     *
     * @throws Throwable if any error occurs
     */
    public Object invoke(MethodInvocation mi) throws Throwable {
        InterceptorStatusToken token = beforeInvocation(mi);

        Object result;
        try {
            result = mi.proceed();
        }
        finally {
            super.finallyInvocation(token);
        }
        return super.afterInvocation(token, result);
    }

    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        clearSecurityContext = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null && httpSession != null) {
            SecurityContext securityContext = (SecurityContext)httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
            if (securityContext != null) {
                SecurityContextHolder.getContext().setAuthentication(securityContext.getAuthentication());
                clearSecurityContext = true;
            }
        }

        return super.beforeInvocation(object);
    }

    @Override
    protected Object afterInvocation(InterceptorStatusToken token, Object returnedObject) {
        if (clearSecurityContext) {
            // Finally we can clear the security context
            SecurityContextHolder.clearContext();
        }

        return super.afterInvocation(token, returnedObject);
    }
}
