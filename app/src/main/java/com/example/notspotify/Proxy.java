package com.example.notspotify;

/**
 * The Proxy implements ProxyInterface class. The class is incomplete
 *
 * @author  Oscar Morales-Ponce
 * @version 0.15
 * @since   2019-01-24
 */

import com.google.gson.JsonObject;


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

        JsonObject jsonparam = new JsonObject();
        if (remoteMethod.equals("Login")) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("password", param[1]);
            jsonparam.addProperty("call-semantics", "maybe");
        }
        else if (remoteMethod.equals("getUser")) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("call-semantics", "maybe");
        }
        else if (remoteMethod.equals("SignUp")) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("password", param[1]);
            jsonparam.addProperty("call-semantics", "maybe");
        }
        else if (remoteMethod.equals("returnSongs")) {
            jsonparam.addProperty("s", param[0]);
            jsonparam.addProperty("call-semantics", "at-most-one");
        }
        else if (   remoteMethod.equals("addPlaylist") ||
                    remoteMethod.equals("deletePlaylist")   ) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("playlistName", param[1]);
        }
        else if (   remoteMethod.equals("addSongToPlaylist") ||
                    remoteMethod.equals("deleteSongFromPlaylist")   ) {
            jsonparam.addProperty("username", param[0]);
            jsonparam.addProperty("playlistName", param[1]);
            jsonparam.addProperty("songID", param[2]);
        }
        else if (remoteMethod.equals("SongHandler")) {
            jsonparam.addProperty("songID", param[0]);
        }

        metadata.addProperty("remoteMethod", remoteMethod);
        metadata.add("param", jsonparam);
        metadata.addProperty("requestID", Integer.toString(requestID));
        metadata.addProperty("call-semantics", "maybe");

        cm.send(metadata);
        if (remoteMethod.equals("SongHandler"))
            return null;
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


