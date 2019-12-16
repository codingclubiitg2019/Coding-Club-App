package com.example.codingclub;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Event> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public MyViewHolder(View v) {
            super(v);
            textViewTitle = v.findViewById(R.id.textViewTitle);
            textViewShortDesc = v.findViewById(R.id.textViewShortDesc);
            textViewRating = v.findViewById(R.id.textViewRating);
            textViewPrice = v.findViewById(R.id.textViewPrice);
            imageView = v.findViewById(R.id.imageView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Event> myDataset) {
        this.mDataset = myDataset;
        Log.d("logsize", String.valueOf(mDataset.size()));
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
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.my_text_view, parent, false);
//
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event event = mDataset.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(event.getName());
        holder.textViewShortDesc.setText(event.getDetails());
        holder.textViewRating.setText(String.valueOf(event.getVenue()));
        holder.textViewPrice.setText(String.valueOf(event.getTime()));

//        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

//        holder.CardView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("logsize", String.valueOf(mDataset.size()));
        return mDataset.size();
    }
}
