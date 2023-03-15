package com.bertilandcorp.spoozyaccountapi;

import android.graphics.Color;

public class XPSItem {
    public String key;
    public String label;
    public String name;
    ///
    public int color = Color.TRANSPARENT;
    ///
    public boolean checked = false;

    public XPSItem(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public XPSItem(String key, String label, String name) {
        this.key = key;
        this.label = label;
        this.name = name;
    }

    public XPSItem(String key, String label, String name, int color) {
        this.key = key;
        this.label = label;
        this.name = name;
        this.color = color;
    }

    public XPSItem(Object key, Object label, Object name, int color) {
        this.key = key+"";
        this.label = label+"";
        this.name = name+"";
        this.color = color;
    }

    public XPSItem checked(boolean checked) {
        this.checked = checked;
        return this;
    }
}
