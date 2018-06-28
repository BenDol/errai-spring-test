/*
 * #%L
 * Server
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
package com.test.security.config;

import com.expansel.errai.springsecurity.server.ErraiClientBusAuthenticationEntryPoint;
import com.expansel.errai.springsecurity.server.ErraiRestClientAuthenticationEntryPoint;
import com.test.RestInfo;
import com.test.RestInfo.Users;
import com.test.SharedConstants;
import com.expansel.errai.springsecurity.server.ErraiWebSecurityConfigurerAdapter;
import com.test.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.Filter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends ErraiWebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final Filter ssoFilter;

    @Autowired
    public WebSecurityConfig(@Lazy UserRepository userRepository,
                             @Lazy @Qualifier("ssoFilter") Filter ssoFilter) {
        this.userRepository = userRepository;
        this.ssoFilter = ssoFilter;
    }

    @Bean
    protected GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        // match requests coming from the errai message bus as configured on Errai servlet in web.xml
        AntPathRequestMatcher clientBusMatcher = new AntPathRequestMatcher("/**/*.erraiBus");

        // match rest requests
        // if the rest service also serves external parties then another auth mechanism
        // needs to be set up eg. basic or oauth. See this for examples:
        // http://www.baeldung.com/spring-security-multiple-entry-points
        AntPathRequestMatcher restClientMatcher = new AntPathRequestMatcher("/rest/**");

        // Since we are illustrating a mix of bus and client side jaxrs requests our errai client
        // matches either bus or rest requests.
        OrRequestMatcher erraiClientMatcher = new OrRequestMatcher(Arrays.asList(clientBusMatcher, restClientMatcher));

        // match requests not coming from client bus or client jaxrs which should be redirected to html login page
        // and not return json
        NegatedRequestMatcher notErraiBusMatcher = new NegatedRequestMatcher(erraiClientMatcher);

        http.csrf()
            .ignoringAntMatchers(RestInfo.LOGIN_PATH)
            .ignoringAntMatchers(RestInfo.ROOT + "/**")
            .ignoringAntMatchers("/"+ SharedConstants.LOGIN_TOKEN)
        .and()
            .formLogin()
            .permitAll()
            .loginProcessingUrl(RestInfo.LOGIN_PATH)
            .usernameParameter(Users.Params.USERNAME)
            .passwordParameter(Users.Params.PASSWORD)
            .loginPage("/"+ SharedConstants.LOGIN_TOKEN)
        .and()
            .httpBasic()
        .and()
            .logout().logoutSuccessUrl("/").permitAll()
        .and()
            .exceptionHandling()
            // client bus json response to generate security error client side
            .defaultAuthenticationEntryPointFor(new ErraiClientBusAuthenticationEntryPoint(),
                    clientBusMatcher)
            // rest client json response to generate security error client side
            .defaultAuthenticationEntryPointFor(new ErraiRestClientAuthenticationEntryPoint(),
                    restClientMatcher)
        .and()
            .sessionManagement().sessionAuthenticationStrategy(
                new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl()));

        http.antMatcher("/oauth2/**").addFilterBefore(ssoFilter, BasicAuthenticationFilter.class);

        http.authorizeRequests().anyRequest().permitAll();

        // disable page caching
        http.headers().cacheControl();

        // Rest API requires authentication.
        http.authorizeRequests().antMatchers(RestInfo.ROOT + "/**").authenticated();
        http.authorizeRequests().antMatchers(RestInfo.LOGIN_PATH).permitAll();
    }

    @Bean
    public ShaPasswordEncoder shaPasswordEncoder() {
        return new ShaPasswordEncoder(512);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userRepository);
        authenticationProvider.setPasswordEncoder(shaPasswordEncoder());
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
}
