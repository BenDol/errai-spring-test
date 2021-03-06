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
package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collection;

@Configuration
@Profile("dev")
public class DevIntitializer {

    private static final Logger logger = LoggerFactory.getLogger(DevIntitializer.class);

    @Bean
    public CommandLineRunner testData() {
        return args -> {
            logger.info("<<<<< Test Data >>>>>>");
        };
    }
}
