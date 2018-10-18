package com.darpysolutions.dove.PurchaseData.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darpysolutions.dove.PurchaseData.ListWifiActivity;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.wifi.ServerBean;
import com.darpysolutions.dove.wifi.WifiDataBean;

import java.util.ArrayList;

public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<WifiDataBean> wifiList;
    ListWifiActivity listWifiActivity;

    public WifiListAdapter(ListWifiActivity context, ArrayList<WifiDataBean> wList) {
        mContext = context;
        wifiList = wList;
        listWifiActivity = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_buyhistory, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        WifiDataBean serverBean = wifiList.get(position);
        holder.tvWifiname.setText("Dove Network :");
        final String wifiDetails[] = serverBean.getDetailString();
        holder.tvDetails.setText(wifiDetails[1]);
        holder.tvPrice.setText(serverBean.getPrice() + " D");
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listWifiActivity.ConnectTo(listWifiActivity.mScanResults.get(position),wifiDetails[0]);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (wifiList == null) return 0;
        return wifiList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        TextView tvWifiname, tvDetails, tvPrice;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
            tvWifiname = mainView.findViewById(R.id.tvWifiname);
            tvDetails = mainView.findViewById(R.id.tvDetails);
            tvPrice = mainView.findViewById(R.id.tvPrice);

        }
    }

}
