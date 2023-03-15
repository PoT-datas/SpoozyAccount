package com.bertilandcorp.spoozyaccountapi.signup;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bertilandcorp.spoozyaccountapi.R;

public class SSViewHolder {
    public TextView label;
    public TextInputLayout til;
    public EditText editText;

    public SSViewHolder(RelativeLayout root, String lab, int rightIcone) {
        label = (TextView) root.getChildAt(0);
        til = (TextInputLayout) ((RelativeLayout)root.getChildAt(1)).getChildAt(0);
        editText = (EditText) ((FrameLayout) til.getChildAt(0)).getChildAt(0);
        ///
        this.label.setText(lab);
        this.til.setHint(lab);
        this.editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightIcone, 0);
    }
}
