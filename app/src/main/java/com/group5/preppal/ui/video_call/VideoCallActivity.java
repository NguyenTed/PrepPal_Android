package com.group5.preppal.ui.video_call;

import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.group5.preppal.R;

import io.agora.rtc2.ChannelMediaOptions;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.video.VideoCanvas;
import io.agora.rtc2.video.VideoEncoderConfiguration;

public class VideoCallActivity extends AppCompatActivity {

    private static final String APP_ID = "30af67d401074cc49f3008f024e7e925";
    private static final String TOKEN = null;
    private static final String CHANNEL_NAME = "speakingQuizChannel";

    private RtcEngine rtcEngine;
    private SurfaceView localView, remoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 22);

        initializeAgoraEngine();

        Button btnEnd = findViewById(R.id.btn_end_call);
        btnEnd.setOnClickListener(v -> {
            rtcEngine.leaveChannel();
            RtcEngine.destroy();
            rtcEngine = null;
            finish();
        });
    }

    private void initializeAgoraEngine() {
        try {
            rtcEngine = RtcEngine.create(getBaseContext(), APP_ID, new IRtcEngineEventHandler() {
                @Override
                public void onUserJoined(int uid, int elapsed) {
                    runOnUiThread(() -> setupRemoteVideo(uid));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Error initializing RTC engine: " + e.getMessage());
        }

        setupLocalVideo();
        joinChannel();
    }

    private void setupLocalVideo() {
        FrameLayout localContainer = findViewById(R.id.local_video_view_container);
        localView = new SurfaceView(getBaseContext());
        localContainer.addView(localView);
        localView.setZOrderMediaOverlay(true);
        rtcEngine.setupLocalVideo(new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        rtcEngine.startPreview();
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout remoteContainer = findViewById(R.id.remote_video_view_container);
        remoteView = new SurfaceView(getBaseContext());
        remoteContainer.addView(remoteView);
        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    private void joinChannel() {
        ChannelMediaOptions options = new ChannelMediaOptions();
        options.clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER;
        options.channelProfile = io.agora.rtc2.Constants.CHANNEL_PROFILE_COMMUNICATION;
        options.publishCameraTrack = true;
        options.publishMicrophoneTrack = true;

        rtcEngine.enableVideo();
        rtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
        ));

        rtcEngine.joinChannel(TOKEN, CHANNEL_NAME, 0, options);
    }
}