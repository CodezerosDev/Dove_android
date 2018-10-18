package com.darpysolutions.dove.Wallet.Adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.Wallet.WalletListActivity;
import com.darpysolutions.dove.Wallet.WalletModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.MyViewHolder> {
    WalletListActivity activity;
    ArrayList<WalletModel> drBeanArrayList;

    public WalletListAdapter(WalletListActivity context) {
        activity = context;

        JSONObject walletObject = null;
        try {
            walletObject = new JSONObject(PrefUtilities
                    .getString(context, Constants.WALLETS));
            drBeanArrayList = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                    , new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        drBeanArrayList = new Gson().fromJson(PrefUtilities
//                .getString(context, Constants.WALLETS), new TypeToken<ArrayList<WalletModel>>() {
//        }.getType());
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_wallet_export, parent, false);
        return new MyViewHolder(itemView);
    }

    public void refreshData() {
        JSONObject walletObject = null;
        try {
            walletObject = new JSONObject(PrefUtilities
                    .getString(activity, Constants.WALLETS));
            drBeanArrayList = new Gson().fromJson(walletObject.getString(Constants.WALLETS)
                    , new TypeToken<ArrayList<WalletModel>>() {
                    }.getType());
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tv_status.setText(drBeanArrayList.get(holder.getAdapterPosition()).isActive() ? "Active"
                : "Inactive");
//        String walletName = (drBeanArrayList.get(holder.getAdapterPosition()).getType() == 1 ?
//                "Dove Wallet " : "Wallet ") + drBeanArrayList.get(holder.getAdapterPosition())
//                .getWalletSequence();
//        holder.wallet_name.setText(walletName);
        holder.wallet_name.setText(drBeanArrayList.get(holder.getAdapterPosition()).getWalletName());

        holder.tvExportWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.exportWallet(drBeanArrayList.get(holder.getAdapterPosition()));
            }
        });
        holder.tvRemoveWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeWallet(drBeanArrayList.get(holder.getAdapterPosition()));
            }
        });

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.selectWallet(drBeanArrayList.get(holder.getAdapterPosition()),holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
//        return 20;
        if (drBeanArrayList == null) return 0;
        return drBeanArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mainView;
        TextView tvExportWallet, tvRemoveWallet;
        TextView tv_status, wallet_name;

        public MyViewHolder(View view) {
            super(view);
            mainView = view;
            tvExportWallet = mainView.findViewById(R.id.tvExportWallet);
            tvRemoveWallet = mainView.findViewById(R.id.tvRemoveWallet);
            tv_status = mainView.findViewById(R.id.tv_status);
            wallet_name = mainView.findViewById(R.id.wallet_name);
        }
    }

}
