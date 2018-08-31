package com.miles.foodtruck.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.miles.foodtruck.Controller.ModifyTrackingListener;
import com.miles.foodtruck.Controller.OnLongClickListener;
import com.miles.foodtruck.Model.Abstract.AbstractTrackable;
import com.miles.foodtruck.Model.Abstract.AbstractTracking;
import com.miles.foodtruck.R;
import com.miles.foodtruck.Util.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Observer{


    private List<AbstractTrackable> mTrackables;
    private List<AbstractTracking> mTrackings;

    private Context mContext;

    //Constructor, accept data set
    public RecyclerAdapter(List<AbstractTrackable> trucks, List<AbstractTracking> trackings,Context context){
        mTrackables = trucks;
        mContext = context;
        mTrackings = trackings;
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
        if (mTrackings == null){
            return mTrackables.size();
        }
        else{
            return mTrackings.size();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateTrackings((ArrayList<AbstractTracking>) arg);
        this.notifyDataSetChanged();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        private Button mButton;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.list_text);
            mButton = itemView.findViewById(R.id.list_button);
            this.view = itemView;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (mTrackings == null) {
            viewHolder.mTextView.setText(mTrackables.get(i).getOutPutString());
            viewHolder.mButton.setText(R.string.add_button);
            viewHolder.mButton.setOnClickListener(new ModifyTrackingListener(mTrackables.get(i), null, mContext));
        }
        else
        {
            viewHolder.mTextView.setText(mTrackings.get(i).getOutPutString());
            viewHolder.mButton.setText(R.string.edit_button);
            viewHolder.mButton.setOnClickListener(new ModifyTrackingListener(null,mTrackings.get(i),mContext));
            viewHolder.view.setOnLongClickListener(new OnLongClickListener("",mTrackings.get(i),this));

        }

    }

    public void updateTrackables(ArrayList<AbstractTrackable> foodTrucks){
        this.mTrackables = foodTrucks;
    }
    private void updateTrackings(ArrayList<AbstractTracking> trackings){
        this.mTrackings = trackings;

    }

}
