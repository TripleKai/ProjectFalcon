package com.example.kailashsaravanan.projectfalconofficial;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class FragmentGallery extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FragmentGallery";
    private SwipeRefreshLayout swipeLayout;

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private List<Picture> mPictures;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://project-falcon-bucket");
    private StorageReference mStorageRef = mStorage.getReference();

    private ValueEventListener mPictureListener;

    public FragmentGallery() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        swipeLayout = view.findViewById(R.id.swipe_gallery);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorBackgroundLight));
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mPictureListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPictures = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        final Picture picture = new Picture();
                        String[] items = singleSnapshot.getValue(String.class).split(":");
                        picture.setLocation(items[4]);
                        mStorageRef.child("pictures/" + singleSnapshot.getKey() + ".jpg").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                SimpleDateFormat sfd = new SimpleDateFormat("MM/dd/yyyy: HH:mm:ss", Locale.US);
                                String dateTime = sfd.format(storageMetadata.getCreationTimeMillis());
                                String picSize =  humanReadableByteCount(storageMetadata.getSizeBytes(), true);
                                picture.setSize(picSize);
                                picture.setDateTime(dateTime);
                            }
                        });
                        mStorageRef.child("pictures/" + singleSnapshot.getKey() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                picture.setImageUri(uri);
                                picture.setImageUrl(uri.toString());
                                mPictures.add(0, picture);
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
        return view;
    }

    @Override
    public void onRefresh() {
        mRef.child("Captured Motion").addValueEventListener(mPictureListener);
        swipeLayout.setRefreshing(false);
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
