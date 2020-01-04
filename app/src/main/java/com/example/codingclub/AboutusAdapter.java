package com.example.codingclub;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AboutusAdapter extends RecyclerView.Adapter<AboutusAdapter.MyViewHolder> {
    List<Team> mDataset;
    List<String> memberNames;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView nameText, positionText;
        ImageView memberImg;
        View v;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                memberImg.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        public MyViewHolder(View v) {
            super(v);
            nameText = v.findViewById(R.id.display_name);
            positionText = v.findViewById(R.id.display_position);
            memberImg = v.findViewById(R.id.imageView2);
            this.v = v;
        }

        public void bindImage(String url){
            Picasso.get().load(url).tag(v).into(target);
        }
    }

    public AboutusAdapter(List<Team> myDataset, List<String> memberNames) {
        this.mDataset = myDataset;
        this.memberNames = memberNames;
    }

    @Override
    public AboutusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_aboutus_card, parent, false);
        AboutusAdapter.MyViewHolder viewHolder =  new AboutusAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AboutusAdapter.MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Team team = mDataset.get(position);
        String memberName = memberNames.get(position);

        //binding the data with the viewholder views

        holder.positionText.setText(team.getPosition());
        holder.nameText.setText(memberName);
    }

    public void setItems(List<Team> data, List<String> memberNames) {
        this.mDataset = data;
        this.memberNames = memberNames;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
