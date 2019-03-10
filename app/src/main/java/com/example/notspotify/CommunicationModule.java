package com.example.notspotify;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class CommunicationModule implements CommunicationModuleInterface {

    private DatagramSocket datagramSocket;
    private InetAddress host;
    final int PORT = 1234;
    JsonObject ret;

    public CommunicationModule()
    {
        try
        {
            datagramSocket = new DatagramSocket();
            host = InetAddress.getByName("192.168.43.241");
            datagramSocket.setSoTimeout(200);   // set the timeout in millisecounds.



        }
        catch(SocketException e)
        {
            Log.d("Exception", "CM DS constructor");
            Log.d("Exception", "CM DS constructor");

        }
        catch(UnknownHostException e)
        {

        }
    }

    public void send(JsonObject request) {
        //DatagramSocket datagramSocket;
        DatagramPacket outPacket;
        try {

            String message = request.toString();

            //Log.d("RETCM", "call semantic check");

            String callSemantic = ((request).get("call-semantics").getAsString());

            if(callSemantic.equals("at-least-one"))
            {
                String reply = "invalid";
                while (reply.equals("invalid"))
               {
                    //JSONObject response;
                    outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT);
                    datagramSocket.send(outPacket);
                    ret = null;
                   try {
                       ret = receive();
                       if (ret != null)
                       {
                           reply = "valid";
                       }
                   }
                   catch (SocketTimeoutException e) {
                       Log.d("RETCM", "Timeout achieved");
                       datagramSocket.close();
                   }
                }

            }
            else if (callSemantic.equals("at-most-one"))
            {
                String requestSent = "invalid";
                while(requestSent.equals("invalid"))
                {
                    outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT);
                    datagramSocket.send(outPacket);
                    requestSent = "valid";

                    ret = null;

                    try {
                        ret = receive();
                        if (ret == null)
                        {
                            requestSent = "invalid";
                        }
                    }
                    catch (SocketTimeoutException e) {
                        Log.d("RETCM", "Timeout achieved");
                        datagramSocket.close();
                    }

                }
            }
            else if (callSemantic.equals("maybe"))
            {
                outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT);
                datagramSocket.send(outPacket);
                ret = receive();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public JsonObject receive() throws SocketTimeoutException

    {
//        String callSem = temp.get("execute").getAsJsonObject().get("call-semantics").getAsString();

        DatagramPacket inPacket;
        JsonObject response = null;
        byte[] buffer;
        try {

            //MAX to be received
            buffer = new byte[256];
            inPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(inPacket);
            response = new Gson().fromJson(new String(inPacket.getData(), 0, inPacket.getLength()), JsonObject.class);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return response;

    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public JsonObject getRet()
    {
        return ret;
    }


    //public DatagramSocket toServer(JsonObject j) {

    //}


}
