package com.group5.preppal.ui.video_call;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.group5.preppal.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    private final android.os.Handler timeHandler = new android.os.Handler();
    private Runnable checkTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        // --- Check thời gian hợp lệ ---
        checkIsEnableAccess();

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

    private void checkIsEnableAccess() {
        String role = getIntent().getStringExtra("role");
        Log.d("ccccccc", role);
        if (role.equals("Student")) {
            Log.d("bbbbb", "bbbbbbbbbbbbbbbbbb");
            String startTimeStr = getIntent().getStringExtra("startTime"); // ví dụ: "12:30:00"
            if (startTimeStr != null) {
                try {
                    // Parse giờ phút giây từ startTime
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    Date startTime = sdf.parse(startTimeStr);

                    // Lấy thời gian hiện tại (chỉ lấy giờ phút giây của hôm nay)
                    Calendar now = Calendar.getInstance();
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startTime);
                    startCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
                    startCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
                    startCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));

                    long startMillis = startCal.getTimeInMillis();

                    checkTimeRunnable = new Runnable() {
                        @Override
                        public void run() {
                            long currentMillis = System.currentTimeMillis();
                            long diffMinutes = (currentMillis - startMillis) / (60 * 1000);

                            if (diffMinutes > 30) {
                                Toast.makeText(VideoCallActivity.this, "Bạn đã hết thời gian để truy cập vào cuộc trò chuyện.", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                // Check lại sau 1 phút
                                timeHandler.postDelayed(this, 60 * 1000);
                            }
                        }
                    };

                    timeHandler.post(checkTimeRunnable);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkTimeRunnable != null) {
            timeHandler.removeCallbacks(checkTimeRunnable);
        }
    }
}