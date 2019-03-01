package com.example.notspotify;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   2019-01-24
 */

import com.example.notspotify.CommunicationModule;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Proxy implements ProxyInterface {
    CommunicationModule cm = new CommunicationModule();
//    Dispatcher dispacher;   // This is only for test. it should use the Communication  Module
    public Proxy()
    {
//        this.dispacher = dispacher;
    }

    /*
     * Executes the  remote method "remoteMethod". The method blocks until
     * it receives the reply of the message.
     */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();

        jsonRequest.addProperty("remoteMethod", remoteMethod);
        jsonRequest.addProperty("objectName", "SongServices");
        // It is hardcoded. Instead it should be dynamic using  RemoteRef
        if (remoteMethod.equals("Login"))
        {

        }
        if (remoteMethod.equals("getSongChunk"))
        {

            jsonParam.addProperty("song", param[0]);
            jsonParam.addProperty("fragment", param[1]);

        }
        if (remoteMethod.equals("getFileSize"))
        {
            jsonParam.addProperty("song", param[0]);
        }
        jsonRequest.add("param", jsonParam);

        JsonParser parser = new JsonParser();
//        String strRet =  this.dispacher.dispatch(jsonRequest.toString());

//        return parser.parse(strRet).getAsJsonObject();
        return null;
    }

    /*
     * Executes the  remote method remoteMethod and returns without waiting
     * for the reply. It does similar to synchExecution but does not
     * return any value
     *
     */
    public void asynchExecution(String remoteMethod, String[] param)
    {
        return;
    }

    public String sendRequest(JsonObject j) {
        return cm.send(j);
    }
}


