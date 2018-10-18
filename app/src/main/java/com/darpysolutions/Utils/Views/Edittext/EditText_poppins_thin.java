package com.darpysolutions.Utils.Views.Edittext;

/**
 * Created by webclues on 18-Apr-17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by webclues on 1/17/2017.
 */


/**
 * Created by webclues on 6/6/2016.
 */
public class EditText_poppins_thin extends android.support.v7.widget.AppCompatEditText {


    public EditText_poppins_thin(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins_thin.ttf"));
    }

    public EditText_poppins_thin(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setBackgroundColor(Color.parseColor("#fff"));
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins_thin.ttf"));

    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }


}