package com.monenco.insights.views;

import android.os.Build;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monenco.insights.R;
import com.monenco.insights.StringHelper;
import com.monenco.insights.models.ArticlePart;

public class ArticlePartViewHolder extends AbstractModelViewHolder<ArticlePart> {
    private TextView title;
    private TextView caption;
    private ImageViewHolder image;
    private TextView content;

    public ArticlePartViewHolder(ViewGroup parent) {
        super(parent, R.layout.article_part_view_layout);
        title = (TextView) findViewById(R.id.article_part_title);
        caption = (TextView)findViewById(R.id.article_part_caption);
        image = new ImageViewHolder(findViewById(R.id.article_part_image), R.color.color_placeholder);
        content = (TextView) findViewById(R.id.article_part_content);
    }

    @Override
    public void onModelChange() {
        if (StringHelper.notNone(getModel().title)) {
            title.setText(getModel().title);
            caption.setText(getModel().title);
            title.setVisibility(View.VISIBLE);
            caption.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
            caption.setVisibility(View.GONE);
        }
        if (getModel().type == ArticlePart.TYPE_IMAGE) {
            content.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            image.setVisible(true);
            image.setImage(getModel().image);
        } else {
            content.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            image.setVisible(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                content.setText(Html.fromHtml(getModel().content, Html.FROM_HTML_MODE_COMPACT));
            } else {
                content.setText(Html.fromHtml(getModel().content));
            }
        }
    }

    public void setIsLarge(boolean isLarge) {
        if (isLarge) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_xxl));
            content.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_xl));
        } else {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_l));
            content.setTextSize(TypedValue.COMPLEX_UNIT_PX, getDimen(R.dimen.text_size_m));
        }
    }
}
