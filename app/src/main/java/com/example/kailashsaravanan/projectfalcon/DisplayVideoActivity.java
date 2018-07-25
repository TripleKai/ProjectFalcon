package com.example.kailashsaravanan.projectfalcon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

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

        TextView vidUrl = findViewById(R.id.display_vid_url);
        vidUrl.setText(url);
    }
}
