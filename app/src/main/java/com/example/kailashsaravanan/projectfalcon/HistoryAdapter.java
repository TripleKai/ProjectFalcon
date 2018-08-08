package com.example.kailashsaravanan.projectfalcon;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        TextView dateTime;
        TextView motionCount;
        TextView location;
        ImageButton btnShare;

        public TextViewHolder(View itemView) {
            super(itemView);
            dateTime = itemView.findViewById(R.id.history_dateTime);
            motionCount = itemView.findViewById(R.id.history_motion);
            location = itemView.findViewById(R.id.history_location);
            btnShare = itemView.findViewById(R.id.btn_share_history);
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
        final HistoryItem historyItem = mItems.get(position);
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareSub = "FALCON ALERT";
                String shareText = "FALCON ALERT: \n"+ "Incident Time - " + historyItem.getDateTime() + "\n" + "Location - " + historyItem.getLocation();
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                mContext.startActivity(Intent.createChooser(intent, "Share incident using"));
            }
        });
        holder.dateTime.setText(historyItem.getDateTime());
        String historyMotion = "<b>" + "Seconds of Motion: " + "</b> " + historyItem.getMotionCount();
        holder.motionCount.setText(Html.fromHtml(historyMotion, 0));
        String historyLocation = "<b>" + "Location: " + "</b> " + historyItem.getLocation();
        holder.location.setText(Html.fromHtml(historyLocation, 0));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
