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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/comments")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment").addSort("timestamp", SortDirection.ASCENDING);
        response.setContentType("text/html;");

        UserService userService = UserServiceFactory.getUserService();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        
        if(userService.isUserLoggedIn()){    
            ArrayList<ArrayList<String>> userComments = new ArrayList<>();
            for(Entity entity : results.asIterable()){
                ArrayList<String> userCommentCombo = new ArrayList<String>();
                userCommentCombo.add((String)entity.getProperty("email"));
                userCommentCombo.add((String)entity.getProperty("comment"));
                userComments.add(userCommentCombo);
            }
            String message = convertToJsonUsingGson(userComments);
            response.getWriter().println(message);
        }
        
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        
        String comment = request.getParameter("comment");
        long timestamp = System.currentTimeMillis();
        String email = userService.getCurrentUser().getEmail();
        
        if(!comment.equals("")){
            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("comment", comment);
            commentEntity.setProperty("email", email);
            commentEntity.setProperty("timestamp", timestamp);
            
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(commentEntity);
        }
        response.sendRedirect("/index.html");
    }

    private String convertToJsonUsingGson(ArrayList<ArrayList<String>> messages) {
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
    }
}

