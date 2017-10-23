package com.bone7.ilovemywife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by jamijn on 10/23/17.
 */

public class MySpinnerAdapter extends BaseAdapter {
    Context context;
    int flags[];

    LayoutInflater inflter;

    public MySpinnerAdapter(Context applicationContext, int[] flags) {
        this.context = applicationContext;
        this.flags = flags;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_row, null);
        ImageView icon = (ImageView) view.findViewById(R.id.spinnerImage);
        icon.setImageResource(flags[i]);
        return view;
    }


}
