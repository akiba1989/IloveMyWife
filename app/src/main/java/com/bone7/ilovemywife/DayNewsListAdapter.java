package com.bone7.ilovemywife;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelin.calendarlistview.library.BaseCalendarListAdapter;
import com.kelin.calendarlistview.library.CalendarHelper;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;


public class DayNewsListAdapter extends BaseCalendarListAdapter<NewsService.News.StoriesBean> {
    public Context context;
//    CalendarItemAdapter calendarItemAdapter;

    public DayNewsListAdapter(Context context) {
        super(context);
        this.context = context;
//        this.calendarItemAdapter = calendarItemAdapter;
    }

    @Override
    public View getSectionHeaderView(String date, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        List<NewsService.News.StoriesBean> modelList = dateDataMap.get(date);
        if (convertView != null) {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.listitem_calendar_header, null);
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.dayText = (TextView) convertView.findViewById(R.id.header_day);
            headerViewHolder.yearMonthText = (TextView) convertView.findViewById(R.id.header_year_month);
            headerViewHolder.eventText = (TextView) convertView.findViewById(R.id.header_event_name);
            headerViewHolder.isFavImage = (ImageView) convertView.findViewById(R.id.header_btn_fav);
            convertView.setTag(headerViewHolder);
        }

        Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day < 10) {
            dayStr = "0" + dayStr;
        }
        headerViewHolder.dayText.setText(dayStr);
        headerViewHolder.yearMonthText.setText(CalendarScreenActivity.YEAR_MONTH_FORMAT.format(calendar.getTime()));
        if(MainScreenActivity.appConfig.containDate(CalendarScreenActivity.TREEMAP_FORMAT.format(calendar.getTime()))) {
            headerViewHolder.isFavImage.setImageResource(R.mipmap.ic_btn_calendar_heart_normal);
//            Calendar currentCalendar = Calendar.getInstance();
            int diff;
            MyConfigClass.MyEvent e = MainScreenActivity.appConfig.getEventByDate(CalendarScreenActivity.TREEMAP_FORMAT.format(calendar.getTime()));
//            if(CalendarScreenActivity.TREEMAP_FORMAT.format(currentCalendar.getTime()).compareTo(e.eventDate) > 0)
            if(CalendarScreenActivity.TREEMAP_FORMAT.format(calendar.getTime()).substring(5,10).compareTo(e.eventDate.substring(5,10)) > 0)
                diff = calendar.get(Calendar.YEAR) - Integer.valueOf(e.eventDate.substring(0,4))+1;
            else
                diff = calendar.get(Calendar.YEAR) - Integer.valueOf(e.eventDate.substring(0,4));
            if(diff > 1)
                headerViewHolder.eventText.setText(e.eventName+ " ("+diff+" years)");
            else
                headerViewHolder.eventText.setText(e.eventName+ " ("+diff+" year)");
            //        headerViewHolder.eventText.setText(MainScreenActivity.appConfig.getEventName(CalendarScreenActivity.TREEMAP_FORMAT.format(calendar.getTime())));
        }
        else {
            headerViewHolder.isFavImage.setImageResource(R.mipmap.ic_btn_calendar_heart_down);
            headerViewHolder.eventText.setText("");
        }


        return convertView;
    }

    @Override
    public View getItemView(final NewsService.News.StoriesBean model, String date, int pos, View convertView, ViewGroup parent) {
        ContentViewHolder contentViewHolder;
        if (convertView != null) {
            contentViewHolder = (ContentViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.listitem_calendar_content, null);
            contentViewHolder = new ContentViewHolder();
            contentViewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            contentViewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time);
            contentViewHolder.newsImageView = (ImageView) convertView.findViewById(R.id.image);
            contentViewHolder.isChecked = (CheckBox) convertView.findViewById(R.id.jobcheckbox);
            convertView.setTag(contentViewHolder);
        }

        contentViewHolder.titleTextView.setText(model.getTitle());
        contentViewHolder.timeTextView.setText(date);
//        GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(convertView.getResources())
//                .setRoundingParams(RoundingParams.asCircle())
//                .build();
//        contentViewHolder.newsImageView.setHierarchy(hierarchy);
//        contentViewHolder.newsImageView.setImageURI(Uri.parse(model.getImages().get(0)));

        // This is old part for image
//        Picasso.with(convertView.getContext()).load(Uri.parse(model.getImages().get(0)))
//                .into(contentViewHolder.newsImageView);
        ///
//        int resID = context.getResources().getIdentifier(model.getImages().get(0), "mipmap", context.getPackageName());
        int resID = context.getResources().getIdentifier("action"+ model.getId(), "mipmap", context.getPackageName());
        contentViewHolder.newsImageView.setImageResource(resID);

        contentViewHolder.isChecked.setChecked(model.getChecked());
        contentViewHolder.isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(model.getChecked())
                {
                    cb.setChecked(false);
                    model.setChecked(false);
                    if(model.getImages().contains("action"+model.getId()))
                        model.getImages().remove("action"+model.getId());
                }
                else
                {
                    cb.setChecked(true);
                    model.setChecked(true);
                    if(!model.getImages().contains("action"+model.getId()))
                        model.getImages().add("action"+model.getId());
                }
//                calendarItemAdapter.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private static class HeaderViewHolder {
        TextView dayText;
        TextView yearMonthText;
        TextView eventText;
        ImageView isFavImage;
    }

    private static class ContentViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        ImageView newsImageView;
        CheckBox isChecked;
    }

}
