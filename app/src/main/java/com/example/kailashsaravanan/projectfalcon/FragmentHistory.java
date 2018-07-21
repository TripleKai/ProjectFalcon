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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHistory extends Fragment {
    private static final String TAG = "FragmentHistory";
    private String mString;
    private Button addButton;

    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;

    private RecyclerView mRecyclerView;
    private HistoryAdapter mHistoryAdapter;
    private List<HistoryItem> mItems;

    final public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mRef = mDatabase.getReference();

    private ValueEventListener mHistoryListener;

    public FragmentHistory(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mHistoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItems = new ArrayList<>();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        String key = singleSnapshot.getKey();
                        String date = key.substring(4,6) + "/"
                                + key.substring(6,8) + "/"
                                + key.substring(0,4);
                        mItems.add(new HistoryItem(date));
                        mHistoryAdapter = new HistoryAdapter(getActivity(), mItems);
                        mRecyclerView.setAdapter(mHistoryAdapter);
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

//    public void updateUI(){
//        ArrayList<String> taskList = new ArrayList<>();
//
//        taskList.add(mString);
//        taskList.add(mString);
//        taskList.add(mString);
//        if (mArrayAdapter == null) {
//            mArrayAdapter = new ArrayAdapter<>(getActivity(),
//                    R.layout.history_item,
//                    R.id.task_title,
//                    taskList);
//            mListView.setAdapter(mArrayAdapter);
//        } else {
//            mArrayAdapter.clear();
//            mArrayAdapter.addAll(taskList);
//            mArrayAdapter.notifyDataSetChanged();
//        }
//    }
}
