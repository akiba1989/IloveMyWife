package com.bone7.ilovemywife;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
//import com.bone7.ilovemywife.retrofit.RetrofitProvider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.calendarlistview.library.CalendarHelper;
import com.kelin.calendarlistview.library.CalendarListView;
import com.kelin.calendarlistview.library.PinnedHeaderListView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;




public class CalendarScreenActivity extends RxAppCompatActivity {
    public static final SimpleDateFormat TREEMAP_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("MMM-yyyy");
    public ActionBar actionBar;

    private CalendarListView calendarListView;
    private DayNewsListAdapter dayNewsListAdapter;
    private CalendarItemAdapter calendarItemAdapter;
    //key:date "yyyy-mm-dd" format.
    private TreeMap<String, List<NewsService.News.StoriesBean>> listTreeMap = new TreeMap<>();
    private TreeMap<String, List<NewsService.News.StoriesBean>> currentTreeMap = new TreeMap<>();
    private TreeMap<String, List<NewsService.News.StoriesBean>> tempTreeMap = new TreeMap<>();

    private Handler handler = new Handler();

    public List<NewsService.News.StoriesBean> tempActions;
    Gson gson = new Gson();

//    MenuItem btnPre, btnNext, txtMonth;
    ImageButton btnPre, btnNext; TextView txtMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar_screen);


        final PinnedHeaderListView lv  = (PinnedHeaderListView) findViewById(R.id.listview);
        boolean wrapInScrollView = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Add your action")
                .customView(R.layout.add_new_action,wrapInScrollView)
                .positiveText("Agree")
                .negativeText("Cancel");

        final MaterialDialog dialog = builder.build();


        final View actionCustomview = dialog.getCustomView();
        final Spinner spinner = (Spinner)actionCustomview.findViewById(R.id.spinner);
        final EditText editText = (EditText)actionCustomview.findViewById(R.id.editText);
        int flags[] = {R.mipmap.action8,R.mipmap.action1, R.mipmap.action2,R.mipmap.action3,R.mipmap.action4,R.mipmap.action5,R.mipmap.action6,R.mipmap.action7};
        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(getApplicationContext(),flags);
        spinner.setAdapter(mySpinnerAdapter);

        // for old action bar
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);


        calendarListView = (CalendarListView) findViewById(R.id.calendar_listview);
        calendarListView.SetAddNewJobBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Add new job btn clicked!",Toast.LENGTH_LONG).show();
                dialog.show();

            }
        });

        View positive = dialog.getActionButton(DialogAction.POSITIVE);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String action="";
                NewsService.News.StoriesBean story;
                if(spinner.getSelectedItemPosition() > 0) {
                    action = "action" + spinner.getSelectedItemPosition();
                    story = new NewsService.News.StoriesBean(editText.getText().toString(),action,spinner.getSelectedItemPosition(),true);
                }
                else
                    story = new NewsService.News.StoriesBean(editText.getText().toString(),"action8",8,true);
                listTreeMap.get(calendarListView.getCurrentSelectedDate()).add(story);
                Collections.sort(listTreeMap.get(calendarListView.getCurrentSelectedDate()), new MyAndroidHelper.ActionComparator());
                dayNewsListAdapter.notifyDataSetChanged();
                dialog.dismiss();
//                Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG);
            }
        });

        calendarItemAdapter = new CalendarItemAdapter(this);
        dayNewsListAdapter = new DayNewsListAdapter(this);

        calendarListView.setCalendarListViewAdapter(calendarItemAdapter, dayNewsListAdapter);
        lv.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
//                dayNewsListAdapter.notifyDataSetChanged();
//                calendarItemAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"ListItem clicked",Toast.LENGTH_SHORT);
                Log.i("test1", "Itemclicked!");
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {
                Log.i("test1", "ISectrionlicked!");
            }
        });

        // set start time,just for test.
        Calendar calendar = Calendar.getInstance();

//        calendar.add(Calendar.MONTH, -7);
        loadNewsList(TREEMAP_FORMAT.format(calendar.getTime()).substring(0,7));
        loadDayList(TREEMAP_FORMAT.format(calendar.getTime()));

        showActionBar(YEAR_MONTH_FORMAT.format(calendar.getTime()));

