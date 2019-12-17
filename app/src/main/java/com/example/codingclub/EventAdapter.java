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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    List<Event> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView nameText, detailsText, venueText, timeText, dateText;
        ImageView posterImg;
        View v;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                posterImg.setImageBitmap(bitmap);
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
            detailsText = v.findViewById(R.id.diplay_details);
            venueText = v.findViewById(R.id.display_venue);
            timeText = v.findViewById(R.id.display_time);
            dateText = v.findViewById(R.id.display_date);
            posterImg = v.findViewById(R.id.imageView2);
            this.v = v;
        }

        public void bindImage(String url){
            Picasso.get().load(url).tag(v).into(target);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(List<Event> myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_event_card, parent, false);
        MyViewHolder viewHolder =  new MyViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event event = mDataset.get(position);

        //binding the data with the viewholder views
        holder.nameText.setText(event.getName());
        holder.detailsText.setText(event.getDetails());
        holder.venueText.setText(event.getVenue());
        holder.timeText.setText(event.getTime());
        holder.dateText.setText(event.getDate());

        if(event.getImage() != ""){
            holder.bindImage(event.getImage());
        }
    }

    public void setItems(List<Event> data) {
        this.mDataset = data;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
