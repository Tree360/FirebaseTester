package com.example.firebasetester;

import android.renderscript.Sampler;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class FirebaseHelper {
    static FirebaseDatabase events = FirebaseDatabase.getInstance("https://fir-tester-11754-default-rtdb.firebaseio.com/");
    static DatabaseReference mEvent = events.getReference();
    static DatabaseReference mCondition = mEvent.child("Events");

    public static class Event{
        public String location;
        public String date;
        public String org;
        public String desc;
        
        public Map<String, User> members;
        public Event(){
            this.date = null;
            this.location = null;
            this.org = null;
            this.members = null;
            this.desc = null;
        }
        public Event(String location, String date, String org, Map<String, User> members, String desc){
            this.date = date;
            this.location = location;
            this.org = org;
            this.members = members;
            this.desc = desc;
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

    public static void newEvent(String name, String Location, String Date, String Org, String desc){
        Map<String, User> members = new HashMap<>();
        members.put(Org, new User("Organizer"));
        Map<String, Object> event = new HashMap<>();
        event.put(name, new Event(Location, Date, Org, members, desc));
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
                String desc = event1.get("description").toString();
                Map<String, Object> nEvent = new HashMap<>();
                nEvent.put(event, new Event(location, date, org, members, desc));
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

        Map<String, Object> mem = new HashMap<>();
        mem.put(member, new User("Member"));
        members.updateChildren(mem);
    }

    public static void removeMember(String event, String member){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("members"); // .child(member);
        members.child(member).setValue(null);

        return;
    }

    public static String locHelper;
    public static String dateHelper;
    public static String orgHelper;
    public static String descHelper;
    public static ValueEventListener locListener;
    public static ValueEventListener dateListener;
    public static ValueEventListener allEventsListener;
    public static ValueEventListener membersListener;



    public static String getDescription(String event, MainActivity m){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("description");
        if(descHelper == null){
            ValueEventListener descPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    descHelper = snapshot.getValue().toString();
                    MainActivity.update_description(descHelper, m);
                    return;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            members.addValueEventListener(descPostListener);
        }
        if (descHelper == null){
            return "None";
        } else{
            return descHelper;
        }
    }

    public static String getLocation(String event, TextView tv, MainActivity m){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("location");

        if (locListener == null) {
            ValueEventListener locationPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    locHelper = snap.getValue().toString();
                    // tv.setText(locHelper);
                    MainActivity.update_location(locHelper, m);
                    return;
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            members.addValueEventListener(locationPostListener);
        }
        if (locHelper == null){
            return "None";
        } else{
            return locHelper;
        }
    }

    public static String getDate(String event, MainActivity m){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("date");
        if (locListener == null) {
            ValueEventListener datePostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    dateHelper = snap.getValue().toString();
                    // tv.setText(locHelper);
                    MainActivity.update_date(dateHelper, m);
                    return;
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            members.addValueEventListener(datePostListener);
        }
        return "None";
    }

    public static ValueEventListener orgListener;

    public static String getOrg(String event, MainActivity m){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("org");
        if (orgListener == null) {
            ValueEventListener orgPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    orgHelper = snap.getValue().toString();
                    // tv.setText(locHelper);
                    MainActivity.update_org(orgHelper, m);
                    return;
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            members.addValueEventListener(orgPostListener);
        }
        return "";
    }

    public static Map<String, Object> memHelper;

    // Map<String, Object>
    public static String getMembers(String event, MainActivity m){
        DatabaseReference ev = mCondition.child(event);
        DatabaseReference members = ev.child("members");
        if (membersListener == null) {
            ValueEventListener membersPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    //dateHelper = snap.getValue().toString();
                    // tv.setText(locHelper);
                    Log.i("Nate", "In Data Change Members");
                    HashMap mems = (HashMap) snap.getValue();
                    MainActivity.update_members(mems, m);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            members.addValueEventListener(membersPostListener);
        }
        return "None";
    }


    public static ArrayList<String> eventNames;

    // Map<String, Object>
    public static String getAllEvents(MainActivity m) {
        DatabaseReference ev = mCondition;
        if (allEventsListener == null) {
            ValueEventListener eventsPostListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snap) {
                    //dateHelper = snap.getValue().toString();
                    // tv.setText(locHelper);
                    ArrayList<HashMap> events
                            = new ArrayList<HashMap>();
                    HashMap<String,Object> event;
                    for (DataSnapshot o: snap.getChildren()){
                        event = new HashMap<>();
                        event.put("name", o.getKey().toString());
                        event.put("info", o.getValue());
                        events.add(event);
                    }
                    //HashMap mems = (HashMap) snap.getValue();
                    MainActivity.update_events(events, m);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.e("Failure", "The read failed: " + error.getCode());
                }
            };
            ev.addValueEventListener(eventsPostListener);
        }
        return "None";
    }
}
