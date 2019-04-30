package com.aleaf.android.bottombar;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Log;
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
public class BottomBarUtil {

    private static final int[] APPCOMPAT_CHECK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimary
    };

    static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }

    public static List<MMenuItem> inflateMenu(Context context, int resourceId) {
        List<MMenuItem> items = new ArrayList<>();
        try (XmlResourceParser parser = context.getResources().getXml(resourceId)) {
            AttributeSet attributeSet = Xml.asAttributeSet(parser);
            int depth = parser.getDepth();
            int type;
            while (((type = parser.next()) != XmlResourceParser.END_TAG ||
                    parser.getDepth() > depth) && type != XmlResourceParser.END_DOCUMENT) {
                if (type == XmlResourceParser.START_TAG && "MMenuItem".equals(parser.getName())) {
                    TypedArray a =
                            context.obtainStyledAttributes(attributeSet, R.styleable.MMenuItem);
                    int id = a.getResourceId(R.styleable.MMenuItem_mmt_id, 0);
                    String title = a.getString(R.styleable.MMenuItem_mmt_title);
                    int icon = a.getResourceId(R.styleable.MMenuItem_mmt_icon, 0);
                    boolean isMain = a.getBoolean(R.styleable.MMenuItem_mmt_main, false);
                    items.add(new MMenuItem(id, title, icon, isMain));
                    Log.d("BottomBarUtil", "inflateMenu: id="+id);
                    a.recycle();
                }
            }
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
        return items;
    }
}
