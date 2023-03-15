package com.bertilandcorp.spoozyaccountapi;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import api.pot.text.xtv.XTextView;
import api.pot.view.xl.XLayout;

public class XPSItemAdapter extends RecyclerView.Adapter<XPSItemAdapter.ViewHolder> {


    Context mContext;
    List<XPSItem> items;
    private XPSItem item;

    private boolean usingCheckbox = false;

    public XPSItemAdapter(Context mContext, List<XPSItem> mData) {
        this.mContext = mContext;
        this.items = mData;
        this.checkeds = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.xp_item_view,viewGroup,false);
        return new ViewHolder(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item = items.get(position);
        ///
        ///
        if(item.checked || isChecked(item.key)) holder.nameXL.setBackgroundRGBColor(item.color);
        else holder.nameXL.setBackgroundColor(Color.WHITE);
        ///
        holder.name.setText((position+1)+".  "+item.name);
        holder.container.setBackgroundColor(item.color);
        ///
        if(item.label!=null && item.label.length()>0){
            holder.label.setText(item.label);
            ///--
            if(position!=0 && items.get(position-1).label.equals(item.label))
                holder.labelArea.setVisibility(View.GONE);
            else
                holder.labelArea.setVisibility(View.VISIBLE);
        }else {
            holder.labelArea.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private boolean useMargin = true;
    public void useMagin(boolean useMargin) {
        this.useMargin = useMargin;
    }

    private List<String> checkeds;
    public void checked(String checked) {
        checkeds(checked);
    }
    public void checkeds(String... checkeds) {
        if(checkeds==null) return;
        this.checkeds = new ArrayList<>();
        for(String ch : checkeds)
            this.checkeds.add(ch);
    }
    public void checkeds(List<String> checkeds) {
        if(checkeds==null) return;
        this.checkeds = new ArrayList<>();
        for(String ch : checkeds)
            this.checkeds.add(ch);
        ///--if(checkeds==null||checkeds.size()==0) return;
        /**this.checkeds = new ArrayList<>();
        this.checkeds = checkeds;*/
    }

    private boolean isChecked(String key) {
        if(checkeds==null||checkeds.size()==0) return false;
        for(String str : checkeds){
            if(key.equals(str)) return true;
        }
        return false;
    }

    private int primaryColor = Color.WHITE;
    public void color(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        LinearLayout labelArea;
        XTextView label;
        XLayout nameXL;
        XTextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.container);
            labelArea = itemView.findViewById(R.id.labelArea);
            label = itemView.findViewById(R.id.label);
            nameXL = itemView.findViewById(R.id.nameXL);
            name = itemView.findViewById(R.id.name);

            nameXL.getForgrounder().setColor(primaryColor);

            nameXL.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }
}

