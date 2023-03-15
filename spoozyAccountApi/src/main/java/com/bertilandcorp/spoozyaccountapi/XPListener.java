package com.bertilandcorp.spoozyaccountapi;

import java.util.List;

public interface XPListener {

    void onPopupDismiss(String value);
    void onPopupDismiss(List<String> checkeds);
    void onResendCodeCountChange(int resendCodeCount);
    void onSendCode();
    void onSingup();
    void onLogin();
}