/*
 * #%L
 * Errai Prototype
 * %%
 * Copyright (C) 2015 - 2017 Insclix
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

import com.google.gwt.user.client.ui.RootPanel;
import com.test.view.desktop.ContentView;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@EntryPoint
public class Client {

    @Inject
    private ContentView contentView;

    @Inject
    private Navigation navigation;

    @PostConstruct
    protected void onPostConstruct() {
        RootPanel.get().add(contentView);
        contentView.getWidget().add(navigation.getContentPanel());
    }
}