//        actionBar.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
//        txtMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));


        // deal with refresh and load more event.
        calendarListView.setOnListPullListener(new CalendarListView.onListPullListener() {
            @Override
            public void onRefresh() {
                String date = listTreeMap.firstKey();
                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(2017,2,1);
//                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
            }

            @Override
            public void onLoadMore() {
                String date = listTreeMap.lastKey();
                Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(2017,2,1);
//                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
            }
        });

        //
        calendarListView.setOnMonthChangedListener(new CalendarListView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(String yearMonth) {
                Calendar calendar = CalendarHelper.getCalendarByYearMonth(yearMonth);
//                calendar.set(Calendar.DAY_OF_MONTH, 1);
//                actionBar.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                loadNewsList(yearMonth.substring(0,7));
                loadCalendarData(yearMonth);
                Toast.makeText(CalendarScreenActivity.this, YEAR_MONTH_FORMAT.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                loadDayList(yearMonth+"-"+calendarListView.getCurrentSelectedDate().substring(8,10));
            }
        });

        calendarListView.setOnCalendarViewItemClickListener(new CalendarListView.OnCalendarViewItemClickListener() {
            @Override
            public void onDateSelected(View View, String selectedDate, int listSection, SelectedDateRegion selectedDateRegion) {
                Log.i("test1",selectedDate);

                MyAndroidHelper.writeToFile(selectedDate.substring(0,7), gson.toJson(listTreeMap),getApplicationContext());
//                Calendar calendar = getCalendarByYearMonthDay(selectedDate);
                loadDayList(selectedDate);
//                String tremp = MyAndroidHelper.readFromFile(selectedDate.substring(0,7),getApplicationContext());
                Log.i("test3", selectedDate.substring(0,7));
            }
        });
    }
    @Override
    protected void onPause() {

        super.onPause();
        MyAndroidHelper.writeToFile(calendarListView.getCurrentSelectedDate().substring(0,7), gson.toJson(listTreeMap),getApplicationContext());

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_calendar_screen, menu);
//        btnPre = menu.findItem(R.id.action_pre);
//        btnNext = menu.findItem(R.id.action_next);
//        txtMonth= menu.findItem(R.id.action_month);
//        Calendar calendar = getCalendarByTREEMAP(calendarListView.getCurrentSelectedDate());
//        txtMonth.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
//        return true;
//    }
    private void showActionBar(String txtM) {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_calendar_screen_layout, null);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        btnPre = v.findViewById(R.id.btnPre);
        btnNext = v.findViewById(R.id.btnNext);
        txtMonth= (TextView) v.findViewById(R.id.txtMonth);
        txtMonth.setText(txtM);
        final Calendar calendar = getCalendarByTREEMAP(calendarListView.getCurrentSelectedDate());
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar.add(Calendar.MONTH, -1);
                calendarListView.setCurrentSelectedDate(TREEMAP_FORMAT.format(calendar.getTime()));
//                txtMonth.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                txtMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                calendarListView.changeMonth(-1);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar.add(Calendar.MONTH, 1);
                calendarListView.setCurrentSelectedDate(TREEMAP_FORMAT.format(calendar.getTime()));
//                txtMonth.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                txtMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                calendarListView.changeMonth(1);
            }
        });
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setCustomView(v, params);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Calendar calendar = getCalendarByTREEMAP(calendarListView.getCurrentSelectedDate());
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_pre:
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                calendar.add(Calendar.MONTH, -1);
                calendarListView.setCurrentSelectedDate(TREEMAP_FORMAT.format(calendar.getTime()));
//                txtMonth.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                txtMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                calendarListView.changeMonth(-1);

                break;
            // action with ID action_settings was selected
            case R.id.action_next:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                calendar.add(Calendar.MONTH, 1);
                calendarListView.setCurrentSelectedDate(TREEMAP_FORMAT.format(calendar.getTime()));
