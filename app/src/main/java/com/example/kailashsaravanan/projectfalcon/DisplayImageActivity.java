package com.example.kailashsaravanan.projectfalcon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DisplayImageActivity extends Activity{
    private static final String TAG = "DisplayImageActivity";

    public DisplayImageActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);

        ImageView pic = findViewById(R.id.pic);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String url = intent.getStringExtra(ImageAdapter.EXTRA_MESSAGE);

        Picasso.get()
                .load(url)
                .fit()
                .centerInside()
                .into(pic);
    }
}
