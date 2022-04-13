package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class BottomSheet extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener{

    TextInputLayout name;
    TextInputLayout price;
    Spinner spinner;
    String category="";
    String date="";
    Boolean insertStat;
    DataBaseHelper dbh;
    String[] categories = {"HouseHold","Fun","Travelling","Work","Other"};
    private BottomSheetListener bottomSheetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // To bind this java file with bottom navigation drawer
        View v = inflater.inflate(R.layout.bottom_sheet,container,false);

        //Attaching widgets with java variables
        name = v.findViewById(R.id.expenseName);
        price = v.findViewById(R.id.cost);
        Button dateButton = v.findViewById(R.id.dateBtn);

        spinner = v.findViewById(R.id.spinner);
        //Populating Spinner
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        // Instantiating Database Helper
        dbh = new DataBaseHelper(getActivity());

        //Date Selection
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // date picker dialog
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                date = dateButton.getText().toString();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        // When submit button is clicked on BottomSheet
        Button btn = v.findViewById(R.id.submitData);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                ExpenseInfo info = new ExpenseInfo(id, name.getEditText().getText().toString(), price.getEditText().getText().toString(),date,category);
                insertStat = dbh.InsertProgram(info);

                // Check to see if records are inserted or not
                if(insertStat)
                    Toast.makeText(getContext(), "Record Inserted Successfully !", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Record Not Inserted !", Toast.LENGTH_SHORT).show();
                name.getEditText().setText("");
                price.getEditText().setText("");
                dateButton.setText("Select Date");
                bottomSheetListener.onButtonClicked();

            }
        });
        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bottomSheetListener = (BottomSheetListener) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = categories[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
