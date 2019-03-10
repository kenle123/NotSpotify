package com.example.notspotify;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   2019-01-24
 */

import android.provider.ContactsContract;
import android.util.Log;

import com.example.notspotify.CommunicationModule;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class Proxy implements ProxyInterface {
    CommunicationModule cm;
    private static int requestID = 1;
    public Proxy()
    {
        cm = new CommunicationModule();
    }

    /*
     * Executes the  remote method "remoteMethod". The method blocks until
     * it receives the reply of the message.
     */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        RemoteRef rr = new RemoteRef();
        JsonObject metadata = rr.getRemoteReference(remoteMethod);

//        JsonObject exe = new JsonObject();
        JsonObject jsonparam = new JsonObject();
        if (remoteMethod.equals("Login")) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("password", param[1]);
        }
        else if (remoteMethod.equals("SignUp")) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("password", param[1]);
        }
        else if (remoteMethod.equals("returnSongs")) {
            jsonparam.addProperty("s", param[0]);
        }
        metadata.addProperty("remoteMethod", remoteMethod);
        metadata.add("param", jsonparam);
//        metadata.addProperty("return", "Integer");
        metadata.addProperty("requestID", Integer.toString(requestID));
        metadata.addProperty("call-semantics", "at-most-one");
//        exe.add("execute", metadata);

        cm.send(metadata);
        requestID += 1;
        //Log.d("RETCM", "sent");
        JsonObject ret = null;




//                try {
//                    ret = cm.receive();
//                    //Log.d("RETCM", "right after" +ret.toString());
//
//                    //String rcvd = "rcvd from " + dp.getAddress() + ", " + dp.getPort() + ": "+ new String(dp.getData(), 0, dp.getLength());
//                    //System.out.println(rcvd);
//                }
//                catch (SocketTimeoutException e) {
//                    // timeout exception.
//                   Log.d("RETCM", "Timeout achieved");
//                    cm.getDatagramSocket().close();
//                }

        ret = cm.getRet();






        //JsonObject ret = cm.receive();
        //Log.d("RETCM", ret.toString());
        return ret;


//        JsonObject temp = new Gson().fromJson(exe.toString(),JsonObject.class);
//        Log.d("Json", temp.get("execute").getAsJsonObject().get("param").getAsJsonObject().get("username").getAsString());

//        JsonObject jsonRequest = new JsonObject();
//        JsonObject jsonParam = new JsonObject();
//
//        jsonRequest.addProperty("remoteMethod", remoteMethod);
//        jsonRequest.addProperty("objectName", "SongServices");
        // It is hardcoded. Instead it should be dynamic using  RemoteRef
//        if (remoteMethod.equals("getSongChunk"))
//        {
//
//            jsonParam.addProperty("song", param[0]);
//            jsonParam.addProperty("fragment", param[1]);
//
//        }
//        if (remoteMethod.equals("getFileSize"))
//        {
//            jsonParam.addProperty("song", param[0]);
//        }
//        jsonRequest.add("param", jsonParam);

//        JsonParser parser = new JsonParser();
//        String strRet =  this.dispacher.dispatch(jsonRequest.toString());

//        return parser.parse(strRet).getAsJsonObject();
//        return null;
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


