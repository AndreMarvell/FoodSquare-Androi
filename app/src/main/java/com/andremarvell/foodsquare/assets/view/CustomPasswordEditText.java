package com.andremarvell.foodsquare.assets.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.andremarvell.foodsquare.R;


/**
 *
 *
 * sub class of {@link android.widget.AutoCompleteTextView} that includes a clear (dismiss / close) button with
 * a OnClearListener to handle the event of clicking the button
 *
 */
public class CustomPasswordEditText extends EditText {
    // was the text just cleared?
    boolean visible = false;

    // if not set otherwise, the default clear listener clears the text in the
    // text view
    private OnClearListener defaultClearListener = new OnClearListener() {

        @Override
        public void onClear() {
            CustomPasswordEditText et = CustomPasswordEditText.this;
            if(!visible) {
                et.setTransformationMethod(null);

            } else {
                et.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    };

    private OnClearListener onClearListener = defaultClearListener;

    // The image we defined for the clear button
    public Drawable imgPassVisible;
    public Drawable imgPassInvisible = getResources().getDrawable(
            R.drawable.icone_oeil_off);

    public interface OnClearListener {
        void onClear();
    }

    /* Required methods, not used in this implementation */
    public CustomPasswordEditText(Context context) {
        super(context);
        init();
    }

    /* Required methods, not used in this implementation */
    public CustomPasswordEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /* Required methods, not used in this implementation */
    public CustomPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        setTypeface(Typeface.DEFAULT);
        setTransformationMethod(new PasswordTransformationMethod());
        // Set the bounds of the button
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        Drawable dr = getResources().getDrawable(R.drawable.icone_oeil);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        imgPassVisible = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 30, 30, true));

        Drawable dr2 = getResources().getDrawable(R.drawable.icone_oeil_off);
        Bitmap bitmap2 = ((BitmapDrawable) dr2).getBitmap();
        imgPassInvisible = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 30, 30, true));


        // if the clear button is pressed, fire up the handler. Otherwise do nothing
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CustomPasswordEditText et = CustomPasswordEditText.this;

                if (et.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                if (event.getX() > et.getWidth() - et.getPaddingRight()	- imgPassVisible.getIntrinsicWidth()) {
                    onClearListener.onClear();
                    visible = !visible;
                    showClearButton();
                }
                return false;
            }
        });

        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setCompoundDrawablesWithIntrinsicBounds(null, null, ((getText().toString().equals(""))?null:(visible ? imgPassInvisible : imgPassVisible)), null);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });


    }

    public void setImgClearButton(Drawable imgClearButton) {
        this.imgPassVisible = imgClearButton;
    }

    public void setOnClearListener(final OnClearListener clearListener) {
        this.onClearListener = clearListener;
    }

    public void showClearButton() {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null,visible ? imgPassInvisible :  imgPassVisible, null);
        this.setSelection(this.getText().length());

    }


}