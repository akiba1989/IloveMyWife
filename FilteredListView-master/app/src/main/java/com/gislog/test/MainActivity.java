package com.gislog.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<City> cities;
    private CityAdapter cityAdapter;
    private ListView listView;
    private EditText searchEditText;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cityArray = getResources().getStringArray(R.array.cities);
        int[] idArray = getResources().getIntArray(R.array.cityids);
        cities = new ArrayList<>();
        for (int i = 0; i < cityArray.length; i++) {
            cities.add(new City(idArray[i], cityArray[i]));
        }

        searchEditText = (EditText) findViewById(R.id.searchEditText);
        listView = (ListView) findViewById(R.id.listView);
        next = (Button) findViewById(R.id.next);
        cityAdapter = new CityAdapter(this, cities);
        listView.setAdapter(cityAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = String.valueOf(charSequence).toLowerCase();
                cityAdapter.getFilter().filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityAdapter.getCheckedItems();
            }
        });

    }
}
