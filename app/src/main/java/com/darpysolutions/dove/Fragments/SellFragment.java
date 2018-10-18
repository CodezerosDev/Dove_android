package com.darpysolutions.dove.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Activity.SellData;
import com.darpysolutions.dove.Manageplans_history.HistoryActivity;
import com.darpysolutions.dove.Manageplans_history.ManagePlansActivity;
import com.darpysolutions.dove.R;

public class SellFragment extends Fragment implements View.OnClickListener {
    Button btnContinue;
    Context mContext;
    TextView tvManagePlans, tvViewAll;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_sell, null);
        btnContinue = myView.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(this);

        tvManagePlans = myView.findViewById(R.id.tvManagePlans);
        tvManagePlans.setOnClickListener(this);

        tvViewAll = myView.findViewById(R.id.tvViewAll);
        tvViewAll.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnContinue:
                startSaleDataActivity();
                break;
            case R.id.tvManagePlans:
                startManageplansActivity();
                break;
            case R.id.tvViewAll:
                startHistoryActivity();
                break;
        }
    }

    private void startHistoryActivity() {
        Intent myIntent = new Intent(mContext, HistoryActivity.class);
        myIntent.putExtra(Constants.BUY_FLAG, false);
        startActivity(myIntent);
    }

    private void startManageplansActivity() {
        Intent myIntent = new Intent(mContext, ManagePlansActivity.class);
        myIntent.putExtra(Constants.BUY_FLAG, false);
        startActivity(myIntent);
    }

    private void startSaleDataActivity() {
        startActivity(new Intent(mContext, SellData.class));
    }
}
