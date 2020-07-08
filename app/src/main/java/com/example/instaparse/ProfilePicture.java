package com.example.instaparse;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ProfilePicture")
public class ProfilePicture extends ParseObject {
    public ProfilePicture(){
    }
    public static final String KEY_PROFILE_PICTURE = "profilePicture" ;
    public static final String KEY_USER_POINTER = "userPointer" ;


    public ParseFile getProfilePicture(){
        return getParseFile(KEY_PROFILE_PICTURE);
    }
    public void setProfilePicture(ParseFile image){
        put(KEY_PROFILE_PICTURE,image);
    }

    public void setUser(ParseUser user){
        put(KEY_USER_POINTER,user);
    }
}
