package com.usman.todoapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.usman.todoapp.Model.ToDoModel;
import com.usman.todoapp.R;

import java.util.List;

public class ToDoTaskAdapter extends RecyclerView.Adapter<ToDoTaskAdapter.MyViewHolder> {
    private List<ToDoModel> jobModelList;
    public ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_sr , txt_date , txt_name ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_sr = itemView.findViewById(R.id.txt_sr);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_name = itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ToDoTaskAdapter(List<ToDoModel> jobModelList) {
        this.jobModelList = jobModelList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ToDoTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.todo_list_row, parent, false);


        // Return a new holder instance
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ToDoTaskAdapter.MyViewHolder viewHolder, int position) {
        // Get the data model based on position
        ToDoModel m = jobModelList.get(position);

        TextView name = viewHolder.txt_name;
        TextView date = viewHolder.txt_date;
        TextView sr = viewHolder.txt_sr;

        sr.setText(String.valueOf(position));
        name.setText(m.getTaskName());
        date.setText(m.getDate());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jobModelList.size();
    }


}

