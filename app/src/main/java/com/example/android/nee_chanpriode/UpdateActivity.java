package com.example.android.nee_chanpriode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTanggal = datePicker1.getYear() + "/"
                        + (datePicker1.getMonth() + 1) + "/"
                        + datePicker1.getDayOfMonth();
                Toast.makeText(UpdateActivity.this, mTanggal, Toast.LENGTH_SHORT).show();
//                updateData(user.getUid()
//                        , Integer.parseInt(editSiklus1.getText().toString())
//                        , Integer.parseInt(editJmlHaid1.getText().toString())
//                        , mTanggal);

            }

        });

        myRef.child(mFirebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Intent i = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        private void updateData(Periode newPeriod) {
//
//         mDatabase.child("periode")
//            .child(periode.getKey())
//          .setValue(periode)
//         .addOnSuccessListener(this, new OnSuccessListener<Void>() {
//         @Override
//         public void onSuccess(Void aVoid) {
//
//        Snackbar.make(findViewById(R.id.btn_InputData), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
//           @Override
//        public void onClick(View view) {
//               finish();
//        }
//        }).show();
//         }
//         });
//
//    }

    }
}