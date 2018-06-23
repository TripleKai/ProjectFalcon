package com.example.kailashsaravanan.projectfalcon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FragmentHistory extends Fragment {
    private static final String TAG = "FragmentHistory";
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;

    private String mString;

    public FragmentHistory(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

//        mListView = view.findViewById(R.id.history_list);
//
//        Button button = view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(getArguments() != null){
//                    mString = getArguments().getString("Test");
//                    updateUI();
//                }
//                else if(getArguments() == null){
//                    Toast.makeText(getContext(), "Null", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return view;
    }

//    public void updateUI(){
//        ArrayList<String> taskList = new ArrayList<>();
//
//        taskList.add(mString);
//        taskList.add(mString);
//        taskList.add(mString);
//        if (mAdapter == null) {
//            mAdapter = new ArrayAdapter<>(getContext(),
//                    R.layout.history_item,
//                    R.id.task_title,
//                    taskList);
//            mListView.setAdapter(mAdapter);
//        } else {
//            mAdapter.clear();
//            mAdapter.addAll(taskList);
//            mAdapter.notifyDataSetChanged();
//        }
//    }
}
