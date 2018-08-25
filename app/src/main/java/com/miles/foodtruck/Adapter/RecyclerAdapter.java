package com.miles.foodtruck.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miles.foodtruck.Model.FoodTruck;
import com.miles.foodtruck.R;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    private List<FoodTruck> mDataSet;

    //Constructor, accept data set
    public RecyclerAdapter(List<FoodTruck> trucks){
        mDataSet = trucks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Load the layout
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent,false);
        return new ViewHolder(item);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.truck_name);
            mImageView = itemView.findViewById(R.id.truck_logo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mDataSet.get(i).getName());
        viewHolder.mImageView.setImageResource(R.drawable.food_truck_logo);
        viewHolder.mImageView.setContentDescription(mDataSet.get(i).getName());
    }

}
