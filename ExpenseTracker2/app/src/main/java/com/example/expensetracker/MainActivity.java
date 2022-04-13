package com.example.expensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements BottomSheet.BottomSheetListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ArrayList<ExpenseInfo> expenseInfo;
    private ArrayList<Double> totalSpent;
    RecyclerView recyclerView;
    DataBaseHelper dbh;
    TextView tv;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        // Database Instantiation
        dbh = new DataBaseHelper(getApplicationContext());
        builder = new AlertDialog.Builder(this);
        tv = findViewById(R.id.tv);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        expenseInfo = new ArrayList<>();
        getData();
        setAdapter();

        // Initialize Item Touch Helper
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                Log.i("ID:", String.valueOf(expenseInfo.get(viewHolder.getAdapterPosition()).getId()));

                //Dialog box
                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Delete this ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbh.delete(expenseInfo.get(viewHolder.getAdapterPosition()).getId());
                                // Remove when swiped
                                expenseInfo.remove(viewHolder.getAdapterPosition());
                                // Notify Adapter
                                setAdapter();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                expenseInfo = new ArrayList<>();
                                getData();
                                setAdapter();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Expense");
                alert.show();
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        }).attachToRecyclerView(recyclerView);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "BottomSheet");
            }
        });
    }

    public void setAdapter() {
        RecyclerAdapter adapter = new RecyclerAdapter(expenseInfo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getData() {
        Cursor cursor = dbh.ViewData();
        ExpenseInfo info = new ExpenseInfo();
        totalSpent = new ArrayList<>();

        // Taking value from cursor to Program variable
        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tv.setVisibility(View.INVISIBLE);
            if (cursor.moveToFirst()) {
                do {
                    info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    info.setName(cursor.getString(cursor.getColumnIndex("name")));
                    info.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    info.setDate(cursor.getString(cursor.getColumnIndex("date")));
                    info.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                    expenseInfo.add(new ExpenseInfo(info.getId(), info.getName(), info.getPrice(), info.getDate(), info.getCategory()));
                    totalSpent.add(Double.parseDouble(info.getPrice()));
                } while (cursor.moveToNext());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.totalSpent) {
            double sum=0.0;

            for(int i=0;i<totalSpent.size();i++) {
                sum = sum + totalSpent.get(i);
            }
            Toast.makeText(getApplicationContext(), "Total Money Spent is: " + sum, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.searchMenu) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onButtonClicked() {
        expenseInfo = new ArrayList<>();
        getData();
        setAdapter();
    }
}