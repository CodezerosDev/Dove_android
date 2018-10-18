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
public class EditText_poppins_medium extends android.support.v7.widget.AppCompatEditText {


    public EditText_poppins_medium(Context context) {
        super(context);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins_medium.ttf"));
    }

    public EditText_poppins_medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setBackgroundColor(Color.parseColor("#fff"));
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins_medium.ttf"));

    }

    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }


}