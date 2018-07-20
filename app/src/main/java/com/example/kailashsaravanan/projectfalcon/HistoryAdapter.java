package com.example.kailashsaravanan.projectfalcon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TextViewHolder> {
    private Context mContext;
    private List<HistoryItem> mItems;

    public HistoryAdapter(Context context, List<HistoryItem> items){
        mContext = context;
        mItems = items;
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.history_item);
        }

    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // May need to change this line below
        View v = LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false);
        return new TextViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@Nullable TextViewHolder holder, int position) {
        HistoryItem historyItem = mItems.get(position);
        holder.textView.setText(historyItem.getmTest());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
