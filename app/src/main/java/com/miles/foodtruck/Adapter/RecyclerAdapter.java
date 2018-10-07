package com.miles.foodtruck.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.miles.foodtruck.controller.AddOrEditTrackingListener;
import com.miles.foodtruck.controller.OnLongClickListener;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.model.abstracts.AbstractTracking;
import com.miles.foodtruck.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
    Reusable Recycler Adapter for both Tracking and Trackable List.
    Depends on different parameterS passed in, perform different operations.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements Observer{

    private List<AbstractTrackable> mTrackables;
    private List<AbstractTracking> mTrackings;
    private Context mContext;
    private Activity activity;

    public RecyclerAdapter(List<AbstractTrackable> trucks, List<AbstractTracking> trackings,
                           Activity activity){
        mTrackables = trucks;
        mContext = activity.getApplicationContext();
        mTrackings = trackings;
        this.activity = activity;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        //Set layout for per list item.
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,
                parent,false);
        return new ViewHolder(item);
    }



    @Override
    public int getItemCount() {

        if (mTrackings == null) {
            return mTrackables.size();
        }
        else{
            return mTrackings.size();
        }
    }


    /*
    To update tracking list using Observer.
     */
    @Override
    public void update(Observable o, Object arg) {

        updateTrackings((ArrayList<AbstractTracking>) arg);
        this.notifyDataSetChanged();

    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        private Button mButton;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            //In the list, there is one button and one test for per item.
            mTextView = (TextView) itemView.findViewById(R.id.list_text);
            mButton = (Button) itemView.findViewById(R.id.list_button);
            this.view = itemView;

        }
    }

    /*
    Different List has different content, also different Listener.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Trackings not passed in. So this adapter is operating trackable list.
        if (mTrackings == null) {
            viewHolder.mTextView.setText(mTrackables.get(i).getOutPutString());
            viewHolder.mButton.setText(R.string.add_button);

            //pass trackables not trackings. So the listener know what list they are operating.
            viewHolder.mButton.setOnClickListener(new AddOrEditTrackingListener(mTrackables.get(i),
                    null, mContext));
            viewHolder.view.setOnLongClickListener(new OnLongClickListener(Integer
                    .toString(mTrackables.get(i).getId()),null,activity));
        }
        //Operating tracking list
        else
        {
            viewHolder.mTextView.setText(mTrackings.get(i).getOutPutString());
            viewHolder.mButton.setText(R.string.edit_button);
            viewHolder.mButton.setOnClickListener(new AddOrEditTrackingListener(null,
                    mTrackings.get(i),mContext));
            viewHolder.view.setOnLongClickListener(new OnLongClickListener(
                    "",mTrackings.get(i),activity));
        }

    }

    /*
    Help to update the list.
    Then update the adapter and UI.
     */
    public void updateTrackables(ArrayList<AbstractTrackable> foodTrucks){
        this.mTrackables = foodTrucks;
    }
    public void updateTrackings(ArrayList<AbstractTracking> trackings){
        this.mTrackings = trackings;

    }

}
