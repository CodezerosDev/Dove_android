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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Activity.BuyData;
import com.darpysolutions.dove.Manageplans_history.HistoryActivity;
import com.darpysolutions.dove.Manageplans_history.ManagePlansActivity;
import com.darpysolutions.dove.R;

public class BuyFragment extends Fragment implements View.OnClickListener {
    Button btnContinue;

    Context mContext;
    TextView tvManagePlans, tvViewAll;
    FrameLayout flConnectPlan;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_buy, null);
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
                startBuyDataActivity();
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
        startActivity(new Intent(mContext, HistoryActivity.class).putExtra(Constants.BUY_FLAG, true));
    }

    private void startManageplansActivity() {
        startActivity(new Intent(mContext, ManagePlansActivity.class).putExtra(Constants.BUY_FLAG, true));
    }

    private void startBuyDataActivity() {
        startActivity(new Intent(mContext, BuyData.class));
    }
}
