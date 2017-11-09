package com.bone7.ilovemywife;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class SettingScreenActivity extends AppCompatActivity {

    MyConfigClass appConfig;
    ListView listView;
    FancyButton btnAddEvent, btnDonate;
    ToggleButton toggleNotification;
    ArrayList<MyConfigClass.MyEvent> dataModels;
    EventAdapter eventAdapter;
    Gson gson = new Gson();
    MaterialDialog.Builder builder;
    MaterialDialog dialog;
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        Intent intent = getIntent();
        String myConfig = intent.getStringExtra("config");
        Log.i("myconfig",myConfig);
        if(myConfig.length() < 2)
        {
            appConfig = new MyConfigClass();
            appConfig.notification = true;
            appConfig.eventList = new ArrayList<>();
        }else
        {
            Type listType = new TypeToken<MyConfigClass>(){}.getType();
            appConfig = (MyConfigClass) gson.fromJson(myConfig, listType);
        }

        listView = (ListView) findViewById(R.id.listViewEvent);
        eventAdapter = new EventAdapter(appConfig.eventList, SettingScreenActivity.this);
        listView.setAdapter(eventAdapter);

        toggleNotification = (ToggleButton) findViewById(R.id.toggleButton);
        toggleNotification.setChecked(appConfig.notification);

        toggleNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appConfig.notification = b;
            }
        });


        btnAddEvent = (FancyButton) findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new MaterialDialog.Builder(SettingScreenActivity.this)
                        .title("Add your event")
                        .customView(R.layout.dialog_add_new_event,true)
                        .positiveText("Add")
                        .negativeText("Cancel");
                dialog = builder.build();

                final View actionCustomview = dialog.getCustomView();
                final Spinner spinner = (Spinner)actionCustomview.findViewById(R.id.spinner);
                final EditText editText = (EditText)actionCustomview.findViewById(R.id.editText);
                final TextView editTextDate = (TextView)actionCustomview.findViewById(R.id.editTextDate);
                final Button btnSet = (Button)  actionCustomview.findViewById(R.id.btnSet);
                editTextDate.setText(CalendarScreenActivity.TREEMAP_FORMAT.format(c.getTime()));
                String[] eventString = getResources().getStringArray(R.array.event_list);
                List<String> eventStringList = Arrays.asList(eventString);
                ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, eventStringList);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!spinner.getSelectedItem().toString().equals("Other"))
                            editText.setText(spinner.getSelectedItem().toString());
                        else
                            editText.setText("");
                        editText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                        editText.setSelection(editText.getText().length());


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mYear, mMonth, mDay;
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SettingScreenActivity.this,
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
                                        editTextDate.setText(year + "-" + month + "-" + day);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                View positive = dialog.getActionButton(DialogAction.POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyConfigClass.MyEvent event = new MyConfigClass.MyEvent(editText.getText().toString(), editTextDate.getText().toString());
                        appConfig.eventList.add(event);
                        Collections.sort(appConfig.eventList, new MyAndroidHelper.EventComparator());
                        eventAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btnDonate = (FancyButton) findViewById(R.id.btnDonate);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DonationActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onPause() {

        super.onPause();
        MyAndroidHelper.writeToFile("config", gson.toJson(appConfig),getApplicationContext());
        Log.i("myconfig",gson.toJson(appConfig));
        MainScreenActivity.appConfig = appConfig;
    }

}
