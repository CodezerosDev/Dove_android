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
import com.darpysolutions.dove.Manageplans_history.Adapters.ManageBuyPlansAdapter;
import com.darpysolutions.dove.Manageplans_history.Adapters.ManageSalePlansAdapter;
import com.darpysolutions.dove.R;

public class ManagePlansActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvManageplans;
    ImageView ivBack;
    TextView tvBuyPlans, tvSalePlans;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageplans);

        rvManageplans = findViewById(R.id.rvManageplans);
        ivBack = findViewById(R.id.ivBack);

        tvBuyPlans = findViewById(R.id.tvBuyPlans);
        tvSalePlans = findViewById(R.id.tvSalePlans);

        ivBack.setOnClickListener(this);
        tvBuyPlans.setOnClickListener(this);
        tvSalePlans.setOnClickListener(this);

        rvManageplans = findViewById(R.id.rvManageplans);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ManagePlansActivity.this);
        rvManageplans.setLayoutManager(mLayoutManager);
        rvManageplans.setItemAnimator(new DefaultItemAnimator());

        boolean Buyflag = getIntent().getBooleanExtra(Constants.BUY_FLAG, true);
        if (Buyflag) {
            tvBuyPlans.performClick();
        } else {
            tvSalePlans.performClick();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyPlans:
                tvBuyPlans.setTextColor(getResources().getColor(R.color.black));
                tvSalePlans.setTextColor(getResources().getColor(R.color.white));
                tvBuyPlans.setBackgroundResource(R.drawable.rounded_primary);
                tvSalePlans.setBackgroundColor(Color.TRANSPARENT);


                rvManageplans.setAdapter(new ManageBuyPlansAdapter(ManagePlansActivity.this, null));
                break;
            case R.id.tvSalePlans:
                tvBuyPlans.setTextColor(getResources().getColor(R.color.white));
                tvSalePlans.setTextColor(getResources().getColor(R.color.black));
                tvBuyPlans.setBackgroundColor(Color.TRANSPARENT);
                tvSalePlans.setBackgroundResource(R.drawable.rounded_primary);

                rvManageplans.setAdapter(new ManageSalePlansAdapter(ManagePlansActivity.this, null));
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
