package com.monenco.insights.views;

import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.models.Translator;

public class AboutUsFragment extends AbstractFragment implements View.OnClickListener {
    private View back;

    @Override
    public int getLayoutResId() {
        return R.layout.about_us_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        TextView title = (TextView) findViewById(R.id.about_us_fragment_title);
        title.setText(getStringValue(R.string.about_us));
        back = findViewById(R.id.about_us_fragment_back);
        if (Translator.isPersian()) {
            back.setScaleX(-1);
        }
        back.setOnClickListener(this);
        TextView aboutUs = (TextView) findViewById(R.id.about_us_fragment_about_us);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutUs.setText(Html.fromHtml(getMainView().getConfigs().aboutUs, Html.FROM_HTML_MODE_COMPACT));
        } else {
            aboutUs.setText(Html.fromHtml(getMainView().getConfigs().aboutUs));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            popFragment();
        }
    }

}
