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

public class BuyVolFragment extends Fragment {

    Context mContext;
    EditText etVol;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    View mainView;
    CustomSpinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        tv.setText(getArguments().getString("msg"));
        mainView = inflater.inflate(R.layout.fragment_buyvol, null);

        String[] data = {"MB", "GB"};

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item_selected, data);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner = (CustomSpinner) mainView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setSpinnerEventsListener(new CustomSpinner.OnSpinnerEventsListener() {
            public void onSpinnerOpened() {
                spinner.setSelected(true);
            }

            public void onSpinnerClosed() {
                spinner.setSelected(false);
            }
        });

        etVol = mainView.findViewById(R.id.etVol);

        return mainView;
    }

    public static BuyVolFragment newInstance(String text) {

        BuyVolFragment f = new BuyVolFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);


        return f;
    }

    public String getVol() {
//
        if (etVol.getText().toString().length() > 0)
            return etVol.getText().toString();
        else {
            Toast.makeText(getContext(), "Please Enter Valid Volume", Toast.LENGTH_LONG).show();
            return "";
        }

    }

    public String getVolDuration() {
        String selected = (String) spinner.getSelectedItem();
        if (selected.equalsIgnoreCase("MB"))
            return "M";
        else
            return "G";
    }
}
