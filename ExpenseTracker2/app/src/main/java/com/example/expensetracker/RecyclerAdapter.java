package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<ExpenseInfo> expenseList;

    public RecyclerAdapter(ArrayList<ExpenseInfo> gradeInfo){
        this.expenseList = gradeInfo;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nametxt, price, date, category;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nametxt = itemView.findViewById(R.id.nameTv);
            price = itemView.findViewById(R.id.priceTv);
            date = itemView.findViewById(R.id.dateTv);
            category = itemView.findViewById(R.id.categoryTv);
        }
    }


    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        String name = expenseList.get(position).getName();
        String price = expenseList.get(position).getPrice();
        String date = expenseList.get(position).getDate();
        String category = expenseList.get(position).getCategory();

        holder.nametxt.setText(name);
        holder.price.setText(price+"$");
        holder.date.setText(date);
        holder.category.setText(category);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}

