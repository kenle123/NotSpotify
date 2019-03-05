package com.example.notspotify;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CommunicationModule implements CommunicationModuleInterface {
    public JsonObject send(JsonObject request) {
        JsonObject temp = new Gson().fromJson(request.toString(),JsonObject.class);
//        String callSem = temp.get("execute").getAsJsonObject().get("call-semantics").getAsString();

        return toServer(temp);
    }
    public JsonObject toServer(JsonObject j) {
        InetAddress host;
        final int PORT = 1234;
        DatagramSocket datagramSocket;
        DatagramPacket inPacket, outPacket;
        JsonObject response;
        byte[] buffer;
        try {
            host = InetAddress.getByName("10.0.2.2");
            datagramSocket = new DatagramSocket();
            String message = j.toString();

            //JSONObject response;
            outPacket = new DatagramPacket(message.getBytes(), message.length(), host, PORT);
            datagramSocket.send(outPacket);
            buffer = new byte[message.getBytes("utf-8").length];
            inPacket = new DatagramPacket(buffer, buffer.length);
            datagramSocket.receive(inPacket);
            response = new Gson().fromJson(new String(inPacket.getData(), 0, inPacket.getLength()), JsonObject.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
