package com.darpysolutions.dove.SalesData;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.darpysolutions.dove.PurchaseData.Fragment.BuyDurationFragment;
import com.darpysolutions.dove.PurchaseData.Fragment.BuyPriceFragment;
import com.darpysolutions.dove.PurchaseData.Fragment.BuySpeedFragment;
import com.darpysolutions.dove.PurchaseData.Fragment.BuyVolFragment;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.wifi.WifiDataBean;

public class SaleDataActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSummary, tvPrice, tvSpeed, tvDuration, tvVolume;
    TextView tvBack, tvNext;
    ViewPager vpBuyData;
    TextView tvBackTwo, tvPay;
    FrameLayout flSummary, flSwipes;
    ImageView ivBack;
    WifiDataBean wifiDataBean;


    BuyVolFragment buyVolFragment;
    BuyDurationFragment buyDurationFragment;
    BuySpeedFragment buySpeedFragment;
    BuyPriceFragment buyPriceFragment;

    TextView tvVolunit, tvDurationUnit, tvPriceUnit, tvSpeedUnit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saledata);

        ivBack = findViewById(R.id.ivBack);
        tvSummary = findViewById(R.id.tvSummary);
        tvPrice = findViewById(R.id.tvPrice);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvDuration = findViewById(R.id.tvDuration);
        tvVolume = findViewById(R.id.tvVolume);

        tvVolunit = findViewById(R.id.tvVolunit);
        tvDurationUnit = findViewById(R.id.tvDurationUnit);
        tvPriceUnit = findViewById(R.id.tvPriceUnit);
        tvSpeedUnit = findViewById(R.id.tvSpeedUnit);

        tvBack = findViewById(R.id.tvBack);
        tvNext = findViewById(R.id.tvNext);

        vpBuyData = findViewById(R.id.vpBuyData);
        vpBuyData.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        tvBackTwo = findViewById(R.id.tvBackTwo);
        tvPay = findViewById(R.id.tvPay);
        flSummary = findViewById(R.id.flSummary);
        flSwipes = findViewById(R.id.flSwipes);

        tvBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvBackTwo.setOnClickListener(this);
        tvPay.setOnClickListener(this);
        ivBack.setOnClickListener(this);


        tvSummary.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
        tvSpeed.setOnClickListener(this);
        tvDuration.setOnClickListener(this);
        tvVolume.setOnClickListener(this);

        buyVolFragment = BuyVolFragment.newInstance("vol");
        buyDurationFragment = BuyDurationFragment.newInstance("dur");
        buySpeedFragment = BuySpeedFragment.newInstance("spd");
        buyPriceFragment = BuyPriceFragment.newInstance("price");




        vpBuyData.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        resetAllViewBgs(tvVolume);
                        break;
                    case 1:
                        resetAllViewBgs(tvDuration);
                        break;
                    case 2:
                        resetAllViewBgs(tvSpeed);
                        break;
                    case 3:
                        resetAllViewBgs(tvPrice);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        resetAllViewBgs(tvVolume);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvBack:
                if (vpBuyData.getCurrentItem() > 0)
                    vpBuyData.setCurrentItem(vpBuyData.getCurrentItem() - 1);
                else
                    finish();
                break;
            case R.id.tvNext:
                if (vpBuyData.getCurrentItem() < 3)
                    vpBuyData.setCurrentItem(vpBuyData.getCurrentItem() + 1);
                else {
                    flSummary.setVisibility(View.VISIBLE);
                    flSwipes.setVisibility(View.GONE);
                    resetAllViewBgs(tvSummary);
                    getAndSetDataForSummary();
                }
                break;
            case R.id.tvBackTwo:
                flSummary.setVisibility(View.GONE);
                flSwipes.setVisibility(View.VISIBLE);
                resetAllViewBgs(tvPrice);
                break;
            case R.id.tvPay:
                showPayDialogue();
                break;

            case R.id.tvSummary:
                flSummary.setVisibility(View.VISIBLE);
                flSwipes.setVisibility(View.GONE);
                resetAllViewBgs(tvSummary);
                getAndSetDataForSummary();
                break;
            case R.id.tvPrice:
                if (flSummary.getVisibility() == View.VISIBLE) {
                    flSummary.setVisibility(View.GONE);
                    flSwipes.setVisibility(View.VISIBLE);
                    resetAllViewBgs(tvPrice);
                }
                vpBuyData.setCurrentItem(3);
                break;
            case R.id.tvSpeed:
                if (flSummary.getVisibility() == View.VISIBLE) {
                    flSummary.setVisibility(View.GONE);
                    flSwipes.setVisibility(View.VISIBLE);
                    resetAllViewBgs(tvSpeed);
                }
                vpBuyData.setCurrentItem(2);
                break;
            case R.id.tvDuration:
                if (flSummary.getVisibility() == View.VISIBLE) {
                    flSummary.setVisibility(View.GONE);
                    flSwipes.setVisibility(View.VISIBLE);
                    resetAllViewBgs(tvDuration);
                }
                vpBuyData.setCurrentItem(1);
                break;
            case R.id.tvVolume:
                if (flSummary.getVisibility() == View.VISIBLE) {
                    flSummary.setVisibility(View.GONE);
                    flSwipes.setVisibility(View.VISIBLE);
                    resetAllViewBgs(tvVolume);
                }
                vpBuyData.setCurrentItem(0);
                break;
        }
    }

    private void getAndSetDataForSummary() {
        String vol = buyVolFragment.getVol();
        String volDu = buyVolFragment.getVolDuration();

        String dur = buyDurationFragment.getDur();
        String durVol = buyDurationFragment.getDurVol();

        String spd = buySpeedFragment.getSpd();
        String spdVol = buySpeedFragment.getSpdVol();

        String price = buyPriceFragment.getPrice();

        wifiDataBean = new WifiDataBean(vol, volDu, dur, durVol, spd, spdVol, price);
//        wifiDataBean = new WifiDataBean("200", "M", "30", "D", "5", "M", "150");

        tvVolunit.setText(vol + " " + volDu);
        tvDurationUnit.setText(dur + " " + durVol);
        tvSpeedUnit.setText(spd + " " + spdVol);
        tvPriceUnit.setText(price);

    }


    private void showPayDialogue() {
        final Dialog dialog = new Dialog(SaleDataActivity.this, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_sales_confirm);
        dialog.show();

        Button payButton = dialog.findViewById(R.id.btnPay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent data = new Intent();
                data.putExtra("wifiName", "" + wifiDataBean.getGeneratedString());
                data.putExtra("vol", "" + tvVolunit.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        Button cancelBtn = dialog.findViewById(R.id.btnCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void resetAllViewBgs(TextView tv) {

        tvSummary.setBackgroundResource(R.drawable.white_btn_bg);
        tvPrice.setBackgroundResource(R.drawable.white_btn_bg);
        tvSpeed.setBackgroundResource(R.drawable.white_btn_bg);
        tvDuration.setBackgroundResource(R.drawable.white_btn_bg);
        tvVolume.setBackgroundResource(R.drawable.white_btn_bg);

        tvSummary.setTextColor(getResources().getColor(R.color.coolGrayColor));
        tvPrice.setTextColor(getResources().getColor(R.color.coolGrayColor));
        tvSpeed.setTextColor(getResources().getColor(R.color.coolGrayColor));
        tvDuration.setTextColor(getResources().getColor(R.color.coolGrayColor));
        tvVolume.setTextColor(getResources().getColor(R.color.coolGrayColor));

        tv.setBackgroundResource(R.drawable.yellow_btn_bg);
        tv.setTextColor(getResources().getColor(R.color.white));


    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return buyVolFragment;
                case 1:
                    return buyDurationFragment;
                case 2:
                    return buySpeedFragment;
                case 3:
                    return buyPriceFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
