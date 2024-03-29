package com.kliff.scorecard.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class GothamBold extends TextView {

    public GothamBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GothamBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GothamBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DroidSerif-Bold.ttf");
            setTypeface(tf);
        }
    }

}