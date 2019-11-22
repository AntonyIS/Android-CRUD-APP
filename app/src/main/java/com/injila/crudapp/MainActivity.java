package com.injila.crudapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

//    declare objects
    TextView tvWelcome;
    EditText etFirstname,etLastname,etCompany,etEmail;
    Button btnSave,btnView,btnDelete;

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        findViewById()
        etFirstname = findViewById(R.id.etFirstname);
        etLastname = findViewById(R.id.etLastname);
        etCompany = findViewById(R.id.etCompany);
        etEmail = findViewById(R.id.etEmail);

        btnSave = findViewById(R.id.btnSave);
        btnView = findViewById(R.id.btnView);
        btnDelete = findViewById(R.id.btnDelete);

//        START BY CREATING A DATABSE AND THE TABLES
//        Db name = AndroidDB
//        table name = users
//        table column names : firstname, lastname,company, email
        db = openOrCreateDatabase("AndroidDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users (firstname VARCHAR, lastname VARCHAR, company VARCHAR,email VARCHAR)");

//        if user clicks the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                grab data  the user has entered
                String mFirstname = etFirstname.getText().toString().trim();
                String mLastname = etLastname.getText().toString().trim();
                String mCompany = etCompany.getText().toString().trim();
                String mEmail = etEmail.getText().toString().trim();

//                check if the data is empty
                if(mFirstname.isEmpty()){
                    etFirstname.setError("Please enter firstname");
                }
                else if(mLastname.isEmpty()){
                    etLastname.setError("Please enter lastname");
                }
                else if(mCompany.isEmpty()){
                    etCompany.setError("Please enter company");
                }
                else if(mEmail.isEmpty()){
                    etEmail.setError("Please enter email");
                }
                else{
//                    save data into the db
                    db.execSQL("INSERT INTO users  VALUES ('"+mFirstname+"', '"+mLastname+"','"+mCompany+"', '"+mEmail+"')");
                    message("Record added successfully");
                }
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("SELECT * FROM users", null);

//                check if any data/record has been found
                if(cursor.getCount() == 0){
                    message("No data found");
                }

                StringBuffer buffer = new StringBuffer();

                while (cursor.moveToNext()){
                    buffer.append("Your first name is : "+ cursor.getString(0));
                    buffer.append("\n");
                    buffer.append("Your last name is : " + cursor.getString(1));
                    buffer.append("\n");
                    buffer.append("Your company is : " + cursor.getString(2));
                    buffer.append("\n");
                    buffer.append("Your email is : " + cursor.getString(3));
                    buffer.append("\n");
                    Buffer data = buffer;
                }
//                Show the records on a buffer
//                message_two("ALL RECORDS", buffer.toString());
                Intent go = new Intent(MainActivity.this,ViewActivity.class);
                go.putExtra("data", data);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                get a the data you want to delete
                String deleteEmail = etEmail.getText().toString().trim();

//                check if its empty
                if(deleteEmail.isEmpty()){
                    etEmail.setError("Please enter email to delete");
                }
                else{
                    Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email= '"+deleteEmail+"'", null);
                    if(cursor.moveToFirst()){
                        db.execSQL("DELETE FROM users WHERE email='"+deleteEmail+"'");
//                        Toast.makeText(MainActivity.this, " Deleted", Toast.LENGTH_SHORT).show();
                        message_two("Deleted","Data deleted successfuly");
                    }
                }
            }
        });
    }
//    create a message method to display both successful and unsuccessful operations
    public  void message(String title){
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }
    public void message_two(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OKEY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}
