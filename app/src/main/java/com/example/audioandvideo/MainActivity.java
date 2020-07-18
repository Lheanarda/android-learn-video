package com.example.audioandvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

//1. Add Video View in Layout
//2. Add Raw Folder Directory
//3. add video or sound to raw folder (nama musti sesuai android studio rules)
//4. code untuk play video di btnplayclick
//5.media controller untuk kontrol video/audio

//FOR SOUND
//Media player = video view for audio

//FOR CONTROLLING VOLUME
//1. Add SeekBar UI
//2. Add Audio Manager
//3. mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//4. set maximum and minimum audio value in int

//FOR SEEKBAR MUSIC TIMER
//1. add timer in play button and pause button schedule timer
//2. add max value to seekbar : mediaplayer.getduration()
//3. code in seekbarmove
//4. media player.onclompletion listener

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener  {

    //UI Component
    private VideoView vidLearn;//for video
    private Button btnPlayVideo,btnPlayMusic,btnPauseMusic;
    private SeekBar seekBarVolume; //for controlling audio volume
    private SeekBar seekBarMoveBackAndForth;

    private MediaController mMediaController;
    private MediaPlayer mMediaPlayer; //for audio
    private AudioManager mAudioManager; // for audio volume control

    private Timer musicTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ui component
        btnPlayVideo= findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        seekBarMoveBackAndForth  = findViewById(R.id.sbmove);

        //video
        vidLearn = findViewById(R.id.vidLearn); //for video
        mMediaController = new MediaController(MainActivity.this); //media control

        //load audio
        mMediaPlayer = MediaPlayer.create(this,R.raw.killthislove);
        //audio manager
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //SEEK BAR
        int maximumVolumeUserDevice = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeUserDevice = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maximumVolumeUserDevice);
        seekBarVolume.setProgress(currentVolumeUserDevice);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //called when user play with seek(up down volume)
                if(fromUser){
                    //Toast.makeText(MainActivity.this,progress+"",Toast.LENGTH_SHORT).show();
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0); // to apply volume based on seekbar
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //called when user start to touch the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //called when user stop touching seekbar
            }
        });
        seekbarMove();

        //implements method extend
        mMediaPlayer.setOnCompletionListener(MainActivity.this);


    }

    //for play video and control video
    public void btnPlayClick (View view){
        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.mymatch);
        vidLearn.setVideoURI(videoUri);

        vidLearn.setMediaController(mMediaController);
        mMediaController.setAnchorView(vidLearn);

        vidLearn.start();
    }

    public void btnPlayMusic (View view){
        mMediaPlayer.start();
        //music timer ui
        musicTimer = new Timer();
        musicTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //can update UI using schedule
                seekBarMoveBackAndForth.setProgress(mMediaPlayer.getCurrentPosition());
            }
        },0,1000 );
    }

    public void btnPauseMusic(View view){
        mMediaPlayer.pause();

        //music timer ui
        musicTimer.cancel();
    }

    //move back and forth seekbar
    public void seekbarMove(){
        seekBarMoveBackAndForth.setMax(mMediaPlayer.getDuration());//get duration of music playing

        seekBarMoveBackAndForth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
//                    Toast.makeText(MainActivity.this,progress+"",Toast.LENGTH_SHORT).show();
                    mMediaPlayer.seekTo(progress); // user can control play length music
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.start();
            }
        });

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        musicTimer.cancel(); //to stop timer
    }
}
