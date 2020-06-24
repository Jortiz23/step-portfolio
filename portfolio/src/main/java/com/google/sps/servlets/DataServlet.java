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

import com.google.sps.servlets.Constants;

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

    DatastoreService datastore;

    public void init(){
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(Constants.COMMENT_ENTITY_ID).addSort(Constants.COMMENT_TIMESTAMP_ID, SortDirection.DESCENDING);
        PreparedQuery results = datastore.prepare(query);

        ArrayList<String> messages = new ArrayList<>();
        for(Entity entity : results.asIterable()){
            messages.add((String)entity.getProperty(Constants.COMMENT_TEXT_ID));
        }

        String json = convertToJsonUsingGson(messages);
        response.setContentType("text/html;");
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String comment = request.getParameter(Constants.COMMENT_TEXT_ID);
        long timestamp = System.currentTimeMillis();
        
        if(!comment.equals("")){
            Entity commentEntity = new Entity(Constants.COMMENT_ENTITY_ID);
            commentEntity.setProperty(Constants.COMMENT_TEXT_ID, comment);
            commentEntity.setProperty(Constants.COMMENT_TIMESTAMP_ID, timestamp);
            
            datastore.put(commentEntity);
        }
        response.sendRedirect(Constants.INDEX_URL);
    }

    private String convertToJsonUsingGson(ArrayList<String> messages) {
        Gson gson = new Gson();
        return gson.toJson(messages);
    }
}

