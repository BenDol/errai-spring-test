/*
 * #%L
 * ${project}-shared
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
package com.test;

/**
 * RESTful API resource path structures.
 */
public interface RestInfo {
    // Root API Path
    String ROOT = "/api";
    String VERSION = "v1";

    // Web Socket Path
    String WEB_SOCKETS = "/sockets";

    // Login Path
    String LOGIN_PATH = RestInfo.ROOT + "/users/login";

    interface GlobalParams {
        String ID = "id";
    }

    /*
      Users API Branch
     */

    /** User API Structure, root: {@value Users#ROOT} */
    interface Users {
        String ROOT = "/" + Params.USERS;
        String PATH_ID = "/{" + GlobalParams.ID + "}";
        String PATH_LOGOUT = "/logout";
        String PATH_LOGIN = "/login";
        String PATH_PING = "/ping";

        /** Resource specific params */
        interface Params {
            String USERS = "users";
            String ID = USERS + "_" + Params.ID;
            String USERNAME = "username";
            String PASSWORD = "password";
        }
    }
}
