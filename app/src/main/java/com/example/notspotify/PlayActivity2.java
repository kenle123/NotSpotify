package com.example.notspotify;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity2 extends AppCompatActivity {

    private static MediaPlayer mp;
    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    int totalTime = 0;

    TextView artistAndSongName;
    ImageButton backBtn;

    String songTitle = PlaylistSongsActivity.getSongTitle();
    String songID = PlaylistSongsActivity.getSongID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2);

        playBtn = (Button)findViewById(R.id.playBtn_2);
        elapsedTimeLabel = (TextView)findViewById(R.id.elapsedTimeLabel_2);
        remainingTimeLabel = (TextView)findViewById(R.id.remainingTimeLabel_2);

        artistAndSongName = (TextView)findViewById(R.id.artist_songname_2);
        backBtn = (ImageButton)findViewById(R.id.button_back_2);

        // On click listener for back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                finish();
            }
        });

        // Set textview to current artist name and song title
        artistAndSongName.setText(songTitle);

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

        // Start the media player
        mp.start();
        playBtn.setBackgroundResource(R.drawable.stop);

        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        // Position Bar
        positionBar = (SeekBar)findViewById(R.id.positionBar_2);
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
        volumeBar = (SeekBar)findViewById(R.id.volumeBar_2);
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
            // Play
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else {
            // Pause
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
}
