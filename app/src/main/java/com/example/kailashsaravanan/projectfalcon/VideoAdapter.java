package com.example.kailashsaravanan.projectfalcon;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Context mContext;
    private List<Video> mVideos;

    public VideoAdapter(Context context, List<Video> videos){
        mContext = context;
        mVideos = videos;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVideoUrl;
        TextView textViewVidDate;
        TextView textViewVidSize;
        Button btnVidLocation;
        ImageButton btnShare;

        public VideoViewHolder(View itemView) {
            super(itemView);
            textViewVideoUrl = itemView.findViewById(R.id.video_url);
            textViewVidDate = itemView.findViewById(R.id.vid_date);
            textViewVidSize = itemView.findViewById(R.id.vid_size);
            btnVidLocation = itemView.findViewById(R.id.btn_vid_location);
            btnShare = itemView.findViewById(R.id.btn_share_vid);
        }

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // May need to change this line below
        View v = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@Nullable VideoViewHolder holder, int position) {
        final Video video = mVideos.get(position);
        holder.textViewVideoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayer(video);
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/mp4");
//                String shareSub = "Falcon Alert";
//                String shareText = "FALCON ALERT: \n"+ "Incident Time - " + picture.getDateTime() + "\n" + "Location - " + picture.getLocation() + "\n";
                intent.putExtra(Intent.EXTRA_STREAM, video.getVideoUri());
//                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                mContext.startActivity(Intent.createChooser(intent, "Share photo using"));
            }
        });
        holder.textViewVideoUrl.setText(video.getVideoUrl());
        holder.textViewVidDate.setText(video.getDateTime());
        holder.textViewVidSize.setText(video.getSize());
        holder.btnVidLocation.setText(video.getLocation());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void displayer(Video video){
        Intent intent = new Intent(mContext, DisplayVideoActivity.class);
        intent.putExtra(EXTRA_MESSAGE, video.getVideoUrl());
        mContext.startActivity(intent);
    }
}
