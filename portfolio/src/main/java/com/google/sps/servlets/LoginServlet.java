// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.servlets.ServletConstants;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    UserService userService;

    @Override
    public void init(){
        userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        if (userService.isUserLoggedIn()) {
            String logoutUrl = userService.createLogoutURL(ServletConstants.INDEX_URL);
            String logoutDetails = "<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>" +
                                    "<form action = \"/login\" method = \"post\">" +
                                    "<button type = \"submit\">Go Back</button>" +
                                    "</form>";
            response.getWriter().println(logoutDetails);
        } else {
            String loginUrl = userService.createLoginURL(ServletConstants.INDEX_URL);
            String loginDetails = "<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>" +
                                    "<form action = \"/login\" method = \"post\">" +
                                    "<button type = \"submit\">Go Back</button>" +
                                    "</form>";
            response.getWriter().println(loginDetails);
        }
    }
}
