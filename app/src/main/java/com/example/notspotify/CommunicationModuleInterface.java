package com.example.notspotify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public interface CommunicationModuleInterface {
/*
* Handles the communication and call-semantics.
* @param request is a JSon object that contains the 
* remote method, object that handles the method, 
* and params. 
{
   "execute":
   {
        "remoteMethod":"getSongChunk",
        "object":"SongServices",
        "param":
          {
              "song":"Long",
              "fragment":"Long"
          },
   }
}
* It adds the request id before sending to have 
{
   "execute":
   {
        "requestId":"102291" 
        "name":"getSongChunk",
        "object":"SongServices",
        "param":
          {
              "song":"Long",
              "fragment":"Long"
          },
   }
}
* If the call-semantics of remote method is at-least-one or at-most-one, 
* it retransmits if it does not receive the reply within CWND (parameter
* given at compilation time) at most REPETITION (parameter
* given at compilation time, for example 2) times. It blocks until 
* it completes or fails (through an exception)
*/ 
    String send(JsonObject request);
}


