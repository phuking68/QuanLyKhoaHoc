package com.example.asm;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MediaActivity extends AppCompatActivity {

    CircleImageView imgPhuKing;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler ();
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private ImageView ivStartPlay , ivPrevMusic, ivNextMusic;
    public static int oneTimeOnly = 0;
    private TextView startTimeField;
    private TextView endTimeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        ivStartPlay = findViewById(R.id.ivStartPlay);
        ivPrevMusic = findViewById(R.id.ivPrevMusic);
        ivNextMusic = findViewById(R.id.ivNextMusic);
        imgPhuKing = findViewById(R.id.imgPhuKing);
        startTimeField = findViewById(R.id.startTimeField);
        endTimeField = findViewById(R.id.endTimeField);
        seekbar = findViewById(R.id.seekBar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.codontrensofa);
        seekbar.setMax(mediaPlayer.getDuration());

        //
        ivStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                play();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ivStartPlay.setImageResource(R.drawable.ic_play_24);
                        mediaPlayer.reset();
                        stopAnimation();
                    }
                });

            }
        });

        ivPrevMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivNextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //Animation
    private void startAnimation(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //rotationBy: quay 360 độ - setDuration: time quay hết 1 vòng
                imgPhuKing.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        imgPhuKing.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimation() {
        imgPhuKing.animate().cancel();
    }

    //music
    private void play(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            stopAnimation();
            ivStartPlay.setImageResource(R.drawable.ic_play_24);
        } else {
            mediaPlayer.start();
            ivStartPlay.setImageResource(R.drawable.ic_pause_24);
        }

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        endTimeField.setText(String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                finalTime)))
        );

        startTimeField.setText(String.format("%d : %d",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                startTime)))
        );
        myHandler.postDelayed(UpdateSongTime,100);

//        UpdateSeekBar updateSeekBar =new UpdateSeekBar();
//        myHandler.post(updateSeekBar);
    }

    //SeekBar
    private class UpdateSeekBar implements Runnable {

        @Override
        public void run() {
            seekbar.setProgress(mediaPlayer.getCurrentPosition());

            myHandler.postDelayed(this, 100);
        }
    }

    //time
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format("%d : %d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

}