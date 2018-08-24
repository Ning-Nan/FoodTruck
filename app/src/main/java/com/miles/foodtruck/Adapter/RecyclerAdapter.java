package com.miles.foodtruck.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miles.foodtruck.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


    private List<String> mDataSet;

    //Constructor, accept data set
    public RecyclerAdapter(List<String> data){
        mDataSet = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Load the layout
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent,false);
        ViewHolder vh = new ViewHolder(item);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mDataSet.get(i));

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            //由于itemView是item的布局文件，我们需要的是里面的textView，因此利用itemView.findViewById获
            //取里面的textView实例，后面通过onBindViewHolder方法能直接填充数据到每一个textView了
            mTextView = itemView.findViewById(R.id.truck_name);
        }
    }
}
