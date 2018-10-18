package com.darpysolutions.dove.Manageplans_history;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.darpysolutions.Utils.Views.Textview.TextView_poppins_semibold;
import com.darpysolutions.dove.R;

public class SalePlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView btnSaveChanges, btnDeletePlan, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleplandetails);
        ivBack = findViewById(R.id.ivBack);

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnDeletePlan = findViewById(R.id.btnDeletePlan);
        btnCancel = findViewById(R.id.btnCancel);

        ivBack.setOnClickListener(this);
        btnSaveChanges.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDeletePlan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnSaveChanges:
                finish();
                break;
            case R.id.btnDeletePlan:
                showConfirmDelete();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void showConfirmDelete() {

        final Dialog dialog = new Dialog(SalePlanDetailsActivity.this, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_deleteplan_confirm);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        TextView btnDeletePlan = dialog.findViewById(R.id.btnDeletePlan);
        btnDeletePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        TextView cancelBtn = dialog.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
