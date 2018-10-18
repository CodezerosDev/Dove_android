package com.darpysolutions.dove.LoginFlow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darpysolutions.Utils.Apis;
import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.Utils;
import com.darpysolutions.dove.Dashboard.DashboardActivity;
import com.darpysolutions.dove.NetUtils.OnTaskCompleted;
import com.darpysolutions.dove.NetUtils.PostAsynchTask;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SetupPinActivity extends AppCompatActivity {
    TextView btnContinue;
    EditText etCode;
    EditText edtCode1, edtCode2, edtCode3, edtCode4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setuppin);
        btnContinue = findViewById(R.id.btnContinue);
        etCode = findViewById(R.id.etCode);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = edtCode1.getText().toString() + edtCode2.getText().toString() + edtCode3.getText().toString() + edtCode4.getText().toString();
                etCode.setText(code);
                if (etCode.getText().toString().length() < 4) {
                    Toast.makeText(SetupPinActivity.this, "Enter Minimum 4 numbers pin", Toast.LENGTH_LONG).show();
                } else {
                    CallSetPinApi();
                }

            }
        });

        edtCode1 = findViewById(R.id.edtCode1);
        edtCode2 = findViewById(R.id.edtCode2);
        edtCode3 = findViewById(R.id.edtCode3);
        edtCode4 = findViewById(R.id.edtCode4);

        try {
            String[] code = etCode.getText().toString().split("");

            edtCode1.setText(code[1]);
            edtCode2.setText(code[2]);
            edtCode3.setText(code[3]);
            edtCode4.setText(code[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }


        edtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode1.isFocusable()) {
                    edtCode2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode1.isFocusable()) {
                    edtCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1 && edtCode1.isFocusable()) {
                    edtCode2.requestFocus();
                }
            }
        });

        edtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode2.isFocusable()) {
                    edtCode3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode2.isFocusable()) {
                    edtCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1 && edtCode2.isFocusable()) {
                    edtCode3.requestFocus();
                }
            }
        });

        edtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode3.isFocusable()) {
                    edtCode4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1 && edtCode3.isFocusable()) {
                    edtCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1 && edtCode3.isFocusable()) {
                    edtCode4.requestFocus();
                }
            }
        });


        edtCode4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtCode4.setText("");
                    edtCode3.requestFocus();
                    edtCode3.setSelection(edtCode3.getText().length());
                } else {
                    edtCode4.requestFocus();
                }
                return false;
            }
        });

        edtCode3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtCode3.setText("");
                    edtCode2.requestFocus();
                    edtCode2.setSelection(edtCode2.getText().length());
                } else {
                    if (edtCode3.getText().toString().trim().length() > 0) {
                        edtCode4.requestFocus();
                        String number = Utils.getEvent(keyCode);
                        edtCode4.setText(number);
                    } else {

                    }
                }
                return false;
            }
        });

        edtCode2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtCode2.setText("");
                    edtCode1.requestFocus();
                    edtCode1.setSelection(edtCode1.getText().length());
                } else {
                    if (edtCode2.getText().toString().trim().length() > 0) {
                        edtCode3.requestFocus();
                        String number = Utils.getEvent(keyCode);
                        edtCode3.setText(number);
                        edtCode4.requestFocus();
                    } else {

                    }
                }
                return false;
            }
        });

        edtCode1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    edtCode1.setText("");
                    edtCode1.requestFocus();
                } else {
                    if (edtCode1.getText().toString().trim().length() > 0) {
                        edtCode2.requestFocus();
                        String number = Utils.getEvent(keyCode);
                        edtCode2.setText(number);
                        edtCode3.requestFocus();

                    } else {

                    }
                }
                return false;
            }
        });

    }

    private void CallSetPinApi() {

        if (com.darpysolutions.Utils.Utils.isNetworkConnected(SetupPinActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(SetupPinActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PHONE_NUMBER, "" + PrefUtilities.getString(SetupPinActivity.this, Constants.PHONE_NUMBER));
            toSendMap.put(Constants.PIN, "" + etCode.getText().toString());

            new PostAsynchTask(toSendMap, Apis.SETUP_PIN, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);
                            PrefUtilities.saveInt(SetupPinActivity.this, Constants.LOGGED_IN_AFTER_STAGE, Constants.LOGGED_IN_DASHBOARD);
                            Intent myIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(myIntent);
                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(SetupPinActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SetupPinActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).execute();
        } else {
            Toast.makeText(SetupPinActivity.this, "Please Connet to Active Internet Connection", Toast.LENGTH_LONG).show();
        }


    }
}
