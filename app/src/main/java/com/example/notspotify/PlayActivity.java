package com.example.notspotify;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime = 0;

    TextView artistAndSongName;
    ImageButton backBtn;

    String songTitle = BrowseFragment.getSongTitle();
    String songID = BrowseFragment.getSongID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        playBtn = (Button)findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView)findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel);

        artistAndSongName = (TextView)findViewById(R.id.artist_songname);
        backBtn = (ImageButton)findViewById(R.id.button_back);

        // On click listener for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set textview to current artist name and song title
        artistAndSongName.setText(songTitle);

        // Media Player
        // My Dearest
        if(songID.equals("SOCIWDW12A8C13D406")) {
            mp = MediaPlayer.create(this, R.raw.mydearest);
        }
        // Blue Bird
        else if(songID.equals("SOXVLOJ12AB0189215")) {
            mp = MediaPlayer.create(this, R.raw.bluebird);
        }

        // Black Paper Moon
        else if(songID.equals("SONHOTT12A8C13493C")) {
            mp = MediaPlayer.create(this, R.raw.blackpapermoon);
        }

        // Imperial March
        else {
            mp = MediaPlayer.create(this, R.raw.imperialmarch);
        }

        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

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
                while(mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
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
     * For when play button is clicked to play/pause song
     * @param view
     */
    public void playBtnClick(View view) {
        if(!mp.isPlaying()) {
            // Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {
            // Playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
