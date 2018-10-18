package com.darpysolutions.dove.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darpysolutions.Utils.Constants;
import com.darpysolutions.dove.Activity.PlanDashBoard;
import com.darpysolutions.dove.LoggingService;
import com.darpysolutions.dove.Manageplans_history.Adapters.HistoryBuyPlansAdapter;
import com.darpysolutions.dove.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class GraphFragmentBuy extends Fragment implements
        OnChartGestureListener, OnChartValueSelectedListener {
    LineChart mChart;
    Context mContext;
    RecyclerView rvBuyHistory;
    long lastKnownBytes = 0;
    //    long totalAllowedBytes = 0;
    float dataChanged = 0;
    String vol = "";
    ArrayList<Entry> values = new ArrayList<Entry>();

    IntentFilter intentFilter;

    BroadcastReceiver broadcastReceiver;
    TextView btnContinue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();

        assert getArguments() != null;
        lastKnownBytes = getArguments().getLong("lastKnowData");
        vol = getArguments().getString("vol");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_graph_buy, null);
        mChart = mainView.findViewById(R.id.mChart);
        rvBuyHistory = mainView.findViewById(R.id.rvBuyHistory);
        btnContinue = mainView.findViewById(R.id.btnContinue);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        rvBuyHistory.setLayoutManager(mLayoutManager);
        rvBuyHistory.setItemAnimator(new DefaultItemAnimator());
        rvBuyHistory.setAdapter(new HistoryBuyPlansAdapter(mContext, null));
        setChart();
        intentFilter = new IntentFilter(Constants.BROADCAST_SEND_DATA_UPDATES);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                dataChanged = (float) intent.getDoubleExtra("dataChanged", 0);
                Log.e("data from brodcast", intent.getDoubleExtra("dataChanged", 0) + "");
                setData();

            }
        };
        Intent serviceIntent = new Intent(mContext, LoggingService.class);
        serviceIntent.putExtra("lastKnowData", lastKnownBytes);
        serviceIntent.putExtra("vol", vol);
        mContext.startService(serviceIntent);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(),PlanDashBoard.class).putExtra(Constants.BUY_FLAG,true));
                getActivity().finish();
            }
        });
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        mContext.unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void setChart() {


        int offset = 0;
        if (!vol.equals("")) {
            if (vol.contains("M"))
                offset = Integer.parseInt(vol.replace(" M", ""));
            else
                offset = Integer.parseInt(vol.replace(" G", ""));
        }

        // no description text
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(true);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(20f, 20f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        llXAxis.setTextColor(Color.WHITE);

        XAxis xAxis = mChart.getXAxis();
        xAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        xAxis.setAxisMaximum(offset);
        xAxis.setAxisMinimum(0f);
        xAxis.enableGridDashedLine(20f, 20f, 0f);

        //xAxis.disableAxisLineDashedLine();
        //xAxis.disableGridDashedLine();

        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(20f, 20f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.WHITE);

        LimitLine ll2 = new LimitLine(0f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(20f, 20f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTextColor(Color.WHITE);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(offset);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.animateX(1000);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);


    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public void setData() {


        values.add(new Entry(values.size(), 1, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 1, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 7, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 7, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 2, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 2, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 5, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 5, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 1, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 1, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 6, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 6, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 4, getResources().getDrawable(R.drawable.star)));
        values.add(new Entry(values.size(), 4, getResources().getDrawable(R.drawable.star)));
        Log.e("data_changed", dataChanged + " bytes");

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            set1.setColor(Color.YELLOW);
            set1.setCircleColor(Color.YELLOW);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormSize(15.f);
            set1.setFillColor(Color.TRANSPARENT);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            mChart.setData(new LineData(dataSets));
        }
    }
}
