package com.example.mysqlitedatabaseapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
   private  EditText email,number,name;
    private Button save,delete,view;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.edtemail);
        number = findViewById(R.id.edtidnumber);
        name = findViewById(R.id.edtname);
        save = findViewById(R.id.btnsave);
        delete = findViewById(R.id.btndelete);
        view = findViewById(R.id.btnview);

        //create the db
        db = openOrCreateDatabase("voters_db",MODE_PRIVATE,null);
        //create table in your db
        db.execSQL("CREATE TABLE IF NOT EXISTS voterreg(name VARCHAR,email VARCHAR,idNo VARCHAR)");

        //set the onclicklistener to the save button to implement the saving process
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start gettinhg data from the user
                String nam = name.getText().toString().trim();
                String emai = email.getText().toString().trim();
                String idnumb = number.getText().toString().trim();

                //check if the user is submitting empty fields
                if (nam.isEmpty()){
                    //display the message telling the user to fill the name input fields
                    messages("Name Error","Please fill the input field");
                }else if (emai.isEmpty()){
                    //display the message telling the user to fill the name input fields
                                messages("Email Error","Please fill the input field");
                }else if (idnumb.isEmpty()){
                    messages("ID Number Error","Please fill the input field");
                }else {
                    //proceed to save your data
                    db.execSQL("INSERT INTO voterreg VALUES('"+nam+"','"+emai+"','"+idnumb+"')");
                    messages("QUERY SUCCESS","Data was saved succesfully saved");
                    clear();
                }

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use the cursor to select the data from the db
                Cursor cursor = db.rawQuery("SELECT * FROM voterreg",null);

                //check if there are any records in the db
                if (cursor.getCount()==0){
                    messages("No Records","Sorry,No Records found");
                }else {
                    //use the StringBuffer to append the records
                    StringBuffer buffer = new StringBuffer();
                    //loop through the table rows to get the data one by one
                    while (cursor.moveToNext()){
                        buffer.append("Name is:"+cursor.getString(0));
                        buffer.append("\n");
                        buffer.append("Email is:"+cursor.getString(1));
                        buffer.append("\n");
                        buffer.append("ID is:"+cursor.getString(2) );
                        buffer.append("\n");
                    }

                    //we can now display our records using the messages function
                    messages("DATABASE RECORDS",buffer.toString());
                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start by getting the id from the user
                String id_number = number.getText().toString().trim();
                //check if the user is hitting delete without filling the id input field
                if (id_number.isEmpty()){
                    number.setError("Enter the ID NO.");
                }else {
                    //proceed to delete using the received id
                    Cursor cursor = db.rawQuery("SELECT * FROM voterreg WHERE idNo = '"+id_number+"'", null);
                    if (cursor.moveToFirst()){
                        db.execSQL("DELETE FROM voterreg WHERE idNo = '"+id_number+"'");

                        messages("DELETED","Record deleted successfully");
                        clear();
                    }
                }
            }
        });
    }

    public  void  messages(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    public  void clear(){
        name.setText("");
        email.setText("");
        number.setText("");
    }
}
