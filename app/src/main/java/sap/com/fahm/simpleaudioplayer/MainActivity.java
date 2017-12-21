/*
 * Copyright 2018 Google Inc. All rights reserved to FAHMAPPS.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sap.com.fahm.simpleaudioplayer;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import sap.com.fahm.simpleaudioplayer.player.AudioPlayerCallBack;
import sap.com.fahm.simpleaudioplayer.player.AudioPlayerManager;
import sap.com.fahm.simpleaudioplayer.player.IAudioPlayer;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int MEDIA_RES_ID = R.raw.fahm_app_raw;

    private TextView mTextDebug;
    private SeekBar mSeekbarAudio;
    private ScrollView mScrollContainer;
    private IAudioPlayer mIAudioPlayer;
    private boolean mUserIsSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        initializeSeekbar();
        initializePlaybackController();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mIAudioPlayer.loadMedia(MEDIA_RES_ID);
        Log.d(TAG, "onStart: create MediaPlayer");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mIAudioPlayer.isPlaying()) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mIAudioPlayer.release();
            Log.d(TAG, "onStop: release MediaPlayer");
        }
    }


    private void initializeUI() {
        mTextDebug = (TextView) findViewById(R.id.text_debug);
        ImageButton mPlayButton = (ImageButton) findViewById(R.id.button_play);
        ImageButton mPauseButton = (ImageButton) findViewById(R.id.button_pause);
        ImageButton mResetButton = (ImageButton) findViewById(R.id.button_stop);
        mSeekbarAudio = (SeekBar) findViewById(R.id.seekbar_audio);
        mScrollContainer = (ScrollView) findViewById(R.id.scroll_container);

        mPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIAudioPlayer.pause();
                    }
                });
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIAudioPlayer.play();
                    }
                });
        mResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mIAudioPlayer.stop();
                    }
                });
    }

    private void initializePlaybackController() {
        AudioPlayerManager mAudioPlayerManager = new AudioPlayerManager(this);
        LogHelper.Log("initializePlaybackController: created MediaPlayerHolder");
        mAudioPlayerManager.setPlaybackInfoListener(new AudioPlayerCallBack() {
            @Override
            public void onLogUpdated(String message) {
                if (mTextDebug != null) {
                    mTextDebug.append(message);
                    mTextDebug.append("\n");
                    // Moves the scrollContainer focus to the end.
                    mScrollContainer.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                }
                LogHelper.Log("" + message);
            }

            @Override
            public void onDurationChanged(int duration) {
                mSeekbarAudio.setMax(duration);
                LogHelper.Log(String.format("setPlaybackDuration: setMax(%d)", duration));
            }

            @Override
            public void onPositionChanged(int position) {
                if (!mUserIsSeeking) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mSeekbarAudio.setProgress(position, true);
                    }
                    LogHelper.Log(String.format("setPlaybackPosition: setProgress(%d)", position));
                }
            }

            @Override
            public void onStateChanged(int state) {
                String stateToString = LogHelper.convertStateToString(state);
                onLogUpdated(String.format("onStateChanged(%s)", stateToString));
            }

            @Override
            public void onPlaybackCompleted() {

            }
        });
        mIAudioPlayer = mAudioPlayerManager;
        LogHelper.Log("initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mIAudioPlayer.seekTo(userSelectedPosition);
                    }
                });
    }


}
