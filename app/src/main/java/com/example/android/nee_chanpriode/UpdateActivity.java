package com.example.android.nee_chanpriode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateActivity extends AppCompatActivity {
    private EditText editSiklus1,editJmlHaid1;
    //    private CalendarView datePicker;
    private DatePicker datePicker1;
    private Button btnUpdate;


    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    FirebaseAuth mFirebaseAuth;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;

    String mTanggal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Periode");

        editSiklus1 = findViewById(R.id.ev_siklus1);
        editJmlHaid1 = findViewById(R.id.ev_lamahaid1);

        datePicker1 = findViewById(R.id.data_haid_date_picker1);
        btnUpdate = findViewById( R.id.btn_updateData);


    }
}
