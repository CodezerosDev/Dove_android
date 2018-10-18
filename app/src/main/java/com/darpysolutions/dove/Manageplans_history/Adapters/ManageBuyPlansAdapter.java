package com.darpysolutions.dove.Manageplans_history.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darpysolutions.dove.Manageplans_history.BuyPlanDetailsActivity;
import com.darpysolutions.dove.R;

import java.util.ArrayList;

public class ManageBuyPlansAdapter extends RecyclerView.Adapter<ManageBuyPlansAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<String> drBeanArrayList;

    public ManageBuyPlansAdapter(Context context, ArrayList<String> doctorBeanArrayList) {
        mContext = context;
        drBeanArrayList = doctorBeanArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_managebuyplans, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, BuyPlanDetailsActivity.class);
                mContext.startActivity(myIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
       return 20;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mainView;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
        }
    }

}
