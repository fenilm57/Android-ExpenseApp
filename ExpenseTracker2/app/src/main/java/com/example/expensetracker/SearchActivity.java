package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    String category="";
    Boolean insertStat;
    DataBaseHelper dbh;
    Button btn;
    private ArrayList<ExpenseInfo> expenseInfo;
    RecyclerView recyclerView;
    String[] categories = {"HouseHold","Fun","Travelling","Work","Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        spinner = findViewById(R.id.spinner);
        //Populating Spinner
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        expenseInfo = new ArrayList<>();
        btn = findViewById(R.id.searchBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
                setAdapter();
            }
        });
        // Instantiating Database Helper
        dbh = new DataBaseHelper(getApplicationContext());
    }

    public void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(expenseInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
    public void getData() {
        Cursor cursor = dbh.ViewSearch(category);
        ExpenseInfo info = new ExpenseInfo();
        expenseInfo = new ArrayList<>();
        // Taking value from cursor to Program variable
        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                do {
                    info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    info.setName(cursor.getString(cursor.getColumnIndex("name")));
                    info.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    info.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    info.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                    expenseInfo.add(new ExpenseInfo(info.getId(), info.getName(), info.getPrice(), info.getDate(), info.getCategory()));
                } while (cursor.moveToNext());
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = categories[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}