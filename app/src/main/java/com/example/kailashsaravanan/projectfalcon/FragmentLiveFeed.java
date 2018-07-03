package com.example.kailashsaravanan.projectfalcon;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class FragmentLiveFeed extends Fragment {
    private static final String TAG = "FragmentLiveFeed";

    public FragmentLiveFeed(){

    }

//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        try {
//            String link = "http://techslides.com/demos/sample-videos/small.mp4";
//            VideoView videoView = view.findViewById(R.id.myVideoView);
////            final ProgressBar pbLoading = (ProgressBar)  findViewById(R.id.pbVideoLoading);
////            pbLoading.setVisibility(View.VISIBLE);
//            MediaController mediaController = new MediaController(getActivity());
//            mediaController.setAnchorView(videoView);
//            Uri video = Uri.parse(link);
////            videoView.setMediaController(mediaController);
//            videoView.setMediaController(null);
//            videoView.setVideoURI(video);
//            videoView.start();
////            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////                @Override
////                public void onPrepared(MediaPlayer mp) {
////                    pbLoading.setVisibility(View.GONE);
////                }
////            });
//        } catch (Exception e) {
//            // TODO: handle exception
//            Log.i(TAG, e.toString());
//            Toast.makeText(getActivity(), "Error connecting", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_feed, container, false);

        try {
            String link = "http://techslides.com/demos/sample-videos/small.mp4";
            VideoView videoView = view.findViewById(R.id.myVideoView);
//            final ProgressBar pbLoading = (ProgressBar)  findViewById(R.id.pbVideoLoading);
//            pbLoading.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(getActivity());
            mediaController.setAnchorView(videoView);
            Uri video = Uri.parse(link);
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
            // TODO: handle exception
            Log.i(TAG, e.toString());
            Toast.makeText(getActivity(), "Error connecting", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
}
