package com.darpysolutions.dove.PurchaseData.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.darpysolutions.dove.R;

public class BuyPriceFragment extends Fragment {
    Context mContext;
    EditText etPrice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        tv.setText(getArguments().getString("msg"));
        mainView = inflater.inflate(R.layout.fragment_buyprice, null);
        etPrice = mainView.findViewById(R.id.etPrice);

        return mainView;
    }

    public static BuyPriceFragment newInstance(String text) {

        BuyPriceFragment f = new BuyPriceFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);

        return f;
    }

    public String getPrice() {
        return etPrice.getText().toString();

    }
}
