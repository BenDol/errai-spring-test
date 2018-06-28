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
package com.test.domain;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.security.shared.api.Group;
import org.jboss.errai.security.shared.api.Role;
import org.jboss.errai.security.shared.api.identity.UserImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Portable
public class User extends UserImpl implements UserDetails {

    private String givenName;
    private String familyName;
    private String profile;
    private String picture;
    private String email;
    private String gender;
    private String locale;
    private String password;
    private boolean emailVerified;

    public User() {
        super("");
    }

    public User(String name) {
        super(name);
        setEmail(name);
    }

    public User(String name, Collection<? extends Role> roles) {
        super(name, roles);
        setEmail(name);
    }

    public User(String name, Collection<? extends Role> roles, Map<String, String> properties) {
        super(name, roles, properties);
        setEmail(name);
    }

    public User(String name, Collection<? extends Role> roles, Collection<? extends Group> groups) {
        super(name, roles, groups);
        setEmail(name);
    }

    public User(@MapsTo("name") final String name,
                @MapsTo("roles") final Collection<? extends Role> roles,
                @MapsTo("groups") final Collection<? extends Group> groups,
                @MapsTo("properties") final Map<String, String> properties) {
        super(name, roles, groups, properties);
        setEmail(name);
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
