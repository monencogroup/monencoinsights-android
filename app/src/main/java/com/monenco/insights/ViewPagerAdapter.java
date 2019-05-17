package com.monenco.insights;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.monenco.insights.models.Model;
import com.monenco.insights.views.AbstractModelViewHolder;

import java.util.ArrayList;


public class ViewPagerAdapter<E extends Model> extends PagerAdapter {

    public interface ViewHolderGenerator<E extends Model> {
        AbstractModelViewHolder<E> generateViewHolder(ViewGroup parent);
    }

    private ArrayList<E> dataArray;
    private ViewHolderGenerator viewHolderGenerator;

    public ViewPagerAdapter(ViewHolderGenerator viewHolderGenerator) {
        super();
        this.viewHolderGenerator = viewHolderGenerator;
        dataArray = new ArrayList<>();
    }


    public void addData(E data) {
        addData(data, dataArray.size());
    }

    public void addData(E data, int index) {
        dataArray.add(index, data);
    }

    public void clear() {
        dataArray.clear();
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View collection, @NonNull Object object) {
        return collection == ((AbstractModelViewHolder<E>) object).getRootView();
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {
        AbstractModelViewHolder<E> viewHolder = viewHolderGenerator.generateViewHolder(viewGroup);
        viewHolder.setModel(dataArray.get(position));
        viewGroup.addView(viewHolder.getRootView());
        return viewHolder;

    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    public void destroyItem(@NonNull ViewGroup viewGroup, int position, @NonNull Object object) {
        AbstractModelViewHolder<E> viewHolder = (AbstractModelViewHolder<E>) object;
        viewGroup.removeView(viewHolder.getRootView());
    }


}
