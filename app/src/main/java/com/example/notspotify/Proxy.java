package com.example.notspotify;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   2019-01-24
 */

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;


public class Proxy implements ProxyInterface {
    CommunicationModule cm;
    private static int requestID = 1;
    Context context;
    public Proxy(Context cxt)
    {
        cm = new CommunicationModule();
        this.context = cxt;
    }

    /*
     * Executes the  remote method "remoteMethod". The method blocks until
     * it receives the reply of the message.
     */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        RemoteRef rr = new RemoteRef(context);
        //first part of the jsonobject meant to be sent to the server
        JsonObject metadata = rr.getRemoteReference(remoteMethod);
        //create jsonobject of parameters
        JsonObject jsonparam = new JsonObject();
        for (int i = 0; i < param.length; i++) {
            jsonparam.addProperty(Integer.toString(i), param[i]);
        }

        metadata.add("param", jsonparam);
        metadata.addProperty("requestID", Integer.toString(requestID));
        cm.send(metadata);
        requestID += 1;
        JsonObject ret = cm.getRet();

        return ret;
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




}


