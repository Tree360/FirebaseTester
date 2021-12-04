package com.example.firebasetester;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    static FirebaseDatabase events = FirebaseDatabase.getInstance("https://fir-tester-11754-default-rtdb.firebaseio.com/");
    static DatabaseReference mEvent = events.getReference();
    static DatabaseReference mCondition = mEvent.child("Events");

    public static class Event{
        public String location;
        public String date;
        public String org;
        public Map<String, User> members;
        public Event(){
            this.date = null;
            this.location = null;
            this.org = null;
            this.members = null;
        }
        public Event(String location, String date, String org, Map<String, User> members){
            this.date = date;
            this.location = location;
            this.org = org;
            this.members = members;
        }



    }
    public static class User{
        public String name;
        public String status;
        public String bio;
        public User(){};
        public User(String status, String Bio){
            this.name = name;
            this.status = status;
            this.bio = bio;
        }
        public User(String status){
            this.status = status;
        }

    }
    public static void newEvent(String name, String Location, String Date, String Org){
        Map<String, User> members = new HashMap<>();
        members.put(Org, new User("Organizer"));
        Map<String, Object> event = new HashMap<>();
        event.put(name, new Event(Location, Date, Org, members));
        mCondition.updateChildren(event);
    }
    public static void pastEvent(String event){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference past = mEvent.child("PastEvents");
        ev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> event1 = (Map<String, Object>) dataSnapshot.getValue();
                String location = event1.get("location").toString();
                String date = event1.get("date").toString();
                Map<String,User> members = (Map<String, User>) event1.get("members");
                String org = event1.get("org").toString();
                Map<String, Object> nEvent = new HashMap<>();
                nEvent.put(event, new Event(location, date, org, members));
                past.updateChildren(nEvent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Failure", "The read failed: " + databaseError.getCode());
            }
        });
    }
    public static void addMember(String event, String member){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("members");
        members.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> mem = (Map<String,Object>) snapshot.getValue();
                mem.put(member, new User("Member"));
                members.updateChildren(mem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });

    }
    public static void removeMember(String event, String member){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("members").child(member);
        members.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });

    }
    public static String locHelper;
    public static String dateHelper;
    public static String orgHelper;
    public static String getLocation(String event){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("location");
        members.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locHelper = snapshot.getValue().toString();
                Log.e("location", locHelper);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });
        return locHelper;
    }


    public static String getDate(String event){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("date");
        members.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dateHelper = snapshot.getValue().toString();
                Log.e("date", dateHelper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });
        return dateHelper;
    }


    public static String getOrg(String event){
        DatabaseReference ev = mCondition.child(event);

        ev.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Object> event1 = (Map<String, Object>) snapshot.getValue();
                String org = event1.get("org").toString();
                orgHelper = org;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });
        return orgHelper;
    }


    public static Map<String, Object> memHelper;
    public static Map<String, Object> getMembers(String event){
        DatabaseReference ev = mCondition.child(event);
        ev.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Object> event1 = (Map<String, Object>) snapshot.getValue();
                memHelper = (Map<String, Object>) event1.get("members");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Failure", "The read failed: " + error.getCode());
            }
        });
        return memHelper;
    }
    public static ArrayList<String> eventNames;
    public static ArrayList<String> allEvents(){
        mCondition.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

            }
        });


        return eventNames;
    }



}
