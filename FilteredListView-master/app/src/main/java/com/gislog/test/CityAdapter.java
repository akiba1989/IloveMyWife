package com.gislog.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by salih.yalcin on 21.2.2017.
 */

class CityAdapter extends ArrayAdapter<City> {
    private ArrayList<City> originalItems;
    private ArrayList<City> filteredItems;
    private ArrayList<City> notFilteredItems;
    private ArrayList<City> checkedItems;
    private Filter filter;
    private int[] rainbow;

    CityAdapter(Activity context, ArrayList<City> arrayList) {
        super(context, R.layout.adapter_list, arrayList);
        this.originalItems = arrayList;
        this.filteredItems = arrayList;
        this.notFilteredItems = new ArrayList<>(arrayList);
        this.filter = new ModelFilter();
        this.rainbow = context.getResources().getIntArray(R.array.rainbow);
        this.checkedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ModelFilter();
        }
        return filter;
    }

    @Override
    public int getCount() {
        return originalItems.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;

        final City cityItem = originalItems.get(position);

        if (convertView == null) {

            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_list, parent, false);
            holder.cityName = (TextView) convertView.findViewById(R.id.cityNameText);
            holder.labelName = (ImageView) convertView.findViewById(R.id.labelNameText);
            holder.cityCheck = (CheckBox) convertView.findViewById(R.id.cityCheckBox);

            convertView.setTag(holder);
            holder.cityCheck.setTag(cityItem);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.cityCheck.getTag();
        }

        holder.cityName.setText(cityItem.getCityName());
        holder.cityCheck.setChecked(cityItem.isChecked());
        GradientDrawable bgShape = (GradientDrawable) holder.labelName.getBackground().getCurrent();
        bgShape.setColor(rainbow[position % 10]);

        holder.cityCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox cb = (CheckBox) v;
                if (originalItems.get(position).isChecked()) {
                    cb.setSelected(false);
                    originalItems.get(position).setChecked(false);
                    notifyDataSetChanged();
                } else {
                    cb.setSelected(true);
                    originalItems.get(position).setChecked(true);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }


    @Override
    public City getItem(int i) {
        return originalItems.get(i);
    }


    private class ModelFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix.length() == 0) {
                ArrayList<City> list = new ArrayList<>(notFilteredItems);
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<City> list = new ArrayList<>(notFilteredItems);
                final ArrayList<City> nlist = new ArrayList<>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    final City singleItem = list.get(i);
                    final String value = singleItem.getCityName().toLowerCase();
                    if (value.contains(prefix)) {
                        nlist.add(singleItem);
                    }
                    results.values = nlist;
                    results.count = nlist.size();
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredItems = (ArrayList<City>) results.values;
            originalItems.clear();
            notifyDataSetChanged();
            int count = filteredItems.size();
            //..
            for (int i = 0; i < count; i++) {
                originalItems.add(filteredItems.get(i));
                notifyDataSetInvalidated();
            }


        }

    }


    @Override
    public boolean hasStableIds() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public long getItemId(int position) {
        return filteredItems.get(position).hashCode();
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    private static class ViewHolder {
        private TextView cityName;
        private ImageView labelName;
        private CheckBox cityCheck;

    }

    ArrayList<City> getCheckedItems() {
        checkedItems.clear();
        for (City city : originalItems) {
            if (city.isChecked()) {
                checkedItems.add(city);
            }
        }
        return checkedItems;
    }


}
