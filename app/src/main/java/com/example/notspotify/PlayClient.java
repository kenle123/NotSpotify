package com.example.notspotify;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.media.MediaPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.net.UnknownHostException;

import android.content.Context;
import android.util.Log;


public class PlayClient implements Runnable {
    private Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private byte[] mp3Sound;
    private final int PORT = 1111;
    DatagramSocket datagramSocket;
    Session session = MainActivity.getSession();

    public PlayClient(Context mContext) {
        context = mContext;
        try {
            datagramSocket = new DatagramSocket(PORT);
        } catch (SocketException sockEx) {
            System.exit(1);
            Log.d("socketfail", "can't create socket");
        }

    }

    public void run() {
        while(true) {
            try {
                DatagramPacket inPacket;
                byte[] buffer;
                try {
                    Log.d("packetcheck2", "packet waiting");
                    //MAX to be received
                    buffer = new byte[65000];
                    inPacket = new DatagramPacket(buffer, buffer.length);
                    datagramSocket.receive(inPacket);
                    Log.d("packetcheck", "packet received");
                    mp3Sound = inPacket.getData();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*          Byte array to be played has been received                                 */

                // create temp file that will hold byte array
                File tempMp3 = File.createTempFile("kurchina", "mp3", context.getCacheDir());
                tempMp3.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempMp3);
                fos.write(mp3Sound);
                fos.close();

                // resetting mediaplayer instance to evade problems
                mediaPlayer.reset();

                FileInputStream fis = new FileInputStream(tempMp3);
                mediaPlayer.setDataSource(fis.getFD());

                mediaPlayer.prepare();
                if(session.getMediaPlayer() != null) {
                    if(session.getMediaPlayer().isPlaying()) {
                        session.getMediaPlayer().stop();
                        session.getMediaPlayer().reset();
                        session.getMediaPlayer().release();
                    }
                }

                // Start the media player
                session.setMediaPlayer(mediaPlayer);
                session.getMediaPlayer().start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
