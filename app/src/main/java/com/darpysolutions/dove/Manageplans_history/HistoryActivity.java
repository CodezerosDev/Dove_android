package com.darpysolutions.dove.Manageplans_history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Manageplans_history.Adapters.HistoryBuyPlansAdapter;
import com.darpysolutions.dove.Manageplans_history.Adapters.HistorySalePlansAdapter;
import com.darpysolutions.dove.Manageplans_history.Adapters.ManageBuyPlansAdapter;
import com.darpysolutions.dove.Manageplans_history.Adapters.ManageSalePlansAdapter;
import com.darpysolutions.dove.R;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvBuyHistory, tvSaleHistory;
    RecyclerView rvHistory;
    ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        rvHistory = findViewById(R.id.rvHistory);
        ivBack = findViewById(R.id.ivBack);

        tvBuyHistory = findViewById(R.id.tvBuyHistory);
        tvSaleHistory = findViewById(R.id.tvSaleHistory);

        ivBack.setOnClickListener(this);
        tvBuyHistory.setOnClickListener(this);
        tvSaleHistory.setOnClickListener(this);

        rvHistory = findViewById(R.id.rvHistory);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        rvHistory.setLayoutManager(mLayoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());

        boolean buyFlag = getIntent().getBooleanExtra(Constants.BUY_FLAG, true);
        if (buyFlag) {
            tvBuyHistory.performClick();
        } else {
            tvSaleHistory.performClick();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyHistory:
                tvBuyHistory.setTextColor(Color.BLACK);
                tvBuyHistory.setBackgroundResource(R.drawable.rounded_primary);
                tvSaleHistory.setTextColor(Color.WHITE);
                tvSaleHistory.setBackgroundColor(Color.TRANSPARENT);

                rvHistory.setAdapter(new HistoryBuyPlansAdapter(HistoryActivity.this, null));
                break;
            case R.id.tvSaleHistory:
                tvSaleHistory.setTextColor(Color.BLACK);
                tvSaleHistory.setBackgroundResource(R.drawable.rounded_primary);
                tvBuyHistory.setTextColor(Color.WHITE);
                tvBuyHistory.setBackgroundColor(Color.TRANSPARENT);

                rvHistory.setAdapter(new HistorySalePlansAdapter(HistoryActivity.this, null));
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
