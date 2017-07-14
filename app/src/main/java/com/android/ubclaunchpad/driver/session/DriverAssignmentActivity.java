package com.android.ubclaunchpad.driver.session;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.session.models.SessionModel;
import com.android.ubclaunchpad.driver.user.User;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DriverAssignmentActivity extends AppCompatActivity {

    private static final String TAG = DriverAssignmentActivity.class.getSimpleName();

    private DatabaseReference session;
    private ChildEventListener listener;
    private DriverAssignmentAdapter adapter;
    private List<User> passengers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_assignment);

        String sessionName = getIntent().getStringExtra("Session");
        RecyclerView driversPassengers = (RecyclerView) findViewById(R.id.passengers_container);
        passengers = new ArrayList<>();
        adapter = new DriverAssignmentAdapter(this, passengers);

        driversPassengers.setAdapter(adapter);
        session = FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseSessionEndpoint)
                .child(sessionName);

        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addPassengerToAdapter(dataSnapshot, adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        session.child(StringUtils.FirebaseSessionDriverEndpoint).addChildEventListener(listener);
    }


    private void addPassengerToAdapter(DataSnapshot dataSnapshot, final DriverAssignmentAdapter adapter) {
        FirebaseUtils.getDatabase()
                .child(StringUtils.FirebaseUserEndpoint)
                .child(dataSnapshot.getValue().toString()) //get the added user's UID
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            passengers.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Log.d(TAG, "Passenger " + dataSnapshot.getValue() + " is added to this driver");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        session.removeEventListener(listener);
    }
}
