package com.bertilandcorp.spoozyaccountapi.checking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ScrollView;
import android.widget.Spinner;

import com.bertilandcorp.spoozyaccountapi.PType;
import com.bertilandcorp.spoozyaccountapi.R;
import com.bertilandcorp.spoozyaccountapi.XPListener;
import com.bertilandcorp.spoozyaccountapi.XPSItem;
import com.bertilandcorp.spoozyaccountapi.XPSItemAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import api.pot.gl.xiv.XImageView;
import api.pot.gl.xiv.tools.IndetProgress;
import api.pot.sound.WavRecorder;
import api.pot.sound.WaveformView;
import api.pot.text.xtv.XTextView;
import api.pot.text.xtv.tools.FormattedText;
import api.pot.text.xtv.tools.SmartTextCallback;
import api.pot.view.tools.Forgrounder;
import api.pot.view.xl.XLayout;
import api.pot.view.xlistview.XItemViewTouch;
import api.pot.view.xlistview.XItemViewTouchCallback;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static api.pot.gl.tools.XImage.checkDir;

@SuppressLint("NewApi")
public class SpoozyChecking {

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

    private static SpoozyChecking spoozyChecking;

    public SpoozyChecking(Context context) {
        this.context = context;
        spoozyChecking = this;
    }

    public static SpoozyChecking with(Context context){
        /**if(spoozyChecking==null) */spoozyChecking = new SpoozyChecking(context);
        return spoozyChecking;
    }

