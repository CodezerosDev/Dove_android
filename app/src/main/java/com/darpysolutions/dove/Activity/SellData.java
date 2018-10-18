package com.darpysolutions.dove.Activity;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.Test.CustomPagerAdapter;
import com.darpysolutions.dove.R;

public class SellData extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public final static int PAGES = 5;
    public final static int FIRST_PAGE = 0;

    CustomPagerAdapter mAdapter;
    ViewPager mViewPager;
    LinearLayout seekBarItemVol;
    LinearLayout seekBarItemDuration;
    LinearLayout seekBarItemSpeed;
    LinearLayout seekBarItemPrice;

    TextView seekBarOutPutVol;
    TextView seekBarOutPutDuration;
    TextView seekBarOutPutSpeed;
    TextView seekBarOutPutPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_selldata);

        mViewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        seekBarItemVol = (LinearLayout) findViewById(R.id.seekBarItemVol);
        seekBarItemDuration = (LinearLayout) findViewById(R.id.seekBarItemDuration);
        seekBarItemSpeed = (LinearLayout) findViewById(R.id.seekBarItemSpeed);
        seekBarItemPrice = (LinearLayout) findViewById(R.id.seekBarItemPrice);
        seekBarOutPutVol = (TextView) findViewById(R.id.seekBarOutPutVol);
        seekBarOutPutDuration = (TextView) findViewById(R.id.seekBarOutPutDuration);
        seekBarOutPutSpeed = (TextView) findViewById(R.id.seekBarOutPutSpeed);
        seekBarOutPutPrice = (TextView) findViewById(R.id.seekBarOutPutPrice);

        mAdapter = new CustomPagerAdapter(this, this.getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(false, mAdapter);
        mViewPager.setCurrentItem(FIRST_PAGE);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin((int) getResources().getDimension(R.dimen._minus90sdp));

        setSeekBars();
    }

    private void setSeekBars() {
        SeekBar seekBarVol = (SeekBar) findViewById(R.id.seekBarVol);
        SeekBar seekBarDuration = (SeekBar) findViewById(R.id.seekBarDuration);
        SeekBar seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        SeekBar seekBarPrice = (SeekBar) findViewById(R.id.seekBarPrice);

        seekBarVol.setTag(new seekBarModel(100, seekBarItemVol, "MB", seekBarOutPutVol, 100, 1100));
        seekBarDuration.setTag(new seekBarModel(1, seekBarItemDuration, "Hours", seekBarOutPutDuration, 1, 25));
        seekBarSpeed.setTag(new seekBarModel(2, seekBarItemSpeed, "Mbps", seekBarOutPutSpeed, 2, 22));
        seekBarPrice.setTag(new seekBarModel(2, seekBarItemPrice, "dove", seekBarOutPutPrice, 2, 52));

        setMinMaxVal(seekBarVol, seekBarDuration, seekBarSpeed, seekBarPrice);

        seekBarVol.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY));
        seekBarDuration.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY));
        seekBarSpeed.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY));
        seekBarPrice.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY));

        seekBarVol.setOnSeekBarChangeListener(this);
        seekBarDuration.setOnSeekBarChangeListener(this);
        seekBarSpeed.setOnSeekBarChangeListener(this);
        seekBarPrice.setOnSeekBarChangeListener(this);

        seekBarPrice.setProgress(5);
        seekBarSpeed.setProgress(2);
        seekBarDuration.setProgress(5);
        seekBarVol.setProgress(100);

        seekBarPrice.setThumb(getThumb(5));
        seekBarSpeed.setThumb(getThumb(2));
        seekBarDuration.setThumb(getThumb(5));
        seekBarVol.setThumb(getThumb(100));

        setUpseekBarItem(seekBarVol);
        setUpseekBarItem(seekBarDuration);
        setUpseekBarItem(seekBarSpeed);
        setUpseekBarItem(seekBarPrice);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setMinMaxVal(SeekBar seekBarVol, SeekBar seekBarDuration, SeekBar seekBarSpeed, SeekBar seekBarPrice) {
        try {
            seekBarVol.setMin(100);
            seekBarVol.setMax(1100);

            seekBarDuration.setMin(1);
            seekBarDuration.setMax(25);

            seekBarSpeed.setMin(2);
            seekBarSpeed.setMax(22);

            seekBarPrice.setMin(2);
            seekBarPrice.setMax(52);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
    }

    private void setUpseekBarItem(final SeekBar bar) {
        ((seekBarModel) bar.getTag()).getLinearLayout().removeAllViews();
        for (int i = ((seekBarModel) bar.getTag()).getMin(); i <= ((seekBarModel) bar.getTag()).getMax() - ((seekBarModel) bar.getTag()).getIntervals(); i = i + ((seekBarModel) bar.getTag()).getIntervals()) {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = vi.inflate(R.layout.list_item_seekbar, null);

            TextView value = (TextView) view.findViewById(R.id.value);
            TextView unit = (TextView) view.findViewById(R.id.unit);
            ImageView selected = (ImageView) view.findViewById(R.id.selected);

            i = i / ((seekBarModel) bar.getTag()).getIntervals();
            i = i * ((seekBarModel) bar.getTag()).getIntervals();

            if (i >= ((seekBarModel) bar.getTag()).getMin() && i <= ((seekBarModel) bar.getTag()).getMax()) {
                value.setText("" + i);
                unit.setText(((seekBarModel) bar.getTag()).getUnit());

                if (bar.getProgress() == i) {
                    selected.setVisibility(View.VISIBLE);
                } else {
                    selected.setVisibility(View.GONE);
                }

                final int prog = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bar.setProgress(prog);
                        bar.setThumb(getThumb(prog));

                        ((seekBarModel) bar.getTag()).getOutPut().setText(prog + " " + ((seekBarModel) bar.getTag()).getUnit());
                        bar.setThumb(getThumb(prog));
                        updateSelection(bar);
                    }
                });

                ((seekBarModel) bar.getTag()).getLinearLayout().addView(view);
            }
        }
    }

    private void updateSelection(SeekBar bar) {
        LinearLayout layout = ((seekBarModel) bar.getTag()).getLinearLayout();

        int prog = bar.getProgress();
        prog = prog / ((seekBarModel) bar.getTag()).getIntervals();
        prog = prog * ((seekBarModel) bar.getTag()).getIntervals();
        for (int i = 0; i < layout.getChildCount(); i++) {
            TextView value = (TextView) layout.getChildAt(i).findViewById(R.id.value);
            if (value.getText().toString().equalsIgnoreCase(prog + "")) {
                ((ImageView) layout.getChildAt(i).findViewById(R.id.selected)).setVisibility(View.VISIBLE);
                int scroll = i * (int) getResources().getDimension(R.dimen._90sdp);
                ((HorizontalScrollView) layout.getParent()).scrollTo(scroll, ((HorizontalScrollView) layout.getParent()).getBottom());
            } else {
                ((ImageView) layout.getChildAt(i).findViewById(R.id.selected)).setVisibility(View.GONE);
            }
        }

        ((seekBarModel) bar.getTag()).getOutPut().setText(prog + " " + ((seekBarModel) bar.getTag()).getUnit());

    }

    public Drawable getThumb(int progress) {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View thumbView = vi.inflate(R.layout.seekbar_thumb, null);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

        if (progress < ((seekBarModel) seekBar.getTag()).getMin()) {
            progress = ((seekBarModel) seekBar.getTag()).getMin();
        }
        if (progress > ((seekBarModel) seekBar.getTag()).getMax() - ((seekBarModel) seekBar.getTag()).getIntervals()) {
            progress = ((seekBarModel) seekBar.getTag()).getMax() - ((seekBarModel) seekBar.getTag()).getIntervals();
        }

        try {
            progress = progress / ((seekBarModel) seekBar.getTag()).getIntervals();
            progress = progress * ((seekBarModel) seekBar.getTag()).getIntervals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        seekBar.setThumb(getThumb(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        try {
            progress = progress / ((seekBarModel) seekBar.getTag()).getIntervals();
            progress = progress * ((seekBarModel) seekBar.getTag()).getIntervals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (progress < ((seekBarModel) seekBar.getTag()).getMin()) {
            progress = ((seekBarModel) seekBar.getTag()).getMin();
            seekBar.setThumb(getThumb(progress));
            seekBar.setProgress(progress);
        } else if (progress > ((seekBarModel) seekBar.getTag()).getMax() - ((seekBarModel) seekBar.getTag()).getIntervals()) {
            progress = ((seekBarModel) seekBar.getTag()).getMax() - ((seekBarModel) seekBar.getTag()).getIntervals();
            seekBar.setThumb(getThumb(progress));
            seekBar.setProgress(progress);
        }
        updateSelection(seekBar);
    }

    private class seekBarModel {
        int intervals;
        int min;
        int max;
        String unit;
        LinearLayout linearLayout;
        TextView outPut;

        private seekBarModel(int intervals, LinearLayout linearLayout, String unit, TextView outPut, int min, int max) {
            this.intervals = intervals;
            this.linearLayout = linearLayout;
            this.unit = unit;
            this.outPut = outPut;
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public TextView getOutPut() {
            return outPut;
        }

        public void setOutPut(TextView outPut) {
            this.outPut = outPut;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getIntervals() {
            return intervals;
        }

        public void setIntervals(int intervals) {
            this.intervals = intervals;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }

        public void setLinearLayout(LinearLayout linearLayout) {
            this.linearLayout = linearLayout;
        }
    }

    public void seekBarItemVol_left(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemVolScroll));
        horizontalScrollView.smoothScrollBy(-horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemVol_right(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemVolScroll));
        horizontalScrollView.smoothScrollBy(horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemDurationScroll_left(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemDurationScroll));
        horizontalScrollView.smoothScrollBy(-horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemDurationScroll_right(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemDurationScroll));
        horizontalScrollView.smoothScrollBy(horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemSpeedScroll_left(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemSpeedScroll));
        horizontalScrollView.smoothScrollBy(-horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemSpeedScroll_right(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemSpeedScroll));
        horizontalScrollView.smoothScrollBy(horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemPriceScroll_left(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemPriceScroll));
        horizontalScrollView.smoothScrollBy(-horizontalScrollView.getWidth(), 0);
    }

    public void seekBarItemPriceScroll_right(View view) {
        HorizontalScrollView horizontalScrollView = ((HorizontalScrollView) findViewById(R.id.seekBarItemPriceScroll));
        horizontalScrollView.smoothScrollBy(horizontalScrollView.getWidth(), 0);
    }

    public void SalePurchaseActivity(View view) {

        //startActivity(new Intent(this, SalePurchaseActivity.class).putExtra(Constants.BUY_FLAG,false));

        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            if (mode == AppOpsManager.MODE_ALLOWED) {
                startActivity(new Intent(this, PlanDashBoardOngoingPlan.class).putExtra(Constants.BUY_FLAG, false));
                finish();
            } else {
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), 123);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            try {
                PackageManager packageManager = getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
                if (mode == AppOpsManager.MODE_ALLOWED) {
                    startActivity(new Intent(this, PlanDashBoardOngoingPlan.class).putExtra(Constants.BUY_FLAG, false));
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
