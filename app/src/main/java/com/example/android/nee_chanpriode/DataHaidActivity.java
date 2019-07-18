 package com.example.android.nee_chanpriode;

 import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.nee_chanpriode.Model.PeriodeHaid;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DataHaidActivity extends AppCompatActivity {

    private EditText editSiklus,editJmlHaid;
//  private CalendarView datePicker;
    private DatePicker datePicker;
    private Button btnInput;


    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    FirebaseAuth mFirebaseAuth;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;

    String mTanggal;

    String code = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_haid);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            code = null;
        }else {
            code = extras.getString("UPDATE");
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        user = mFirebaseAuth.getCurrentUser();


        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Periode");





        // inisialisasi komponen
        editSiklus = findViewById(R.id.ev_siklus);
        editJmlHaid = findViewById(R.id.ev_lamahaid);


        datePicker = findViewById(R.id.data_haid_date_picker);
        btnInput = findViewById(R.id.btn_InputData);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTanggal = datePicker.getYear()+"/"
                        + (datePicker.getMonth() + 1)+"/"
                        + datePicker.getDayOfMonth();

                Toast.makeText(DataHaidActivity.this, mTanggal ,Toast.LENGTH_SHORT).show();

                newPeriod(user.getUid()
                        , Integer.parseInt(editSiklus.getText().toString())
                        , Integer.parseInt(editJmlHaid.getText().toString())
                        , mTanggal);
//
            }
        });

        myRef.child(mFirebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&& code == null){
                    Intent i = new Intent(DataHaidActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.v("DataHaidActivity","db "+ myRef.toString());
        Log.v("DataHaidActivity", "auth "+user.getUid());


    }

    private void newPeriod(String uid, int siklus, int jumlah, String tanggal){
        PeriodeHaid haid = new PeriodeHaid(siklus, jumlah, tanggal);

        myRef.child(uid).setValue(haid);

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(DataHaidActivity.this, "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DataHaidActivity.this, "Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /// private void updateData(periode periode) {

        /// mDatabase.child("periode")
            //    .child(periode.getKey())
              //  .setValue(periode)
                // .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                   // @Override
                    // public void onSuccess(Void aVoid) {

                       // Snackbar.make(findViewById(R.id.btn_InputData), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                         //   @Override
                           // public void onClick(View view) {
                             //   finish();
                            // }
                       // }).show();
                   // }
                // });
    // }



    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

}
