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

import org.jboss.errai.bus.server.servlet.DefaultBlockingServlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The {@link DefaultBlockingServlet} was not setting the content type to application/json 
 * for session expiry responses. Needs to be fixed in Errai source.
 *
 * @author Zach Visagie
 */
public class FixedBlockingServlet extends DefaultBlockingServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void sendDisconnectDueToSessionExpiry(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        super.sendDisconnectDueToSessionExpiry(response);
    }
}