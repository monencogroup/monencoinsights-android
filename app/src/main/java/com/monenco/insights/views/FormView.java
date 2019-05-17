package com.monenco.insights.views;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.monenco.insights.R;
import com.monenco.insights.Settings;
import com.monenco.insights.models.Model;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

public class FormView extends AbstractViewHolder implements View.OnClickListener {
    public interface FormHandler {
        void handleParams(RequestParams params);
    }

    private HashMap<FormTextFieldView, String> fields;
    private LinearLayout fieldsContainer;
    private ButtonView submit;
    private HashMap<String, String> values;
    private FormHandler formHandler;

    public FormView(View rootView, int submitResId, FormHandler formHandler) {
        super(rootView);
        this.formHandler = formHandler;
        fields = new HashMap<>();
        values = new HashMap<>();
        fieldsContainer = (LinearLayout) findViewById(R.id.form_view_fields_container);
        submit = new ButtonView(findViewById(R.id.form_view_submit), submitResId);
        submit.setOnClickListener(this);

    }

    public LinearLayout getFieldsContainer() {
        return fieldsContainer;
    }

    public void addField(String name, int iconResId, int placeholderResId, int type) {
        FormTextFieldView field = FormTextFieldView.getFormTextView(this, iconResId, placeholderResId, type);
        fieldsContainer.addView(field.getRootView());
        fields.put(field, name);
    }

    @Override
    public void onClick(View view) {
        boolean isValid = validateForm();
        if (isValid) {
            RequestParams params = new RequestParams();
            for (String key : values.keySet()) {
                params.put(key, values.get(key));
            }
            formHandler.handleParams(params);
        } else {
            Toast.makeText(getContext(), getStringValue(R.string.some_fields_are_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void notifyChange(FormTextFieldView field, String value) {
        String name = fields.get(field);
        values.put(name, value);
    }

    private boolean validateForm() {
        boolean valid = true;
        for (FormTextFieldView field : fields.keySet()) {
            valid = valid && field.isValid();
            field.showError(true);
        }
        return valid;
    }

    public void setLoading(boolean loading) {
        this.submit.setLoading(loading);
    }
}
