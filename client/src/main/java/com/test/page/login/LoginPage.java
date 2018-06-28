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
package com.test.page.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.test.domain.User;
import com.test.rpc.TestRemote;
import com.test.view.desktop.ContentView;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialContainer;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.security.shared.service.AuthenticationService;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Page(path = "login", role = {org.jboss.errai.ui.nav.client.local.api.LoginPage.class, DefaultPage.class})
public class LoginPage extends Composite {

    interface ViewBinder extends UiBinder<MaterialPanel, LoginPage> {
    }

    @Inject
    private Caller<TestRemote> testRemote;

    @Inject
    private Caller<AuthenticationService> loginService;

    @UiField
    MaterialLink loginLink, logoutLink;

    @UiField
    MaterialButton securedBtn;

    public LoginPage() {
        initWidget(GWT.<UiBinder<MaterialPanel, LoginPage>>create(ViewBinder.class).createAndBindUi(this));
    }

    @PostConstruct
    public void init() {
        loginService.call((RemoteCallback<User>) user -> {
            MaterialToast.fireToast("Welcome " + user.getEmail());
            loginLink.setVisible(false);
            logoutLink.setVisible(true);
            securedBtn.setVisible(true);
        }, (msg, t) -> {
            loginLink.setVisible(true);
            logoutLink.setVisible(false);
            securedBtn.setVisible(false);
            return false;
        }).getUser();
    }

    @UiHandler("logoutLink")
    public void logoutLinkClick(ClickEvent event) {
        loginService.call(e -> {
            Window.Location.reload();
        }).logout();
    }

    @UiHandler("securedBtn")
    public void securedBtnClick(ClickEvent event) {
        testRemote.call(e -> {
            MaterialToast.fireToast("Passed!");
        }, (msg, throwable) -> {
            MaterialToast.fireToast("Failed: " + msg);
            return true;
        })
        .securedTest();
    }
}