//                txtMonth.setTitle(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                txtMonth.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
                calendarListView.changeMonth(1);
                break;
            default:
                break;
        }

        return true;
    }

    // load list by month
    private void loadNewsList(String date) {

        Gson gson = new Gson();
        Type listType = new TypeToken<TreeMap<String, List<NewsService.News.StoriesBean>>>(){}.getType();
//        listTreeMap = (TreeMap<String, List<NewsService.News.StoriesBean>>) gson.fromJson(MyAndroidHelper.readFromAssetsFile("treemap.json",getApplicationContext()), listType);
        //date: 2017-03
        String json =  MyAndroidHelper.readFromFile(date,getApplicationContext());
        if(json.length() > 0)
            listTreeMap = (TreeMap<String, List<NewsService.News.StoriesBean>>) gson.fromJson(json, listType);
        Log.i("test5",date);

    }
    private void loadDayList(String date) {
        currentTreeMap.clear();
        if(listTreeMap.containsKey(date))
            currentTreeMap.put(date,listTreeMap.get(date));
        else
        {
            //set temple actions
            tempActions = new ArrayList<>();
            tempActions.add(new NewsService.News.StoriesBean("Send her messages","action1",1,false));
            tempActions.add(new NewsService.News.StoriesBean("Buy her a gift","action2",2,false));
            tempActions.add(new NewsService.News.StoriesBean("Tell her stories","action3",3,false));
            tempActions.add(new NewsService.News.StoriesBean("Help her to do house works","action4",4,false));
            tempActions.add(new NewsService.News.StoriesBean("Cook a meal for her","action5",5,false));
            tempActions.add(new NewsService.News.StoriesBean("Take a picture with her","action6",6,false));
            tempActions.add(new NewsService.News.StoriesBean("Go out with her","action7",7,false));
//            tempActions.add(new NewsService.News.StoriesBean("This is custom action","action8",8,false));
            listTreeMap.put(date,tempActions);
//            currentTreeMap.put(date,listTreeMap.firstEntry().getValue());
            currentTreeMap.put(date,listTreeMap.get(date));
        }
//        Collections.sort(listTreeMap.get(date), new Comparator<NewsService.News.StoriesBean>() {
//            @Override
//            public int compare(NewsService.News.StoriesBean lhs, NewsService.News.StoriesBean rhs) {
//                return String.valueOf(rhs.getChecked()).compareTo(String.valueOf(lhs.getChecked()));
//            }
//        });
        Collections.sort(listTreeMap.get(date), new MyAndroidHelper.ActionComparator());

//        dayNewsListAdapter = new DayNewsListAdapter(this);
        dayNewsListAdapter.setDateDataMap(currentTreeMap);
        dayNewsListAdapter.notifyDataSetChanged();
//        calendarItemAdapter.notifyDataSetChanged();
        loadCalendarData(date.substring(0, 7));
    }


    // date (yyyy-MM),load data for Calendar View by date,load one month data one times.
    // generate test data for CalendarView,imitate to be a Network Requests. update "calendarItemAdapter.getDayModelList()"
    //and notifyDataSetChanged will update CalendarView.
    private void loadCalendarData(final String date) {

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(500);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(CalendarScreenActivity.this, date+", "+calendarListView.getCurrentSelectedDate().substring(0, 7), Toast.LENGTH_SHORT).show();

//                            if (date.equals(calendarListView.getCurrentSelectedDate().substring(0, 7)))
                            {
                                for (String d : listTreeMap.keySet()) {
                                    if (date.equals(d.substring(0, 7)))
                                    {
                                        CustomCalendarItemModel customCalendarItemModel = calendarItemAdapter.getDayModelList().get(d);
                                        if (customCalendarItemModel != null) {
                                            int newCount = 0;
                                            //set newscount cho nay
                                            for(int i=0;i<listTreeMap.get(d).size();i++)
                                            {
                                                if(listTreeMap.get(d).get(i).getChecked())
                                                    newCount++;
                                            }
                                            customCalendarItemModel.setNewsCount(newCount);
                                            Log.i("test3",d.substring(5,10));
                                            if(MainScreenActivity.appConfig.containDate(d)) {
                                                customCalendarItemModel.setHoliday(true);
                                                customCalendarItemModel.setFav(true);
                                            }
                                            //Get Image list
//                                            customCalendarItemModel.setListImages(listTreeMap.get(d).getImages());
                                            List<String> imageActions = new ArrayList<>();
                                            for(int i=0;i<listTreeMap.get(d).size();i++)
                                            {
                                                if(listTreeMap.get(d).get(i).getChecked())
                                                    imageActions.addAll(listTreeMap.get(d).get(i).getImages());
                                            }
                                            customCalendarItemModel.setListImages(imageActions);
                                        }

                                    }
                                }

                                if(MainScreenActivity.appConfig.containMonth(date.substring(5,7)))
                                {

                                    for(MyConfigClass.MyEvent event : MainScreenActivity.appConfig.eventList)
                                    {
                                        if(event.eventDate.substring(5,7).equals(date.substring(5,7)))
                                        {
                                            Log.i("date1", date+event.eventDate.substring(7,10));
                                            CustomCalendarItemModel itemModel = calendarItemAdapter.getDayModelList().get(date+event.eventDate.substring(7,10));
                                            if(itemModel != null)
                                            {
                                                itemModel.setHoliday(true);
                                                itemModel.setFav(true);
                                            }
                                            else
                                            {
                                                itemModel = new CustomCalendarItemModel();
                                                itemModel.setHoliday(true);
                                                itemModel.setFav(true);
                                                calendarItemAdapter.getDayModelList().put(date+event.eventDate.substring(7,10), itemModel);

                                            }
                                        }

                                    }
                                }
                                calendarItemAdapter.notifyDataSetChanged();
//                                Toast.makeText(CalendarScreenActivity.this, date, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();
//        calendarItemAdapter.notifyDataSetChanged();

    }


    public static Calendar getCalendarByYearMonthDay(String yearMonthDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(DAY_FORMAT.parse(yearMonthDay).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
    public static Calendar getCalendarByTREEMAP(String yearMonthDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(TREEMAP_FORMAT.parse(yearMonthDay).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }



}
