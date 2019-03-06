package com.example.notspotify;

import com.google.gson.JsonObject;

public class RemoteRef implements RemoteRefInterface {
    public RemoteRef(){}
    public JsonObject getRemoteReference(String remoteMethod) {
        JsonObject returnJson = new JsonObject();

        if(remoteMethod.equals("Login")) {
            //returnJson.addProperty("remoteMethod", "handleSignIn");
            returnJson.addProperty("object", "SignIn"); // SignIn = classname
        }

        if(remoteMethod.equals("SignUp")) {
            //returnJson.addProperty("remoteMethod", "handleSignIn");
            returnJson.addProperty("object", "SignIn");
        }

        return returnJson;
    }
}
