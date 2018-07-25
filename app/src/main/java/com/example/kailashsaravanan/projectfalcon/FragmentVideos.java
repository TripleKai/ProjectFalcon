package com.example.kailashsaravanan.projectfalcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FragmentVideos extends Fragment {
    private static final String TAG = "FragmentVideos";

    private RecyclerView mRecyclerView;
    private VideoAdapter mVideoAdapter;
    private List<Video> mVideos;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://project-falcon-bucket");
    private StorageReference mStorageRef = mStorage.getReference();

    private ValueEventListener mVideoListener;

    public FragmentVideos(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_videos);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mVideos = new ArrayList<>();
        Video video = new Video();
        video.setDateTime("7/24/2018");
        video.setSize("4 MB");
        video.setVideoUrl("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/output.avi?alt=media&token=bdec171d-c94a-4589-817e-1ba4f707af80");
        mVideos.add(video);
        mVideoAdapter = new VideoAdapter(getActivity(), mVideos);
        mRecyclerView.setAdapter(mVideoAdapter);

        return view;
    }
}
