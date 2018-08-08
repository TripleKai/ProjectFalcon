package com.example.kailashsaravanan.projectfalcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class FragmentHistory extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FragmentHistory";
    private SwipeRefreshLayout swipeLayout;

    private String mString;
    private Button addButton;

    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private RecyclerView mRecyclerView;
    private HistoryAdapter mHistoryAdapter;
    private List<HistoryItem> mItems;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    private FirebaseStorage mStorage = FirebaseStorage.getInstance("gs://project-falcon-bucket");
    private StorageReference mStorageRef = mStorage.getReference();

    private ValueEventListener mHistoryListener;

    public FragmentHistory(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        swipeLayout = view.findViewById(R.id.swipe_history);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorBackgroundLight));
        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent));

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mHistoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItems = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        final HistoryItem historyItem = new HistoryItem();
                        String[] items = singleSnapshot.getValue(String.class).split(":");
                        historyItem.setLocation(items[4]);
                        historyItem.setMotionCount(items[0]);
                        mStorageRef.child("pictures/" + singleSnapshot.getKey() + ".jpg").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {
                                SimpleDateFormat sfd = new SimpleDateFormat("MM/dd/yyyy: HH:mm:ss", Locale.US);
                                String dateTime = sfd.format(storageMetadata.getCreationTimeMillis());
                                historyItem.setDateTime(dateTime);
                                mItems.add(0, historyItem);
                                mHistoryAdapter = new HistoryAdapter(getActivity(), mItems);
                                mRecyclerView.setAdapter(mHistoryAdapter);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.child("Captured Motion").addValueEventListener(mHistoryListener);
        return view;
    }

    @Override
    public void onRefresh() {
        mRef.child("Captured Motion").addValueEventListener(mHistoryListener);
        swipeLayout.setRefreshing(false);
    }
}
