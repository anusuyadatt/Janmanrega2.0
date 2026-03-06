package nic.hp.ccmgnrega.common;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RedAsterisk {

    public static SpannableString Spantext(final String text, final int start, final int end) {
        SpannableString spString = new SpannableString(text);
        spString.setSpan(new ForegroundColorSpan(Color.RED),start,end,0);
        return spString;
    }

    public static void setAstrikOnTextViewAndEditText(final TextView tv, final EditText et, final String str)
    {
        tv.setText(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
        et.setHint(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }

    public static void setAstrikOnEditText(final EditText et, final String str)
    {
        et.setHint(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }
    public static void setAstrikOnTextInputEditText(final TextInputEditText et, final String str)
    {
        et.setHint(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }
    public static void setAstrikOnTextInputTextInputLayout(final TextInputLayout et, final String str)
    {
        et.setHint(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }

    public static void setAstrikOnTextView(final TextView tv, final String str)
    {
        tv.setText(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }

    public static void setAstrikOnBuilder(final TextView tv, final SpannableStringBuilder str)
    {
        tv.setText(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
        tv.setText(RedAsterisk.Spantext(str + " *", str.length() + 1, str.length() + 2));
    }
}
