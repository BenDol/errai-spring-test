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

import com.expansel.errai.spring.server.ErraiApplicationListener;
import com.expansel.errai.spring.server.ErraiRequestDispatcherFactoryBean;
import com.expansel.errai.spring.server.ErraiServerMessageBusFactoryBean;
import com.expansel.errai.springsecurity.server.SpringSecurityMessageCallbackWrapper;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.bus.server.api.ServerMessageBus;
import org.jboss.errai.common.server.CacheManifestServlet;
import org.jboss.errai.marshalling.server.protocol.ErraiProtocolServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.RequestContextFilter;

@Configuration
public class SpringErraiConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringErraiConfig.class);

    /**
     *  This method needs to be static because it is a BeanFactoryPostProcessor
     */
    @Bean
    public static ErraiApplicationListener erraiApplicationListener() {
        return new ErraiApplicationListener(new SpringSecurityMessageCallbackWrapper());
    }

    @Bean
    public FilterRegistrationBean requestContextFilter() {
        final FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new RequestContextFilter());
        filterRegBean.addUrlPatterns("*");
        filterRegBean.setEnabled(Boolean.TRUE);
        filterRegBean.setName("RequestContextFilter");
        return filterRegBean;
    }

    @Bean
    public ServletRegistrationBean erraiBlockingServlet() {
        logger.info("Registering Errai Servlet");
        ServletRegistrationBean registration = new ServletRegistrationBean(
            new FixedBlockingServlet(), "*.erraiBus");
        registration.addInitParameter("auto-discover-services", "false");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean cacheManifestServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new CacheManifestServlet(), "*.appcache");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public FactoryBean<ServerMessageBus> erraiServerMessageBusFactoryBean() {
        logger.info("Creating Errai ServiceMessageBus FactoryBean");
        return new ErraiServerMessageBusFactoryBean();
    }

    @Bean
    public FactoryBean<RequestDispatcher> erraiRequestDispatcherFactoryBean() {
        logger.info("Creating Errai RequestDispatcher FactoryBean");
        return new ErraiRequestDispatcherFactoryBean();
    }
}
