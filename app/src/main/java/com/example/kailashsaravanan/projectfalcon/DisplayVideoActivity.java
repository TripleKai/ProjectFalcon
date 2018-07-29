package com.example.kailashsaravanan.projectfalcon;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class DisplayVideoActivity extends Activity{
    private static final String TAG = "DisplayVideoActivity";

    public DisplayVideoActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String url = intent.getStringExtra(VideoAdapter.EXTRA_MESSAGE);

        try {
            VideoView videoView = findViewById(R.id.vid);
//            final ProgressBar pbLoading = (ProgressBar)  findViewById(R.id.pbVideoLoading);
//            pbLoading.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(url);
//            videoView.setMediaController(mediaController);
            videoView.setMediaController(null);
            videoView.setVideoURI(video);
            videoView.start();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    pbLoading.setVisibility(View.GONE);
//                }
//            });
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }
}
