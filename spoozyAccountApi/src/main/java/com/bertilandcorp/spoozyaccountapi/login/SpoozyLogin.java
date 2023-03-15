package com.bertilandcorp.spoozyaccountapi.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bertilandcorp.spoozyaccountapi.PType;
import com.bertilandcorp.spoozyaccountapi.R;
import com.bertilandcorp.spoozyaccountapi.XPListener;
import com.bertilandcorp.spoozyaccountapi.XPSItem;
import com.bertilandcorp.spoozyaccountapi.XPSItemAdapter;
import com.bertilandcorp.spoozyaccountapi.datas.D;
import com.bertilandcorp.spoozyaccountapi.signup.SSViewHolder;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.IndetProgress;
import api.pot.sound.WavRecorder;
import api.pot.sound.WaveformView;
import api.pot.system.Log;
import api.pot.system.tools.QueryCallback;
import api.pot.text.xtv.XTextView;
import api.pot.text.xtv.tools.FormattedText;
import api.pot.text.xtv.tools.SmartTextCallback;
import api.pot.view.tools.Forgrounder;
import api.pot.view.xl.XLayout;
import api.pot.view.xlistview.XItemViewTouch;
import api.pot.view.xlistview.XItemViewTouchCallback;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static api.pot.gl.tools.XImage.checkDir;
import static java.security.AccessController.getContext;

@SuppressLint("NewApi")
public class SpoozyLogin {

    private String RIGHT_CODE;
    private Context context;
    private PType pType;
    private XPListener xpListener;
    private FormattedText regexFormat;
    private boolean dismissOnTapOutside = true;
    private int resendCodeCount = 0;
    //
    private String[] valuesSet;
    private String defaultValue;
    private String titleValue = "Title";
    private String labelValue = "Label";
    private Integer iconeValue;
    private String iconeValuePath;
    private int iconeResId = -1;
    private Bitmap iconeValueBitmap;
    private Integer closeValue;
    private String submitValue = "Valider";
    private String resendCodeValue = "Renvoyez le code";
    private String resendCodeRegex = "Renvoyez le code";
    private int primaryColor = Color.WHITE;
    private int primaryColorDark = Color.BLACK;
    private int gravity = Gravity.CENTER;
    //
    private String appKey;
    //
    private View popupView;
    private LayoutInflater inflater;
    //
    private XLayout container;
    private XImageView icone;
    private XTextView title;
    private XTextView label;
    //
    private HorizontalScrollView codeArea;
    private List<EditText> codes = new ArrayList();
    //
    private TextInputLayout textInputTil;
    private AutoCompleteTextView textInput;
    //
    private Spinner spinner;
    //
    private XLayout submit;
    private XTextView resendCode;
    private XImageView close, check;
    ///
    private WaveformView waveformView;
    ///
    private CalendarView calendarView;
    private ScrollView calendarSv;

    private PopupWindow popupWindow;
    private ValueAnimator progresser;

    ///////////////------------------------------------++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private static SpoozyLogin spoozyLogin;

    public SpoozyLogin(Context context) {
        this.context = context;
        spoozyLogin = this;
    }

    public static SpoozyLogin with(Context context){
        /**if(spoozyLogin==null) */spoozyLogin = new SpoozyLogin(context);
        return spoozyLogin;
    }

