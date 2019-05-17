package com.monenco.insights.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.monenco.insights.R;
import com.monenco.insights.models.Category;
import com.monenco.insights.models.Translator;
import com.monenco.insights.requester.Request;
import com.monenco.insights.requester.RequestParams;
import com.monenco.insights.requester.listeners.OnResponseReceivedListener;
import com.monenco.insights.requester.utils.URLs;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectFragment extends AbstractFragment implements View.OnClickListener, OnResponseReceivedListener {
    private View back;
    private ArrayList<Category> categories;
    private ButtonView submit;
    private ListView categoriesList;

    @Override
    public int getLayoutResId() {
        return R.layout.category_select_fragment_layout;
    }

    @Override
    public void initialArguments() {

    }

    @Override
    public void initialViews() {
        TextView title = (TextView) findViewById(R.id.category_select_title);
        title.setText(getStringValue(R.string.select_categories));
        back = findViewById(R.id.category_select_fragment_back);
        if (Translator.isPersian()) {
            back.setScaleX(-1);
        }
        if (getMainView().isEmpty()) {
            back.setVisibility(View.GONE);
        }
        back.setOnClickListener(this);
        TextView message = (TextView) findViewById(R.id.category_select_message);
        String messageText = getStringValue(R.string.select_categories_message)+
                " "+getMainView().getConfigs().minCategorySelectCount+" "+
                getStringValue(R.string.select_categories_message_after);
        message.setText(messageText);
        TextView note = (TextView) findViewById(R.id.category_select_note);
        note.setText(getStringValue(R.string.you_can_change_this));
        submit = new ButtonView(findViewById(R.id.category_select_submit), R.string.submit);
        submit.setOnClickListener(this);
        submit.setEnable(false);
        categories = new ArrayList<>();
        for(Category category:getMainView().getCategories()){
            Category c = new Category();
            c.isFavorite = category.isFavorite;
            c.previousIsFavorite = c.isFavorite;
            c.icon = category.icon;
            c.name = category.name;
            c.id = category.id;
            categories.add(c);
        }
        categoriesList = (ListView) findViewById(R.id.category_select_list);
        categoriesList.setAdapter(new CategoryListAdapter(getContext(), R.layout.category_view_layout));
        notifyChange();
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            popFragment();
        } else {
            RequestParams params = new RequestParams();
            List<RequestParams> idSet = new ArrayList<>();
            for (Category category : categories) {
                if (category.isFavorite != category.previousIsFavorite) {
                    RequestParams categoryParams = new RequestParams();
                    categoryParams.put("id", category.id);
                    categoryParams.put("newState",category.isFavorite);
                    idSet.add(categoryParams);
                }
            }
            params.putAllToList("toggleSet", idSet);
            if (!idSet.isEmpty()) {
                submit.setLoading(true);
                Request request = new Request(Request.METHOD_POST, URLs.CATEGORY +URLs.LIST+ URLs.TOGGLE);
                request.setParams(params);
                request.setResponseReceivedListener(this);
                request(request);
            } else {
                popFragment();
            }
        }
    }

    public void notifyChange() {
        int favoriteCount = 0;
        for (Category category : categories) {
            if (category.isFavorite) {
                favoriteCount++;
            }
        }
        submit.setEnable(favoriteCount >= getMainView().getConfigs().minCategorySelectCount);
    }

    @Override
    public void onSuccess(int requestId, int statusCode) {
        submit.setLoading(false);
        Toast.makeText(getContext(), getStringValue(R.string.select_categories_success), Toast.LENGTH_LONG).show();
        getMainView().setCategories(categories);
        if (getMainView().isEmpty()) {
            getMainView().popFragment(1);
            pushFragment(new SearchFragment());
        } else {
            popFragment(true);
        }
    }

    @Override
    public void onFailure(int requestId, int statusCode) {
        submit.setLoading(false);
        Toast.makeText(getContext(), getStringValue(R.string.connection_failed_please_try_again), Toast.LENGTH_SHORT).show();
    }

    private class CategoryListAdapter extends ArrayAdapter<Category> {

        public CategoryListAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Category getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CategoryViewHolder holder;
            if (convertView == null || convertView.getTag(R.id.view_0) == null) {
                holder = new CategoryViewHolder(parent, CategorySelectFragment.this);
                convertView = holder.getRootView();
                convertView.setTag(R.id.view_0, holder);
            } else {
                holder = (CategoryViewHolder) convertView.getTag(R.id.view_0);
            }
            holder.setModel(getItem(position));
            return convertView;
        }
    }
}
