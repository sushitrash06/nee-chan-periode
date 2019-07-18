package com.example.android.nee_chanpriode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.nee_chanpriode.CalendarLib.CalendarView;
import com.example.android.nee_chanpriode.Callback.MyCallback;
import com.example.android.nee_chanpriode.Model.PeriodeHaid;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;


    private PeriodeHaid periodeHaid;

    private CalendarView calendarView;


    int jumlahHari;
    int siklus;
    String tglHaid;

    HashSet<Date> events = new HashSet<>();

    Button btn, btnUpdate;


    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // [START: INIT FIREBASE]
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Periode").child(mAuth.getUid());


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        // [START: INIT TOOLBAR]
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // [FINISH: INIT TOOLBAR]



        // [START: INIT CALENDAR]
        calendarView = findViewById(R.id.activity_main_calendar_view);

        Calendar c = Calendar.getInstance();


        HashSet<Date> events = new HashSet<>();
        events.add(c.getTime());

        calendarView.updateCalendar(events);



        calendarView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        btn = findViewById(R.id.btnLogout);

        readData(new MyCallback() {
            @Override
            public void readData(PeriodeHaid callback) {
                Log.v(TAG, callback.toString());
            }
        });
        btn.setOnClickListener(this);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);


        // [FINISH: INIT FIREBASE]



    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnLogout){
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }else if(v.getId() == R.id.btnUpdate){
            Intent i = new Intent(MainActivity.this, DataHaidActivity.class);
            i.putExtra("UPDATE", "111");
            startActivity(i);
        }



    }

    public void readData(final MyCallback callback){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                periodeHaid = dataSnapshot.getValue(PeriodeHaid.class);
                jumlahHari = periodeHaid.getJml_hari();
                siklus = periodeHaid.getSiklus();
                tglHaid = periodeHaid.getTgl_haid();
                Log.v(TAG, "siklus "+siklus);
                Log.v(TAG, "jmlHari "+jumlahHari);
                Log.v(TAG, "tglHari "+tglHaid);

                HashSet<Date> haid = new HashSet<>();
                Calendar startDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");

                try {


                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(tglHaid));

                    Date date = cal.getTime();

                    Log.v(TAG, "tanggal awal : "+ cal.get(Calendar.DAY_OF_MONTH) +" "+ cal.get(Calendar.MONTH));

                    int tahun = cal.get(Calendar.YEAR);
                    int bulan = cal.get(Calendar.MONTH);
                    int hari = cal.get(Calendar.DAY_OF_MONTH);

                    for(int th = 0; th < 36; th++){
                        for(int i = 0; i < siklus; i++){
                            if( i < jumlahHari){
                                if(hari == 1){
                                    bulan = bulan + 1;
                                    cal.set(tahun, bulan, hari);
                                    events.add(cal.getTime());
                                }else if(bulan == 0){
                                    tahun = tahun + 1;
                                    cal.set(tahun, bulan, hari);
                                    events.add(cal.getTime());
                                }
                                else{
                                    cal.set(tahun, bulan, hari);
                                    events.add(cal.getTime());
                                }
                            }

                            hari++;

                            Log.v(TAG, "date : "+ date.getTime());
                        }
                    }


                    calendarView.updateCalendar(events);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                callback.readData(periodeHaid);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigasi, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.bt_about){
            startActivity(new Intent(this, AboutActivity.class));
        } else if (item.getItemId() == R.id.bt_update) {

            Intent i = new Intent(MainActivity.this, DataHaidActivity.class);
            startActivity(i);

        } else if (item.getItemId() == R.id.btnLogout) {
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        }

        return true;
    }
}