    ///////////////------------------------------------++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public SpoozyLogin appKey(String appKey){
        this.appKey = appKey;
        return spoozyLogin;
    }

    public SpoozyLogin gravity(int gravity){
        this.gravity = gravity;
        return spoozyLogin;
    }

    public SpoozyLogin type(PType type){
        this.pType = type;
        return spoozyLogin;
    }

    public SpoozyLogin rightCode(String code){
        this.RIGHT_CODE = code;
        return spoozyLogin;
    }

    public SpoozyLogin icone(int resId){
        this.iconeResId = resId;
        return spoozyLogin;
    }

    public SpoozyLogin icone(String path){
        this.iconeValuePath = path;
        return spoozyLogin;
    }

    public SpoozyLogin icone(Bitmap bitmap){
        this.iconeValueBitmap = bitmap;
        return spoozyLogin;
    }

    public SpoozyLogin title(String title) {
        this.titleValue = title;
        return spoozyLogin;
    }

    public SpoozyLogin label(String label) {
        this.labelValue = label;
        return spoozyLogin;
    }

    public SpoozyLogin defaultValue(String dValue) {
        this.defaultValue = dValue;
        return spoozyLogin;
    }

    public SpoozyLogin valuesSet(String[] valuesSet) {
        this.valuesSet = valuesSet;
        return spoozyLogin;
    }

    public SpoozyLogin primaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        return spoozyLogin;
    }

    public SpoozyLogin primaryColorDark(int primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
        return spoozyLogin;
    }

    public SpoozyLogin listener(XPListener xpListener){
        this.xpListener = xpListener;
        return spoozyLogin;
    }

    ///////////////------------------------------------++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void codeNotReady(String msg){
        if(pType!=PType.CODE) return;
        label.setText(msg);
    }

    private String data;
    private View bgView;
    public void show(View view){
        bgView = view;
        //
        if(bgView instanceof XLayout){
            XLayout xLayout = (XLayout) bgView;
            xLayout.setBlurCover(true);
        }
        //
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popupView, width, height, dismissOnTapOutside);
        popupWindow.showAtLocation(view, gravity, 0, 0);
        //
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(bgView instanceof XLayout){
                    XLayout xLayout = (XLayout) bgView;
                    xLayout.setBlurCover(false);
                }
                if(xpListener!=null) {
                    xpListener.onPopupDismiss(data);
                }
            }
        });
        //
        data = null;
    }

    public void build(View view) {
        build();
        show(view);
    }

    public void dismiss(String msg){
        try {
            data = msg;
            popupWindow.dismiss();
        }catch (Exception e){}
    }


    private XTextView tosignup;
    SSViewHolder holderUsername;
    SSViewHolder holderPassword;
    public void build() {
        inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.spoozy_login_view, null);

        FormattedText format = new FormattedText();
        format.setBold();
        format.setColor(context.getResources().getColor(R.color.colorSpecific5));
        format.setUnderline();

        tosignup = popupView.findViewById(R.id.tosignup);

        tosignup.setRegex("signup...");
        tosignup.setRegexFormat(format);
        tosignup.setDetectRegex(true);
        tosignup.setSmartTextCallback(new SmartTextCallback() {
            @Override
            public void regexClick(String value) {
                super.regexClick(value);
                popupWindow.dismiss();
                if(xpListener!=null) xpListener.onSingup();
            }
        });

        holderUsername = new SSViewHolder((RelativeLayout) popupView.findViewById(R.id.item1),
                "spoozy id", R.drawable.ic_account_circle_black_24dp);
        holderPassword = new SSViewHolder((RelativeLayout) popupView.findViewById(R.id.item2),
                "Mot de passe", R.drawable.ic_account_circle_black_24dp);

        submit = popupView.findViewById(R.id.submit);
        submit.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = holderUsername.editText.getText().toString();
                Volley.newRequestQueue(context).add(volley_login_Request(id.contains("@")?null:id,
                        id.contains("@")?id:null, holderPassword.editText.getText().toString()));
                /**data="";
                popupWindow.dismiss();*/
            }
        });
    }




    private StringRequest volley_login_Request(final String phone, final String email, final String password) {
        String url = D.getApiLink()+"users/signin/"+appKey;

        StringRequest volley_check_phone_Request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            data=response;
                            popupWindow.dismiss();
                        } catch (Exception e) {
                            Log.r(context, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.r(context, error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String>  params = new HashMap<String, String>();
                if(phone!=null)params.put("telephone", phone);
                if(email!=null)params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        return volley_check_phone_Request;
    }


}
