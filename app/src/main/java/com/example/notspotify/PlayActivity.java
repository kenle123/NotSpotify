package com.example.notspotify;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class PlayActivity extends AppCompatActivity {

    // MediaPlayer
    private static MediaPlayer mp = new MediaPlayer();
    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    int totalTime = 0;

    TextView artistAndSongName;
    ImageButton backBtn;
    Button addToPlaylistBtn;

    // Get song title and song ID from Browse Fragment
    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    // Session
    Session session;
    byte[] mp3Sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        session = MainActivity.getSession();

        // Bind media player variables
        playBtn = findViewById(R.id.playBtn);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);

        artistAndSongName = findViewById(R.id.artist_songname);
        backBtn = findViewById(R.id.button_back);
        addToPlaylistBtn = findViewById(R.id.playActivity_addSongToPlaylist);

        // On click listener for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set textview to current artist name and song title
        artistAndSongName.setText(songTitle);

//        // My Dearest
//        if(songID.equals("SOCIWDW12A8C13D406")) {
//            mp = MediaPlayer.create(this, R.raw.mydearest);
//        }
//        // Blue Bird
//        else if(songID.equals("SOXVLOJ12AB0189215")) {
//            mp = MediaPlayer.create(this, R.raw.bluebird);
//        }
//
//        // Black Paper Moon
//        else if(songID.equals("SONHOTT12A8C13493C")) {
//            mp = MediaPlayer.create(this, R.raw.blackpapermoon);
//        }
//
//        // Imperial March
//        else {
//            mp = MediaPlayer.create(this, R.raw.imperialmarch);
//        }
        try {
            mp3Sound = getSongFromServer();
            File tempMp3 = File.createTempFile("kurchina", "mp3", getApplicationContext().getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3Sound);
            fos.close();
            FileInputStream fis = new FileInputStream(tempMp3);
            mp.setDataSource(fis.getFD());
            mp.prepare();

        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Checks if the mediaplayer is currently playing, if so,
        // stop current song
        if(session.getMediaPlayer() != null) {
            if(session.getMediaPlayer().isPlaying()) {
                session.getMediaPlayer().stop();
                session.getMediaPlayer().reset();
                session.getMediaPlayer().release();
            }
        }

        // Start the media player
        session.setMediaPlayer(mp);
        session.getMediaPlayer().start();
        playBtn.setBackgroundResource(R.drawable.stop);

        // Set looping to false and start track at 0 and set volume
        mp.setLooping(false);
        session.getMediaPlayer().seekTo(0);
        session.getMediaPlayer().setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // On click listener for when user presses play button
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBtnClick(v,session);
            }
        });

        // On click listener for when user adds the song to the playlist
        addToPlaylistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });

        // Position Bar
        positionBar = (SeekBar)findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser) {
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Volume Bar
        volumeBar = (SeekBar)findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // Thread (Update positionBar & timeLabel)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(session.getMediaPlayer() != null) {
                    try {
                        Message msg = new Message();
                        msg.what = session.getMediaPlayer().getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch(InterruptedException e ) {}
                }
            }
        }).start();
    }

    /**
     * Handler for updating position bar and time labels
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;

            // Update position bar
            positionBar.setProgress(currentPosition);

            // Update labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText("-" + remainingTime);
        }
    };

    /**
     * Creates a time label for the current time it is at
     * @param time The current time the song is at
     * @return The current time label
     */
    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if(sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }


    /**
     * Action to be taken when user clicks on play button
     * @param view The view
     * @param session The session
     */
    public void playBtnClick(View view, Session session) {
        if(!session.getMediaPlayer().isPlaying()) {
            // Play
            session.getMediaPlayer().start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {
            // Pause
            session.getMediaPlayer().pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
    public byte[] getSongFromServer() {
        Proxy proxy = new Proxy();
        JsonObject ret;
        byte[] retByte = {};
        String[] array2 = {songID, "0"};
        do {
            ret = proxy.synchExecution("getSongFragment", array2);
            byte[] serverRet = ret.get("data").getAsString().getBytes();
            byte[] current = Arrays.copyOf(retByte, retByte.length);
            retByte = new byte[serverRet.length + current.length];
            System.arraycopy(current, 0, retByte, 0, current.length);
            System.arraycopy(serverRet, 0, retByte, current.length, serverRet.length);
            array2[1] = ret.get("currentIndex").getAsString();
        } while(ret.get("keepPulling").getAsString().equals("true"));
        return retByte;
    }

}
