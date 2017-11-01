package com.bone7.ilovemywife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelin.calendarlistview.library.BaseCalendarItemAdapter;
import com.kelin.calendarlistview.library.BaseCalendarItemModel;

/**
 * Created by kelin on 16-7-19.
 */
public class CalendarItemAdapter extends BaseCalendarItemAdapter<CustomCalendarItemModel> {
    public Context context;
    public CalendarItemAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public View getView(String date, CustomCalendarItemModel model,View convertView, ViewGroup parent) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.griditem_custom_calendar_item, null);

        TextView dayNum = (TextView) view.findViewById(R.id.tv_day_num);
        dayNum.setText(model.getDayNumber());

        view.setBackgroundResource(com.kelin.calendarlistview.library.R.drawable.bg_shape_calendar_item_normal);

        if (model.isToday()) {
            dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.red_ff725f));
            dayNum.setText(mContext.getResources().getString(com.kelin.calendarlistview.library.R.string.today));
        }

        if (model.isHoliday()) {
            dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.red_ff725f));
        }


        if (model.getStatus() == BaseCalendarItemModel.Status.DISABLE) {
            dayNum.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }

        if (!model.isCurrentMonth()) {
            dayNum.setTextColor(mContext.getResources().getColor(com.kelin.calendarlistview.library.R.color.gray_bbbbbb));
            view.setClickable(true);
        }

        TextView dayNewsCount = (TextView) view.findViewById(R.id.tv_day_new_count);
        if (model.getNewsCount() > 0) {
            dayNewsCount.setText(String.format(mContext.getResources().getString(R.string.calendar_item_new_count), model.getNewsCount()));
            dayNewsCount.setVisibility(View.VISIBLE);
        } else {
            dayNewsCount.setVisibility(View.GONE);
        }

        ImageView isFavImageView = (ImageView) view.findViewById(R.id.image_is_fav);
        if (model.isFav()) {
            isFavImageView.setVisibility(View.VISIBLE);
        } else {
            isFavImageView.setVisibility(View.GONE);
        }
        // Images
        org.apmem.tools.layouts.FlowLayout imageContainer = (org.apmem.tools.layouts.FlowLayout) view.findViewById(R.id.list_image_action);
        imageContainer.removeAllViewsInLayout();
        if(model.getListImages()!= null)
            for (String image:model.getListImages())
            {
                ImageView imgv = new ImageView(context);
//                ImageView imgv = (ImageView)LayoutInflater.from(context).inflate(R.layout.flowlayout_item,imageContainer,false);
                org.apmem.tools.layouts.FlowLayout.LayoutParams lp = new org.apmem.tools.layouts.FlowLayout.LayoutParams(dpToPx(10), dpToPx(10));
////                lp.setMargins(0,0,dpToPx(1),dpToPx(1));
                imgv.setLayoutParams(lp);
                int resID = context.getResources().getIdentifier(image, "mipmap", context.getPackageName());
                imgv.setImageResource(resID);
                imageContainer.addView(imgv);
            }

        return view;
    }
}
