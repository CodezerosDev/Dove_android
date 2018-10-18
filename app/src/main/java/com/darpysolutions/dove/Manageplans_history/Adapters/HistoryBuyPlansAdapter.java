package com.darpysolutions.dove.Manageplans_history.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.darpysolutions.dove.R;

import java.util.ArrayList;

public class HistoryBuyPlansAdapter extends RecyclerView.Adapter<HistoryBuyPlansAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<String> drBeanArrayList;

    public HistoryBuyPlansAdapter(Context context, ArrayList<String> doctorBeanArrayList) {
        mContext = context;
        drBeanArrayList = doctorBeanArrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_buyhistory, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistoryDialogue();
            }
        });
    }

    private void showHistoryDialogue() {
        final Dialog dialog = new Dialog(mContext, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_history);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        dialog.show();

        TextView btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 20;
//        if (drBeanArrayList == null) return 0;
//        return drBeanArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mainView;
//        ImageView ivProfile;
//        TextView tvDrName, tvSpecialization;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
//            tvDrName = mainView.findViewById(R.id.tvDrName);
//            tvSpecialization = mainView.findViewById(R.id.tvSpecialization);
//            ivProfile = mainView.findViewById(R.id.ivProfile);
        }
    }

}
