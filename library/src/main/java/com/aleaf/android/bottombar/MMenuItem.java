package com.aleaf.android.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;

import com.aleaf.android.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tory
 * @date 2019/4/30
 * @des:
 */
public class MMenuItem {

    int id;
    String title;
    int iconRes;
    boolean isMain;

    public MMenuItem(int id, String title, int iconRes, boolean isMain) {
        this.id = id;
        this.title = title;
        this.iconRes = iconRes;
        this.isMain = isMain;
    }
}
