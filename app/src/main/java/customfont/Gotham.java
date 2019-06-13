package customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class Gotham extends TextView {

    public Gotham(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Gotham(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Gotham(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DroidSerif-Regular.ttf");
            setTypeface(tf);
        }
    }

}