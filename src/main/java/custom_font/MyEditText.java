package custom_font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;


/**
 * Created by one on 3/12/15.
 */
public class MyEditText extends TextInputLayout {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Lato-Regular.ttf");
            setTypeface(tf);
        }
    }

}