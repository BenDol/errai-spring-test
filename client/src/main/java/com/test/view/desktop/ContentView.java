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
package com.test.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.ui.MaterialContainer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContentView extends Composite {

    interface ViewBinder extends UiBinder<MaterialContainer, ContentView> {
    }

    public ContentView() {
        initWidget(GWT.<UiBinder<MaterialContainer, ContentView>>create(ViewBinder.class).createAndBindUi(this));
    }

    @Override
    public MaterialContainer getWidget() {
        return (MaterialContainer)super.getWidget();
    }
}
