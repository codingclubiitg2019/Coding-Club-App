package com.example.codingclub;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class AboutusAdapter extends RecyclerView.Adapter<AboutusAdapter.AboutusViewHolder>
{
    private ArrayList<Team> mDataset;
    private ArrayList<String> memberNames;

    public static class AboutusViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, positionText;
        ImageView memberImage;
        View view;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                memberImage.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        public AboutusViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.display_name);
            positionText = view.findViewById(R.id.display_position);
            memberImage = view.findViewById(R.id.imageView2);
            this.view = view;
        }

        public void bindImage(String url){
            Picasso.get().load(url).tag(view).into(target);
        }
    }

    public AboutusAdapter(ArrayList<Team> myDataset, ArrayList<String> memberNames) {
        this.mDataset = myDataset;
        this.memberNames = memberNames;
    }

    @NonNull
    @Override
    public AboutusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_aboutus_card, parent, false);
        return new AboutusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AboutusViewHolder holder, int position) {
        Team team = mDataset.get(position);
        String memberName = memberNames.get(position);

        holder.positionText.setText(team.getPosition());
        holder.nameText.setText(memberName);

        if(team.getImage() != ""){
            holder.bindImage(team.getImage());
        }
    }

    public void setItems(ArrayList<Team> data, ArrayList<String> memberNames) {
        this.mDataset = data;
        this.memberNames = memberNames;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
