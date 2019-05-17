package com.monenco.insights.views;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.monenco.insights.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTextFieldView extends AbstractViewHolder implements TextWatcher {
    public static final int TYPE_USERNAME = 1;
    public static final int TYPE_PASSWORD = 2;
    public static final int TYPE_EMAIL = 3;

    private int type;
    private FormView formView;
    private EditText editText;
    private TextView error;
    private boolean valid = false;
    private String errorText;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static FormTextFieldView getFormTextView(FormView formView, int iconResId, int placeholderResId, int type) {
        switch (type) {
            case TYPE_EMAIL:
                return new FormTextFieldView(formView, iconResId, placeholderResId, R.layout.form_email_field_view_layout, type);
            case TYPE_PASSWORD:
                return new FormTextFieldView(formView, iconResId, placeholderResId, R.layout.form_password_field_view_layout, type);
            case TYPE_USERNAME:
                return new FormTextFieldView(formView, iconResId, placeholderResId, R.layout.form_text_field_view_layout, type);
        }
        return null;
    }

    private FormTextFieldView(FormView formView, int iconResId, int placeholderResId, int layoutResourceId, int type) {
        super(formView.getFieldsContainer(), layoutResourceId);
        this.formView = formView;
        this.type = type;
        ImageView icon = (ImageView) findViewById(R.id.form_field_view_icon);
        icon.setImageResource(iconResId);
        editText = (EditText) findViewById(R.id.form_field_view_text);
        editText.setHint(getStringValue(placeholderResId));
        error = (TextView) findViewById(R.id.form_field_view_error);
        editText.addTextChangedListener(this);
        errorText = getStringValue(R.string.this_field_is_required);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String newText = editText.getText().toString();
        formView.notifyChange(this, newText);
        showError(false);
        valid = true;
        if (newText.isEmpty()) {
            valid = false;
            errorText = getStringValue(R.string.this_field_is_required);
        } else {
            switch (type) {
                case TYPE_PASSWORD:
                    if (newText.length() < 6) {
                        valid = false;
                        errorText = getStringValue(R.string.password_length_error);
                    }
                    break;
                case TYPE_USERNAME:
                    if (newText.length() < 6) {
                        valid = false;
                        errorText = getStringValue(R.string.username_length_error);
                    }
                    break;
                case TYPE_EMAIL:
                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(newText);
                    valid = matcher.find();
                    if (!valid) {
                        errorText = getStringValue(R.string.email_error);
                    }
            }
        }
    }

    public void showError(boolean show) {
        if (!show) {
            error.setVisibility(View.GONE);
        } else {
            if (!valid) {
                error.setText(errorText);
                error.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean isValid() {
        return valid;
    }
}
