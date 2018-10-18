package com.darpysolutions.dove.PurchaseData.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.darpysolutions.Utils.CustomSpinner;
import com.darpysolutions.dove.R;

public class BuySpeedFragment extends Fragment {

    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    View mainView;
    EditText etSpeed;
    CustomSpinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        tv.setText(getArguments().getString("msg"));
        mainView = inflater.inflate(R.layout.fragment_buyspeed, null);

        String[] data = {"Kbps", "Mbps", "Gbps"};

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        etSpeed = mainView.findViewById(R.id.etSpeed);

        spinner = mainView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                spinner.setSelected(true);
            }

            public void onSpinnerClosed() {
                spinner.setSelected(false);
            }
        });
        return mainView;
    }

    public static BuySpeedFragment newInstance(String text) {

        BuySpeedFragment f = new BuySpeedFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    public String getSpd() {
        if (etSpeed.getText().toString().length() > 0)
            return etSpeed.getText().toString();
        else {
            Toast.makeText(getContext(), "Please Enter Valid Volume", Toast.LENGTH_LONG).show();
            return "";
        }

    }

    public String getSpdVol() {
        String selected = (String) spinner.getSelectedItem();
        if (selected.equalsIgnoreCase("Kbps"))
            return "K";
        else if (selected.equalsIgnoreCase("Mbps"))
            return "M";
        else return "G";

    }
}
