package com.example.kailashsaravanan.projectfalcon;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.google.firebase.database.DatabaseReference;

public class FragmentGallery extends Fragment {
    private static final String TAG = "FragmentGallery";

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private List<Picture> mPictures;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://project-falcon-bucket");
    private StorageReference mStorageRef = mStorage.getReference();

    private ValueEventListener mPictureListener;

    public FragmentGallery(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mPictureListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPictures = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        mStorageRef.child(singleSnapshot.getKey() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mPictures.add(new Picture(uri.toString()));

                                mImageAdapter = new ImageAdapter(getActivity(), mPictures);
                                mRecyclerView.setAdapter(mImageAdapter);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("Captured Motion").addValueEventListener(mPictureListener);
        
        FloatingActionButton mDownloadButton = view.findViewById(R.id.download);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadImages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

//        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

//        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/20180617_15h51m50s014584.jpg?alt=media&token=32e6fa34-feab-4965-a1e4-8aeb3b80facd").into((ImageView) view.findViewById(R.id.image_view));

        return view;
    }

    public void downloadImages() throws IOException {
        StorageReference picRef = mStorage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/20180707_12h29m30s436982.jpg?alt=media&token=9fa6ee41-5c42-41b6-beac-70045d156434");
        File localFile = File.createTempFile("images", "jpg");

        picRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(), "Downloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateGallery(){
        mStorageRef.child("20180625_17h58m19s710377.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            String s = "";
            @Override
            public void onSuccess(Uri uri) {
                s = uri.toString();
//                mImageURI = uri;
//                Toast.makeText(getActivity(), mImageURI, Toast.LENGTH_SHORT).show();
                mPictures.add(new Picture(s));
            }
        });
    }
}
