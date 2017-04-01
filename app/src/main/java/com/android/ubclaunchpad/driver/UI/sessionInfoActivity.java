package com.android.ubclaunchpad.driver.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ubclaunchpad.driver.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class sessionInfoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_info);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final ArrayList<String> itemsArray = new ArrayList<String>();
        final ListView listView = (ListView) findViewById(R.id.sessionItemsList);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, itemsArray);
        listView.setAdapter(adapter);

        //    Intent intent = getIntent();
        //  String session_Name = intent.getStringExtra("SESSION_NAME");
        String session_Name = "UBC";
        final String passengerDistance = "\nP\n\t\t\t\t";
        final String driverDistance = "\nD\n\t\t\t\t";
        TextView  SessionName = (TextView) findViewById(R.id.sessionName);
        SessionName.setText(session_Name);


        //Adding Drivers
        mDatabase.child("Session Group").child(session_Name).child("drivers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Log.i("sessionInfoActivity", "Populating");
                String driverInfo = (String) dataSnapshot.child("title").getValue(String.class);
                driverInfo =  driverDistance + driverInfo;
                if(driverInfo != null) {
                    itemsArray.add(driverInfo);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("sessionInfoActivity","DataChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String removableDriver = driverDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removableDriver);
                adapter.notifyDataSetChanged();
                Log.d("sessionInfoActivity","Removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("sessionInfoActivity","DataMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("sessionInfoActivity","DataCancelled");
            }
        });

        //Adding Passengers
        mDatabase.child("Session Group").child(session_Name).child("passengers").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String passengerInfo = (String) dataSnapshot.child("title").getValue(String.class);
                passengerInfo =  passengerDistance + passengerInfo ;
                if(passengerInfo != null) {
                    adapter.add(passengerInfo);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("sessionInfoActivity","DataChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String removablePassenger = passengerDistance + (String) dataSnapshot.child("title").getValue();
                adapter.remove(removablePassenger);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("sessionInfoActivity","DataMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("sessionInfoActivity","DataCancelled");
            }
        });


        //Testing
//        mDatabase.child("Session Group").child("UBC").child("drivers").push().child("title").setValue("before :D");
//        mDatabase.child("Session Group").child("UBC").child("passengers").push().child("title").setValue("So even if the name is really long. Still it will look better then before :D");

    }
}