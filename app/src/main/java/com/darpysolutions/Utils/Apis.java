package com.darpysolutions.Utils;

public class Apis {

    public static final String BASE_URL = "http://dove.network/dashboard/public/web_service/";
    //    public static final String BASE_URL = "http://webcluestech.com/qa/dove_network/public/web_service/";
    public static final String REGISTER_MOBILE = BASE_URL + "register_step1";
    public static final String VERIFY_OTP = BASE_URL + "register_step2";
    public static final String RESEND_OTP = BASE_URL + "resendotp";

    public static final String CREATE_WALLET = "http://webcluestech.com/qa/dove_network/public/web_service/keyether_create_wallet";
    public static final String EXPORT_WALLET = "http://webcluestech.com/qa/dove_network/public/web_service/keyether_export_wallet";
    public static final String IMPORT_WALLET = "http://webcluestech.com/qa/dove_network/public/web_service/keyether_import_wallet";
    public static final String IMPORT_WALLET_TYPE2 = "http://webcluestech.com/qa/dove_network/public/web_service/keyether_privatekeytopublickey";
    //    public static final String CREATE_WALLET = "http://webcluestech.com/qa/dove_network/public/web_service/np_create_wallet";
    public static final String BALANCE_CHECK = "http://webcluestech.com/qa/dove_network/public/web_service/np_address_balance";
    public static final String SETUP_PIN = BASE_URL + "setup_pin";
}