    ///////////////------------------------------------++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public SpoozyChecking gravity(int gravity){
        this.gravity = gravity;
        return spoozyChecking;
    }

    public SpoozyChecking type(PType type){
        this.pType = type;
        return spoozyChecking;
    }

    public SpoozyChecking rightCode(String code){
        this.RIGHT_CODE = code;
        return spoozyChecking;
    }

    public SpoozyChecking icone(int resId){
        this.iconeResId = resId;
        return spoozyChecking;
    }

    public SpoozyChecking icone(String path){
        this.iconeValuePath = path;
        return spoozyChecking;
    }

    public SpoozyChecking icone(Bitmap bitmap){
        this.iconeValueBitmap = bitmap;
        return spoozyChecking;
    }

    public SpoozyChecking title(String title) {
        this.titleValue = title;
        return spoozyChecking;
    }

    public SpoozyChecking label(String label) {
        this.labelValue = label;
        return spoozyChecking;
    }

    public SpoozyChecking defaultValue(String dValue) {
        this.defaultValue = dValue;
        return spoozyChecking;
    }

    public SpoozyChecking valuesSet(String[] valuesSet) {
        this.valuesSet = valuesSet;
        return spoozyChecking;
    }

    public SpoozyChecking primaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        return spoozyChecking;
    }

    public SpoozyChecking primaryColorDark(int primaryColorDark) {
        this.primaryColorDark = primaryColorDark;
        return spoozyChecking;
    }

    public SpoozyChecking listener(XPListener xpListener){
        this.xpListener = xpListener;
        return spoozyChecking;
    }

    ///////////////------------------------------------++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void codeReady(String msg, int resendCount){
        if(pType!=PType.CODE) return;
        label.setText(msg);
        resendCodeCount = resendCount;
        initProgresser(resendCodeCount, resendCode);
        progresser.start();
        if(resendCodeCount==0) {
            if(xpListener!=null) xpListener.onSendCode();
        }
        resendCodeCount++;
        if(xpListener!=null) xpListener.onResendCodeCountChange(resendCodeCount);
    }

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
                if(pType==PType.SOUND){
                    stopRec();
                    stopPlay();
                }
                if(xpListener!=null) {
                    if(pType==PType.CHECKING) xpListener.onPopupDismiss(checkeds);
                    else xpListener.onPopupDismiss(data);
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

    public void build() {
        inflater = (LayoutInflater)
                context.getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.spoozy_checking_view, null);

        ///acces views
        container = popupView.findViewById(R.id.container);
        icone = popupView.findViewById(R.id.icone);
        title = popupView.findViewById(R.id.title);
        label = popupView.findViewById(R.id.label);
        //
        textInputTil = popupView.findViewById(R.id.textInputTil);
        textInput = popupView.findViewById(R.id.textInput);
        //
        codeArea = popupView.findViewById(R.id.codeArea);
        resendCode = popupView.findViewById(R.id.resendCode);
        close = popupView.findViewById(R.id.close);
        check = popupView.findViewById(R.id.check);
        //
        spinner = popupView.findViewById(R.id.spinner);
        //
        waveformView = popupView.findViewById(R.id.waveformView);
        tongle = popupView.findViewById(R.id.tongle);
        //
        calendarView = popupView.findViewById(R.id.calendarView);
        calendarSv = popupView.findViewById(R.id.calendarSv);
        //
        title.setText(titleValue);
        if(iconeResId!=-1) icone.setImageResource(iconeResId);
        else if(iconeValuePath!=null) icone.setImagePath(iconeValuePath);
        else if(iconeValueBitmap!=null) icone.setImageBitmap(iconeValueBitmap);
        //
        submit = popupView.findViewById(R.id.submit);
        //
        icone.getIndetProgress().usingColors(primaryColor);
        icone.getIndetProgress().initType(IndetProgress.INDET_PROGRESS_STATIC_TURN);
        icone.getIndetProgress().play();
        //
        submit.setOnFgClickListener(new Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pType==PType.CODE){
                    if(isCodesValide(codes)){
                        popupWindow.dismiss();
                    }else {
                        data = null;
                        container.notifyError(false, true);
                    }
                }else if(pType==PType.TEXT){
                    if(validate(textInput, textInputTil)){
                        popupWindow.dismiss();
                    }
                }else if( pType==PType.SELECT ){
                    data = spinner.getSelectedItem().toString();
                    popupWindow.dismiss();
                }
            }
        });
        ///
        check.setVisibility(View.GONE);
        ///
        if(pType==PType.TEXT) initTextInput();
        else if (pType==PType.CODE) initCodeInput();
        else if(pType==PType.SELECT) initSelection();
        else if(pType==PType.CONFIRM) initConfirm();
        else if(pType==PType.CHECKING) initChecking();
        else if(pType==PType.SOUND) initSound();
        else if(pType==PType.DATE) initDate();
    }

    private void initDate() {
        ///
        calendarSv.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        ///
        check.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = calendarView.getDate()+"";
                popupWindow.dismiss();
            }
        });
        close.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = null;
                popupWindow.dismiss();
            }
        });
        ///
        textInputTil.setVisibility(View.GONE);
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        ////------
        initRec();
        ////------
    }

    private boolean recMode = true;
    private boolean isPlay = false;
    private boolean isRec = false;
    private MediaPlayer mediaPlayer;
    private WavRecorder wavRecorder;
    private XImageView tongle;
    private File file;
    private String path;
    private void initSound() {
        tongle.getTransitor().useTransition(false);
        //
        tongle.setVisibility(View.VISIBLE);
        waveformView.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        ///
        label.setText(labelValue);
        //
        data = null;
        //
        check.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file!=null&&file.exists())
                    data = file.getAbsolutePath();
                else data = null;
                popupWindow.dismiss();
            }
        });
        close.setImageResource(R.drawable.ic_stop_black_24dp);
        close.getTransitor().useTransition(false);
        close.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wavRecorder.isRecording()){
                    stopRec();
                    tongle.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    recMode = false;
                    isRec = false;
                    close.setImageResource(R.drawable.ic_delete_forever_black_24dp);
                }else {
                    stopPlay();
                    if(mediaPlayer!=null)mediaPlayer.stop();
                    mediaPlayer.release();
                    if(file!=null&&file.exists())
                        file.delete();
                    initRec();
                    recMode = true;
                    tongle.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    close.setImageResource(R.drawable.ic_stop_black_24dp);
                }
            }
        });
        tongle.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recMode){
                    if(isRec){
                        pauseRec();
                        ///---
                        tongle.setImageResource(R.drawable.ic_mic_none_black_24dp);
                    }else {
                        startRec();
                        ///---
                        tongle.setImageResource(R.drawable.ic_mic_off_black_24dp);
                    }
                    isRec = !isRec;
                }else {
                    if(isPlay){
                        stopPlay();
                        ///
                        tongle.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    }else {
                        startPlay();
                        ///
                        tongle.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    }
                    isPlay = !isPlay;
                }

            }
        });
        ///
        textInputTil.setVisibility(View.GONE);
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        ////------
        initRec();
        ////------
    }

    private void initRec() {
        ////------
        path = Environment.getExternalStorageDirectory().getPath()+ "/tgps/sounds/"+System.currentTimeMillis()+".wav";
        ///
        checkDir(Environment.getExternalStorageDirectory().getPath()+ "/tgps/sounds/");
        ///
        file = new File(path);
        wavRecorder = new WavRecorder(path);
        try {
            if(!file.exists()) file.createNewFile();
            mediaPlayer = MediaPlayer.create(context, Uri.parse(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////------
        wavRecorder.setListener(new WavRecorder.OnAudioBufferChangeListener() {
            @Override
            public void onAudioBufferChange(final byte[] audioBuffer) {
                try {
                    waveformView.updateAudioData(audioBuffer);
                }catch (Exception e){}
            }

            @Override
            public void onAudioBufferChange(short[] shortData) {
                waveformView.updateAudioData(shortData);
            }
        });
    }

    private void stopPlay() {
        try {
            mediaPlayer.pause();
        }catch (Exception e){}
    }

    private void startPlay() {
        try {
            mediaPlayer.start();
        }catch (Exception e){}
    }

    private void startRec() {
        if(!wavRecorder.isRecording()){
            wavRecorder.startRecording();
        }
        wavRecorder.setOnPause(false);
    }

    private void pauseRec() {
        if(wavRecorder.isRecording()){
            wavRecorder.setOnPause(true);
        }
    }

    private void stopRec() {
        if(wavRecorder.isRecording()){
            wavRecorder.stopRecording();
            ///
            ////initRec();
            ///
            try {
                if(mediaPlayer==null)
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(path));
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initConfirm() {
        close.setVisibility(View.VISIBLE);
        check.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        ///
        label.setText(labelValue);
        //
        check.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = "validate";
                popupWindow.dismiss();
            }
        });
        close.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = null;
                popupWindow.dismiss();
            }
        });
        ///
        textInputTil.setVisibility(View.GONE);
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
    }

    private void initSelection() {
        spinner.setVisibility(View.VISIBLE);
        if(valuesSet!=null && valuesSet.length>0){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, valuesSet);
            spinner.setAdapter(adapter);
        }

        if(defaultValue!=null && defaultValue.length()>0 && valuesSet!=null){
            int i=0;
            for(String val : valuesSet){
                if(defaultValue.equals(val)){
                    spinner.setSelection(i);
                    break;
                }
                i++;
            }
        }
        textInput.setText(defaultValue);
        //
        items(true);
        //
        spinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        ///
        textInputTil.setVisibility(View.GONE);
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
    }

    private void initChecking() {
        spinner.setVisibility(View.VISIBLE);
        if(valuesSet!=null && valuesSet.length>0){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, valuesSet);
            spinner.setAdapter(adapter);
        }

        if(defaultValue!=null && defaultValue.length()>0 && valuesSet!=null){
            int i=0;
            for(String val : valuesSet){
                if(defaultValue.equals(val)){
                    spinner.setSelection(i);
                    break;
                }
                i++;
            }
        }
        textInput.setText(defaultValue);
        //
        items(false);
        //
        spinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
        ///
        textInputTil.setVisibility(View.GONE);
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
    }

    List<XPSItem> items = new ArrayList<>();
    public XPSItemAdapter itemAdapter;
    RecyclerView recyclerView;
    //
    GridLayoutManager layoutManager;
    private void items(final boolean radio) {
        recyclerView = popupView.findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.VISIBLE);
        itemAdapter = new XPSItemAdapter(context, items);
        itemAdapter.checkeds(checkeds);
        itemAdapter.color(primaryColor);
        layoutManager = new GridLayoutManager(context, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.addOnItemTouchListener(new XItemViewTouch(context,
                recyclerView, new XItemViewTouchCallback() {
            @Override
            public void onClick(View view, int position) {
                if(radio){
                    data = items.get(position).key;
                    popupWindow.dismiss();
                }else {
                    if(isChecked(items.get(position).key)) unChecked(items.get(position));
                    else checked(items.get(position));
                    itemAdapter.checkeds(checkeds);
                    itemAdapter.notifyDataSetChanged();
                }
            }/**

             @Override
             public void onTouchDown(View view, int position) {
             if(radio){
             data = items.get(position).key;
             popupWindow.dismiss();
             }else {
             if(isChecked(items.get(position).key)) unChecked(items.get(position));
             else checked(items.get(position));
             itemAdapter.checkeds(checkeds);
             itemAdapter.notifyDataSetChanged();
             ///----Log.x(context, items.get(position).name);
             }
             super.onTouchDown(view, position);
             }*/
        }));
    }

    private List<String> checkeds;
    public SpoozyChecking checked(String checked) {
        if(checked==null) return this;
        return checkeds(checked);
    }
    public SpoozyChecking checkeds(String... checkeds) {
        if(checkeds==null||checkeds.length==0) return this;
        this.checkeds = new ArrayList<>();
        for(String ch : checkeds)
            this.checkeds.add(ch);
        return this;
    }
    public SpoozyChecking unChecked(XPSItem item) {
        if(checkeds==null||checkeds.size()==0) return spoozyChecking;
        int i=0;
        for(String k : checkeds){
            if(k.equals(item.key))
                break;
            i++;
        }
        checkeds.remove(i);
        return spoozyChecking;
    }
    public SpoozyChecking checked(XPSItem item) {
        if(item==null) return spoozyChecking;
        if(this.checkeds==null) this.checkeds = new ArrayList<>();
        this.checkeds.add(item.key);
        return spoozyChecking;
    }
    public SpoozyChecking checkeds(List<XPSItem> items) {
        if(items==null || items.size()==0) return spoozyChecking;
        this.checkeds = new ArrayList<>();
        for(XPSItem i : items)
            this.checkeds.add(i.key);
        return spoozyChecking;
    }

    private boolean isChecked(String key) {
        if(checkeds==null||checkeds.size()==0) return false;
        for(String str : checkeds){
            if(key.equals(str)) return true;
        }
        return false;
    }

    public SpoozyChecking itemset(XPSItem... items) {
        if(items==null || items.length==0) return spoozyChecking;
        List<XPSItem> itemList = new ArrayList<>();
        for(XPSItem item : items)
            itemList.add(item);
        return itemset(itemList);
    }

    public SpoozyChecking itemset(List<XPSItem> items) {
        if(items==null || items.size()==0) return spoozyChecking;
        spoozyChecking.items.clear();
        spoozyChecking.items.addAll(items);
        return spoozyChecking;
    }

    private int aColor(float a, int color) {
        return Color.argb((int) (255*a), Color.red(color), Color.green(color), Color.blue(color));
    }

    private void initCodeInput() {
        codes.add((EditText) popupView.findViewById(R.id.code_1));
        codes.add((EditText) popupView.findViewById(R.id.code_2));
        codes.add((EditText) popupView.findViewById(R.id.code_3));
        codes.add((EditText) popupView.findViewById(R.id.code_4));
        //
        codeArea.setVisibility(View.VISIBLE);
        resendCode.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        label.setVisibility(View.VISIBLE);
        //
        textInputTil.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        //
        setCodesListeners(codes);
        //
        label.setText(labelValue);
        label.setDetectPhoneNumbers(true);
        label.setPhoneNumberColorCode(context.getResources().getColor(R.color.colorPrimary));
        //
        regexFormat = new FormattedText();
        regexFormat.setBold();
        regexFormat.setColor(primaryColor);
        regexFormat.setUnderline();
        //
        resendCode.setText(resendCodeValue);
        resendCode.setRegex(resendCodeRegex);
        resendCode.setRegexFormat(regexFormat);
        resendCode.setDetectRegex(true);
        resendCode.setSmartTextCallback(new SmartTextCallback() {
            @Override
            public void regexClick(String value) {
                initProgresser(++resendCodeCount, resendCode);
                progresser.start();
                if(xpListener!=null) xpListener.onSendCode();
            }
        });
        //
        close.setOnFgClickListener(new api.pot.gl.xiv.tools.Forgrounder.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void initTextInput() {
        textInputTil.setVisibility(View.VISIBLE);
        textInput.setHint(labelValue);
        textInputTil.setHint(labelValue);
        if(defaultValue!=null && defaultValue.length()>0)
            textInput.setText(defaultValue);
        //
        codeArea.setVisibility(View.GONE);
        resendCode.setVisibility(View.GONE);
        close.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
    }

    // validate fields
    private boolean validate(EditText editText, TextInputLayout textInputLayout) {
        if (editText.getText().toString().trim().length() > 0) {
            editText.clearFocus();
            textInputLayout.setErrorEnabled(false);
            //
            if( (RIGHT_CODE==null || RIGHT_CODE.length()==0) || (textInput.getText().toString().equals(RIGHT_CODE) )){
                data = textInput.getText().toString();
                return true;
            }
        }
        data = null;
        editText.requestFocus(); // set focus on fields
        textInputLayout.setError(context.getResources().getString(R.string.input_invalide_msg)); // set error message
        container.notifyError(false, true);
        return false;
    }

    private boolean checkValuesSet() {
        if(valuesSet==null || valuesSet.length==0) return false;
        for(String val : valuesSet){
            if(textInput.getText().toString().equals(val))
                return true;
        }
        return false;
    }

    private int KEY_CODE_DELETE = 67;
    private String lastInput;
    private void setCodesListeners(final List<EditText> codes) {
        for (int i=0;i<codes.size();i++){
            final EditText code = codes.get(i);
            final int finalI = i;
            code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    focusCode(code,codes);
                }
            });
            code.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                @Override
                public void afterTextChanged(Editable editable) {
                    if(code.getText().toString().length()==0 && finalI >0)
                        focusCode(codes.get(finalI-1), codes);
                    else if(finalI <codes.size()-1)
                        focusCode(codes.get(finalI+1), codes);
                }
            });
        }
    }

    private void focusCode(EditText code, List<EditText> codes) {
        for(EditText c : codes)
            c.setBackground(context.getResources().getDrawable(R.drawable.input_once_bg_not_focused));
        code.setBackground(context.getResources().getDrawable(R.drawable.input_once_bg_focused));
        code.requestFocus();
    }

    private boolean isCodesValide(List<EditText> codes) {
        String script = "";
        for (EditText code : codes){
            if(code.getText().toString().length()==0) {
                code.setBackground(context.getResources().getDrawable(R.drawable.input_once_bg_error));
                return false;
            }
            script+=code.getText().toString();
        }
        data = script;
        return script.equals(RIGHT_CODE);
    }

    private void initProgresser(int count, final XTextView xTextView) {
        if(xpListener!=null) xpListener.onResendCodeCountChange(count);
        //
        progresser = ValueAnimator.ofInt(count*context.getResources().getInteger(R.integer.system_resend_code_delay), 0);
        progresser.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                xTextView.setText(context.getResources().getString(R.string.phone_check_resend_code)+" ("+getMinSecTime(val)+")");
            }
        });
        progresser.setDuration(count*context.getResources().getInteger(R.integer.system_resend_code_delay)*1000);
        progresser.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                xTextView.setEnabled(false);
                xTextView.setAlpha(0.3f);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                xTextView.setText(context.getResources().getString(R.string.phone_check_resend_code));
                xTextView.setEnabled(true);
                xTextView.setAlpha(1f);
            }
        });
        progresser.setInterpolator(null);
    }

    private String getMinSecTime(int sec) {
        return (sec/60)+":"+(sec%60);
    }
}
