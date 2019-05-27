package com.example.android.nee_chanpriode;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.nee_chanpriode.CalendarLib.CalendarView;
import com.example.android.nee_chanpriode.Model.PeriodeHaid;
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

public class MainActivity extends AppCompatActivity{

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START: INIT FIREBASE]
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Periode").child(mAuth.getUid());







        ValueEventListener valueEventListener = new ValueEventListener() {
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

                    for(int th = 0; th < 12; th++){
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mReference.addListenerForSingleValueEvent(valueEventListener);


//        Log.v(TAG, "siklus "+siklus);
//        Log.v(TAG, "jmlHari "+jumlahHari);
//        Log.v(TAG, "tglHari "+tglHaid);


//        startDate.set(Calendar.YEAR, Calendar.MONTH, 12);
//        for(int i = 0; i < jumlahHari; i++){
////            haid.add();
//        }

//        Log.v(TAG, "hari " + jumlahHari);
//        Log.v(TAG, "siklus " + siklus);
//        Log.v(TAG, "tglHaid " + tglHaid);

        // [FINISH: INIT FIREBASE]

        // [START: INIT TOOLBAR]
        Toolbar toolbar = findViewById(R.id.main_toolbar);
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

    }


}