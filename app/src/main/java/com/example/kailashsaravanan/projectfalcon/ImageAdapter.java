package com.example.kailashsaravanan.projectfalcon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
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

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewPicDate = itemView.findViewById(R.id.pic_date);
            textViewPicSize = itemView.findViewById(R.id.pic_size);
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
        Picture picture = mPictures.get(position);
        Picasso.get()
                .load(picture.getImageUrl())
                .fit()
                .centerInside()
                .into(holder.imageView);
        holder.textViewPicDate.setText(picture.getDateTime());
        holder.textViewPicSize.setText(picture.getSize() + " Bytes");
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }
}
