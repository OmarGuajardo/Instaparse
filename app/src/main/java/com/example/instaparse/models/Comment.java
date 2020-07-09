package com.example.instaparse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public Comment(){}
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "userPointer";
    public static final String KEY_POST= "postPointer";

    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }
    public void setPost(Post post){
        put(KEY_POST,post);
    }

    public ParseObject getPost(){
        return getParseObject(KEY_POST);
    }

}
