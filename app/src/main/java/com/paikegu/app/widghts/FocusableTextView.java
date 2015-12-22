package com.xiankezu.sirceo.widghts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 作者：杨剑飞.
 * 邮箱：847564732@qq.com
 * 创建日期:2015/7/6
 * 功能：可以滚动的textview
 */
public class FocusableTextView extends TextView {

    public FocusableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusableTextView(Context context) {
        super(context);
    }

    /**
     * 欺骗了系统
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
