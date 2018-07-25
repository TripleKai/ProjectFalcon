package com.example.kailashsaravanan.projectfalcon;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ImageViewHolder> {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Context mContext;
    private List<Video> mVideos;

    public VideoAdapter(Context context, List<Video> videos){
        mContext = context;
        mVideos = videos;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVideoUrl;
        TextView textViewVidDate;
        TextView textViewVidSize;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewVideoUrl = itemView.findViewById(R.id.video_url);
            textViewVidDate = itemView.findViewById(R.id.vid_date);
            textViewVidSize = itemView.findViewById(R.id.vid_size);
        }

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // May need to change this line below
        View v = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@Nullable ImageViewHolder holder, int position) {
        final Video video = mVideos.get(position);
        holder.textViewVideoUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayer(video);
            }
        });
        holder.textViewVideoUrl.setText(video.getVideoUrl());
        holder.textViewVidDate.setText(video.getDateTime());
        holder.textViewVidSize.setText(video.getSize() + " Bytes");
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
