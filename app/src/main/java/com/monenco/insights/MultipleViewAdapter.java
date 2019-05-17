package com.monenco.insights;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.monenco.insights.models.Model;
import com.monenco.insights.views.AbstractModelViewHolder;

import java.util.ArrayList;
import java.util.HashMap;


public class MultipleViewAdapter extends BaseAdapter {


    public interface ViewGenerator {
        AbstractModelViewHolder<? extends Model> generateViewHolder(ViewGroup parent, String typeKey);
    }

    private ArrayList<Pair<Model, Integer>> dataArray;
    private HashMap<String, Integer> typesValues;
    private ViewGenerator viewGenerator;
    private int typesCount;

    public MultipleViewAdapter(ViewGenerator viewGenerator) {
        this.viewGenerator = viewGenerator;
        dataArray = new ArrayList<>();
        typesValues = new HashMap<>();
        typesCount = 0;
    }
    public boolean notContains(String typeData) {
        boolean contains = false;
        if (typesValues.containsKey(typeData)) {
            int typeValue = typesValues.get(typeData);
            for (Pair<Model, Integer> data : dataArray) {
                if (data.second == typeValue) {
                    contains = true;
                }
            }
        }
        return !contains;
    }
    public void addData(Model data, String type) {
        addData(data, type, dataArray.size());
    }

    public void addData(Model data, String typeKey, int index) {
        int typeValue;
        if (typesValues.containsKey(typeKey)) {
            typeValue = typesValues.get(typeKey);
        } else {
            typeValue = typesCount;
            typesCount++;
            typesValues.put(typeKey, typeValue);
        }
        dataArray.add(index, new Pair<>(data, typeValue));

    }

    public void removeAll(String toDeleteTypeKey) {
        if (typesValues.containsKey(toDeleteTypeKey)) {
            int typeValue = typesValues.get(toDeleteTypeKey);
            ArrayList<Pair<Model, Integer>> rubbish = new ArrayList<>();
            for (Pair<Model, Integer> data : dataArray) {
                if (data.second == typeValue) {
                    rubbish.add(data);
                }
            }
            for (Pair<Model, Integer> toDelete : rubbish) {
                dataArray.remove(toDelete);
            }
        }
    }

    public void clear() {
        dataArray.clear();
    }


    public void remove(Model toDelete) {
        for (Pair<Model, Integer> data : dataArray) {
            if (data.first.equals(toDelete)) {
                dataArray.remove(data);
                break;
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataArray.get(position).second;
    }

    @Override
    public int getViewTypeCount() {
        if (typesCount == 0) {
            return 1;
        } else {
            return typesCount;
        }
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Model getItem(int position) {
        return dataArray.get(position).first;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int typeValue = getItemViewType(position);
        String typeKey = null;
        for (String key : typesValues.keySet()) {
            if (typesValues.get(key) == typeValue) {
                typeKey = key;
                break;
            }
        }
        if (typeKey == null) {
            throw new IllegalStateException("No type for such position!");
        }
        AbstractModelViewHolder<? extends Model> holder;
        if (convertView == null || convertView.getTag(getResourceIdForType(typeValue)) == null) {
            holder = viewGenerator.generateViewHolder(parent, typeKey);
            convertView = holder.getRootView();
            convertView.setTag(getResourceIdForType(typeValue), holder);
        } else {
            holder = (AbstractModelViewHolder<? extends Model>) convertView.getTag(getResourceIdForType(typeValue));
        }
        holder.setModel(getItem(position));
        return convertView;
    }

    private static int getResourceIdForType(int typeValue) {
        switch (typeValue) {
            case 0:
                return R.id.view_0;
            case 1:
                return R.id.view_1;
            case 2:
                return R.id.view_2;
            case 3:
                return R.id.view_3;
            case 4:
                return R.id.view_4;
            case 5:
                return R.id.view_5;
            case 6:
                return R.id.view_6;
            case 7:
                return R.id.view_7;
            case 8:
                return R.id.view_8;
            case 9:
                return R.id.view_9;
            case 10:
                return R.id.view_10;
            case 11:
                return R.id.view_11;
            case 12:
                return R.id.view_12;
            case 13:
                return R.id.view_13;
            case 14:
                return R.id.view_14;
            case 15:
                return R.id.view_15;
            case 16:
                return R.id.view_16;
            case 17:
                return R.id.view_17;
            case 18:
                return R.id.view_18;
            case 19:
                return R.id.view_19;
            case 20:
                return R.id.view_20;
            case 21:
                return R.id.view_21;
            case 22:
                return R.id.view_22;
            case 23:
                return R.id.view_23;
            case 24:
                return R.id.view_24;
            case 25:
                return R.id.view_25;
            case 26:
                return R.id.view_26;
            case 27:
                return R.id.view_27;
            case 28:
                return R.id.view_28;
            case 29:
                return R.id.view_29;
            default:
                throw new IllegalStateException("More than 30 types in list view");
        }
    }
}
