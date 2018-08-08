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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Context mContext;
    private List<Picture> mPictures;

    public ImageAdapter(Context context, List<Picture> pictures){
        mContext = context;
        mPictures = pictures;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewPicDate;
        TextView textViewPicSize;
        Button btnLocation;
        ImageButton btnShare;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewPicDate = itemView.findViewById(R.id.pic_date);
            textViewPicSize = itemView.findViewById(R.id.pic_size);
            btnLocation = itemView.findViewById(R.id.btn_pic_location);
            btnShare = itemView.findViewById(R.id.btn_share_pic);
        }

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // May need to change this line below
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@Nullable ImageViewHolder holder, int position) {
        final Picture picture = mPictures.get(position);
        Picasso.get()
                .load(picture.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayer(picture);
            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, picture.getImageUri());
                mContext.startActivity(Intent.createChooser(intent, "Share photo using"));
            }
        });
        holder.textViewPicDate.setText(picture.getDateTime());
        holder.textViewPicSize.setText(picture.getSize());
        holder.btnLocation.setText(picture.getLocation());
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public void displayer(Picture picture){
        Intent intent = new Intent(mContext, DisplayImageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, picture.getImageUrl());
        mContext.startActivity(intent);
    }
}
