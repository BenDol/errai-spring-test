/*
 * #%L
 * Errai Prototype
 * %%
 * Copyright (C) 2015 - 2016 Insclix
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
package com.test.role;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.security.shared.api.RoleImpl;
import org.jboss.errai.ui.nav.client.local.PageRole;

@Portable
public class AdminRole extends RoleImpl implements PageRole {

    public static final String NAME = "ADMIN";
    public static final AdminRole INSTANCE = new AdminRole();

    public AdminRole() {
        super(NAME);
    }
}
