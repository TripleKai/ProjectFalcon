package com.example.kailashsaravanan.projectfalcon;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        mVideoListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mVideos = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        final Video video = new Video();
                        final DataSnapshot snapshot = singleSnapshot;
                        mStorageRef.child("videos/" + singleSnapshot.getKey() + ".mp4").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                SimpleDateFormat sfd = new SimpleDateFormat("MM/dd/yyyy: HH:mm:ss", Locale.US);
                                String dateTime = sfd.format(storageMetadata.getCreationTimeMillis());
                                String vidSize = humanReadableByteCount(storageMetadata.getSizeBytes(), true);
                                video.setSize(vidSize);
                                video.setDateTime(dateTime);
                                video.setName(snapshot.getKey());
                            }
                        });
                        mStorageRef.child("videos/" + singleSnapshot.getKey() + ".mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                video.setVideoUrl(uri.toString());
                                mVideos.add(0, video);
                                mVideoAdapter = new VideoAdapter(getActivity(), mVideos);
                                mRecyclerView.setAdapter(mVideoAdapter);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("Videos").addValueEventListener(mVideoListener);
        return view;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
