package com.usman.todoapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usman.todoapp.Adapter.ItemClickListener;
import com.usman.todoapp.Adapter.ToDoTaskAdapter;
import com.usman.todoapp.Model.ToDoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG ="MainLog" ;
    private RecyclerView mRecyclerView;
    private ToDoTaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<ToDoModel> toDoModelList = new ArrayList<>();

    DatePickerDialog.OnDateSetListener date;
    Button btnSelectDate;
    final Calendar myCalendar = Calendar.getInstance();
    String cashDate="";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("ToDoApp");

    DatabaseReference myRefWithUid;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        myRefWithUid = myRef.child(currentUser.getUid());
        getAllToDoTask();
//        dateSelector();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   public void getAllToDoTask(){
       // Read from the database

       myRefWithUid.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               toDoModelList.clear();
               for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren() ){

                   String key = dataSnapshot1.getKey();
                   String name = dataSnapshot1.child("TaskName").getValue(String.class);
                   String date = dataSnapshot1.child("Date").getValue(String.class);
                   ToDoModel toDoModel = new ToDoModel(name,date,key);
                   toDoModelList.add(toDoModel);
               }
               mAdapter = new ToDoTaskAdapter(toDoModelList);
               mRecyclerView.setAdapter(mAdapter);
               mAdapter.setClickListener(MainActivity.this);
               mAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(DatabaseError error) {
               // Failed to read value
               Log.w(TAG, "Failed to read value.", error.toException());
           }
       });
   }

    public void dateSelector(){


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };



        btnSelectDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale.US);

        cashDate = sdf.format(myCalendar.getTime());
        btnSelectDate.setText(sdf.format(myCalendar.getTime()));
    }

    Dialog dialog;
    public void showDialog()
    {

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialouge);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText EdtTaskName = dialog.findViewById(R.id.edt_task_name);
        btnSelectDate =  dialog.findViewById(R.id.btn_select_date);
        Button btnSubmit =  dialog.findViewById(R.id.btn_submit);

        dateSelector();
        dialog.show();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String date =  btnSelectDate.getText().toString();
                 String taskName = EdtTaskName.getText().toString();

                 if(!date.equals("Select Date")){

                     if(!taskName.equals("")){

                         DatabaseReference pushRef = myRefWithUid.push();
                         pushRef.child("TaskName").setValue(taskName);
                         pushRef.child("Date").setValue(date);
                       dialog.cancel();
                     }
                     else{
                         Toast.makeText(MainActivity.this,"Please Enter Task Name",Toast.LENGTH_LONG).show();
                     }
                  }
                 else{
                     Toast.makeText(MainActivity.this,"Please Select Date",Toast.LENGTH_LONG).show();
                 }

            }
        });


    }

    @Override
    public void onClick(View view, int position) {
        final ToDoModel toDoModel = toDoModelList.get(position);

        showRDialog(toDoModel);

    }

    Dialog dialog1;
    public void showRDialog(final ToDoModel toDoModel)
    {
        dialog1 = new Dialog(MainActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.update_delete_dialouge);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        final EditText EdtTaskName = dialog1.findViewById(R.id.edt_task_name);
        btnSelectDate =  dialog1.findViewById(R.id.btn_select_date);
        Button btnUpdate =  dialog1.findViewById(R.id.btn_update);
        Button btnDelete =  dialog1.findViewById(R.id.btn_delete);

        EdtTaskName.setText(toDoModel.getTaskName());
        btnSelectDate.setText(toDoModel.getDate());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRefWithUid.child(toDoModel.getKey()).child("TaskName").setValue(EdtTaskName.getText().toString());
                myRefWithUid.child(toDoModel.getKey()).child("Date").setValue(btnSelectDate.getText().toString());
                dialog1.cancel();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRefWithUid.child(toDoModel.getKey()).removeValue();
                getAllToDoTask();
                dialog1.cancel();
            }
        });

        dateSelector();
        dialog1.show();



    }

}
