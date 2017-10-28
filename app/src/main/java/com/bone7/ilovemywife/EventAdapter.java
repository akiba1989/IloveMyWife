package com.bone7.ilovemywife;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ducnu on 10/28/2017.
 */

public class EventAdapter extends ArrayAdapter<MyConfigClass.MyEvent> {
    private ArrayList<MyConfigClass.MyEvent> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtDate;
        Button btnSet;
        Button btnRemove;
    }

    public EventAdapter(ArrayList<MyConfigClass.MyEvent> data, Context context) {
        super(context, R.layout.event_row, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MyConfigClass.MyEvent dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtEventName);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtEventDate);
            viewHolder.btnSet = (Button) convertView.findViewById(R.id.btnSet);
            viewHolder.btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.eventName);
        viewHolder.txtDate.setText(dataModel.eventDate);
        viewHolder.btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String day, month;
                                if(dayOfMonth< 10)
                                    day = "0"+ dayOfMonth;
                                else
                                    day = ""+dayOfMonth;
                                if((monthOfYear+1) < 10)
                                    month = "0"+(monthOfYear+1);
                                else
                                    month = ""+(monthOfYear+1);
                                viewHolder.txtDate.setText(year + "-" + month + "-" + day);
                                dataModel.eventDate = year + "-" + month + "-" + day;
                                notifyDataSetChanged();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(dataModel);
                notifyDataSetChanged();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
