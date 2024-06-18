package github.kutouzi.actassistant.view.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import github.kutouzi.actassistant.R;


public class ToggleButton extends MaterialButton {

    private boolean isToggled = false;

    public ToggleButton(Context context) {
        super(context);
        init(null);
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ToggleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToggleButton);
            isToggled = a.getBoolean(R.styleable.ToggleButton_toggled, false);
            a.recycle();
        }
        updateButton();
        setOnClickListener(v -> toggle());
    }

    public void toggle() {
        isToggled = !isToggled;
        updateButton();
    }

    public boolean isToggled() {
        return isToggled;
    }

    public void setToggled(boolean toggled) {
        isToggled = toggled;
        updateButton();
    }
    private void updateButton() {
        if (isToggled) {
            setTextAppearance(R.style.Widget_ToggleButton_On);
        } else {
            setTextAppearance(R.style.Widget_ToggleButton_Off);
        }
    }
}
