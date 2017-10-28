package com.bone7.ilovemywife;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SettingScreenActivity extends AppCompatActivity {

    MyConfigClass appConfig;
    ListView listView;
    Button btnAddEvent, btnDonate;
    ToggleButton toggleNotification;
    ArrayList<MyConfigClass.MyEvent> dataModels;
    EventAdapter eventAdapter;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);
        Intent intent = getIntent();
        String myConfig = intent.getStringExtra("config");

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
        eventAdapter = new EventAdapter(appConfig.eventList, getApplicationContext());
        listView.setAdapter(eventAdapter);

        toggleNotification = (ToggleButton) findViewById(R.id.toggleButton);
        toggleNotification.setChecked(appConfig.notification);

        toggleNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                appConfig.notification = b;
            }
        });

        btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.Builder builder = new MaterialDialog.Builder(getApplicationContext())
                        .title("Add your event")
                        .customView(R.layout.dialog_add_new_event,true)
                        .positiveText("Add")
                        .negativeText("Cancel");

                final MaterialDialog dialog = builder.build();


                final View actionCustomview = dialog.getCustomView();
                final Spinner spinner = (Spinner)actionCustomview.findViewById(R.id.spinner);
                final EditText editText = (EditText)actionCustomview.findViewById(R.id.editText);
                final EditText editTextDate = (EditText)actionCustomview.findViewById(R.id.editTextDate);
                final Button btnSet = (Button)  actionCustomview.findViewById(R.id.btnSet);
                String[] eventString = getResources().getStringArray(R.array.event_list);
                List<String> eventStringList = Arrays.asList(eventString);
                ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, eventStringList);
                spinner.setAdapter(spinnerAdapter);
                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!spinner.getSelectedItem().toString().equals("Other"))
                            editText.setText(spinner.getSelectedItem().toString());
                    }
                });
                btnSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int mYear, mMonth, mDay;
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(),
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
                        eventAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    protected void onPause() {

        super.onPause();
        MyAndroidHelper.writeToFile("config", gson.toJson(appConfig),getApplicationContext());

    }

}
