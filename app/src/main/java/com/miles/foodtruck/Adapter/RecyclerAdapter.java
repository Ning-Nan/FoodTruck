package com.miles.foodtruck.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miles.foodtruck.Controller.AddTrackingBtnListener;
import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    private List<AbstractTrackable> mDataSet;

    //Constructor, accept data set
    public RecyclerAdapter(List<AbstractTrackable> trucks){
        mDataSet = trucks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Load the layout
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,false);
        return new ViewHolder(item);
    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        private Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_text);
            mButton = itemView.findViewById(R.id.list_button);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.mTextView.setText(mDataSet.get(i).getOutPutString());
        viewHolder.mButton.setText(R.string.add_button);
        viewHolder.mButton.setOnClickListener(new AddTrackingBtnListener(mDataSet.get(i), null));
    }

    public void update(ArrayList<AbstractTrackable> foodTrucks){
        this.mDataSet = foodTrucks;
    }

}
