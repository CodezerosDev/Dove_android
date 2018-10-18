package com.darpysolutions.dove.LoginFlow;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darpysolutions.Utils.Apis;
import com.darpysolutions.Utils.Constants;
import com.darpysolutions.Utils.Utils;
import com.darpysolutions.dove.NetUtils.OnTaskCompleted;
import com.darpysolutions.dove.NetUtils.PostAsynchTask;
import com.darpysolutions.dove.NetUtils.PrefUtilities;
import com.darpysolutions.dove.R;
import com.darpysolutions.dove.countrypicker.Country;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class VerificationCodeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    TextView tvTimer;
    int time = 30;
    Button btnEditNumber;
    EditText etCode;
    EditText edtCode1, edtCode2, edtCode3, edtCode4, edtHiddenCode;
    TextView tvCode;
    TextView btnContinue;
    ImageView ivCountry;
    TextView etPhoneNumber;

    int flagResId;
    Country country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationcode);
        tvTimer = findViewById(R.id.tvTimer);
        btnEditNumber = findViewById(R.id.btnEditNumber);
        btnContinue = findViewById(R.id.btnContinue);
        etCode = findViewById(R.id.etCode);
        etCode.setText(PrefUtilities.getString(VerificationCodeActivity.this, Constants.VERIFICATION_CODE));
        edtHiddenCode = (EditText) findViewById(R.id.edtHiddenCode);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        ivCountry = findViewById(R.id.ivCountry);
        tvCode = findViewById(R.id.tvCode);

        flagResId = getIntent().getIntExtra(Constants.FLAG_RES_ID, 0);
        country = getIntent().getParcelableExtra(Constants.COUNTRY);


        //tvCode.setText(country.getDialingCode());
        ivCountry.setImageResource(flagResId);
        etPhoneNumber.setText(getIntent().getStringExtra(Constants.PHONE_NUMBER));
        edtCode1 = findViewById(R.id.edtCode1);
        edtCode2 = findViewById(R.id.edtCode2);
        edtCode3 = findViewById(R.id.edtCode3);
        edtCode4 = findViewById(R.id.edtCode4);

        String[] code = etCode.getText().toString().split("");

        edtCode1.setText(code[1]);
        edtCode2.setText(code[2]);
        edtCode3.setText(code[3]);
        edtCode4.setText(code[4]);


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

        edtHiddenCode.setText(PrefUtilities.getString(VerificationCodeActivity.this, Constants.VERIFICATION_CODE));
        btnContinue.setOnClickListener(this);
        btnEditNumber.setOnClickListener(this);
        startTimer();
    }

    private void startTimer() {
        time = 30;
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("0:" + checkDigit(time));
                time--;
            }

            public void onFinish() {
                tvTimer.setText("Resend");
                tvTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallForResendOtpApi();
                    }
                });
            }
        }.start();

    }

    private void CallForResendOtpApi() {
        tvTimer.setOnClickListener(null);

        if (com.darpysolutions.Utils.Utils.isNetworkConnected(VerificationCodeActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(VerificationCodeActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PHONE_NUMBER, "" + PrefUtilities.getString(VerificationCodeActivity.this, Constants.PHONE_NUMBER));

            new PostAsynchTask(toSendMap, Apis.RESEND_OTP, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);
//                            {
//                                "status_code": 1,
//                                    "message": "Resend Code Successfully",
//                                    "data": {
//                                "user_id": "91",
//                                        "phone_number": "918200587492",
//                                        "otp": "5615"
//                            }
//                            }

                            PrefUtilities.saveString(VerificationCodeActivity.this, Constants.VERIFICATION_CODE, dataObject.getString(Constants.OTP));
                            etCode.setText(PrefUtilities.getString(VerificationCodeActivity.this, Constants.VERIFICATION_CODE));

                            startTimer();

                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(VerificationCodeActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(VerificationCodeActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).execute();
        } else {
            Toast.makeText(VerificationCodeActivity.this, "Please Connet to Active Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditNumber:
                finish();
                break;
            case R.id.btnContinue:
                CallVerificationAPI();
                break;
        }
    }

    private void CallVerificationAPI() {

        if (com.darpysolutions.Utils.Utils.isNetworkConnected(VerificationCodeActivity.this)) {
            final ProgressDialog progressDialog = new ProgressDialog(VerificationCodeActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            HashMap<String, String> toSendMap = new HashMap<>();
            toSendMap.put(Constants.PHONE_NUMBER, "" + PrefUtilities.getString(VerificationCodeActivity.this, Constants.PHONE_NUMBER));
            toSendMap.put(Constants.USER_ID, "" + PrefUtilities.getInt(VerificationCodeActivity.this, Constants.USER_ID));
            toSendMap.put(Constants.VERIFICATION_CODE, edtHiddenCode.getText().toString());
            new PostAsynchTask(toSendMap, Apis.VERIFY_OTP, new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(String result) {
                    progressDialog.dismiss();
                    Log.e("Result", "found :" + result);
                    JSONObject resultObject = null;
                    try {
                        resultObject = new JSONObject(result);
                        if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("1")) {

                            JSONObject dataObject = resultObject.getJSONObject(Constants.DATA);
//                            {
//                                "status_code": 1,
//                                    "message": "Pin not setuped",
//                                    "data": {
//                                "user_id": 91,
//                                        "phone_number": "918200587492",
//                                        "otp": "9018"
//                            }
//                            }
                            PrefUtilities.saveBoolean(VerificationCodeActivity.this, Constants.IS_LOGGED_IN, true);
                            PrefUtilities.saveInt(VerificationCodeActivity.this, Constants.LOGGED_IN_AFTER_STAGE, Constants.LOGGED_IN_WALLET);
                            Intent intent = new Intent(getApplicationContext(), WelcomeWalletActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else if (resultObject.getString(Constants.STATUS_CODE).equalsIgnoreCase("0")) {

                            String msg = resultObject.getString(Constants.MESSAGE);
                            Toast.makeText(VerificationCodeActivity.this, msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(VerificationCodeActivity.this, "Some Technical Error Occured", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }).execute();
        } else {
            Toast.makeText(VerificationCodeActivity.this, "Please Connet to Active Internet Connection", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            edtCode1.setText("_");
            edtCode2.setText("_");
            edtCode3.setText("_");
            edtCode4.setText("_");
        } else if (s.length() == 1) {
            edtCode1.setText(s.charAt(0) + "");
            edtCode2.setText("_");
            edtCode3.setText("_");
            edtCode4.setText("_");
        } else if (s.length() == 2) {
            edtCode2.setText(s.charAt(1) + "");
            edtCode3.setText("_");
            edtCode4.setText("_");
        } else if (s.length() == 3) {
            edtCode3.setText(s.charAt(2) + "");
            edtCode4.setText("_");
        } else if (s.length() == 4) {
            edtCode4.setText(s.charAt(3) + "");
            Utils.hideKeyboardView(VerificationCodeActivity.this, edtCode4);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        final int id = v.getId();
        switch (id) {
            case R.id.edtCode1:
                if (hasFocus) {
                    setFocus(edtHiddenCode);
                    showSoftKeyboard(edtHiddenCode);
                }
                break;

            case R.id.edtCode2:
                if (hasFocus) {
                    setFocus(edtHiddenCode);
                    showSoftKeyboard(edtHiddenCode);
                }
                break;

            case R.id.edtCode3:
                if (hasFocus) {
                    setFocus(edtHiddenCode);
                    showSoftKeyboard(edtHiddenCode);
                }
                break;

            case R.id.edtCode4:
                if (hasFocus) {
                    setFocus(edtHiddenCode);
                    showSoftKeyboard(edtHiddenCode);
                }
                break;

            default:
                break;
        }

    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.edtHiddenCode:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (edtHiddenCode.getText().length() == 4)
                            edtCode4.setText("_");
                        else if (edtHiddenCode.getText().length() == 3)
                            edtCode3.setText("_");
                        else if (edtHiddenCode.getText().length() == 2)
                            edtCode2.setText("_");
                        else if (edtHiddenCode.getText().length() == 1)
                            edtCode1.setText("_");

                        if (edtHiddenCode.length() > 0)
                            edtHiddenCode.setText(edtHiddenCode.getText().subSequence(0, edtHiddenCode.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }
}
