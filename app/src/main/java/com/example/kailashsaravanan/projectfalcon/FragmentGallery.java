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

import java.util.ArrayList;
import java.util.List;

//import com.google.firebase.database.DatabaseReference;

public class FragmentGallery extends Fragment {
    private static final String TAG = "FragmentGallery";

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private List<Picture> mPictures;
//    private DatabaseReference mDatabaseReference;

    public FragmentGallery(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mPictures = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mPictures.add(new Picture("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/20180617_15h51m50s014584.jpg?alt=media&token=32e6fa34-feab-4965-a1e4-8aeb3b80facd"));
        mPictures.add(new Picture("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/20180617_15h51m47s218193.jpg?alt=media&token=f94d2396-6b3b-443e-9899-bd308dfe2a05"));
        mPictures.add(new Picture("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/misc%2F?alt=media&token=59ec336d-24dc-4c6d-85e5-f2a6834f0d2a"));

//        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/project-falcon-bucket/o/20180617_15h51m50s014584.jpg?alt=media&token=32e6fa34-feab-4965-a1e4-8aeb3b80facd").into((ImageView) view.findViewById(R.id.image_view));

        mImageAdapter = new ImageAdapter(getActivity(), mPictures);
        mRecyclerView.setAdapter(mImageAdapter);
        return view;
    }
}
