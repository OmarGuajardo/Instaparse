package com.example.instaparse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public Comment() {
    }

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "userPointer";
    public static final String KEY_POST = "postPointer";

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getDescription() throws ParseException {
        return fetchIfNeeded().getString(KEY_DESCRIPTION);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseUser getUser() throws ParseException {
        return getParseUser(KEY_USER).fetchIfNeeded();
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

    public ParseObject getPost() throws ParseException {
        return getParseObject(KEY_POST).fetchIfNeeded();
    }

}
