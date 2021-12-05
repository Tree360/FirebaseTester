package com.example.firebasetester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Response;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {
    static boolean location_updated = true;
    static String location = "None";
    static ArrayList<HashMap> events;
    static String description = "Gumbo Jones";


    public static void update_location(String loc, MainActivity c){  // Fragment f
        location = loc;
        c.setText("loc", loc);
    }
    public static void update_description(String desc, MainActivity m){
        description = desc;

    }

    public static void update_date(String date, MainActivity c){  // Fragment f
        c.setText("date", date);
    }

    public static void update_org(String org, MainActivity c){  // Fragment f
        c.setText("org", org);
    }

    public static void update_members(HashMap members, MainActivity c){  // Fragment f
        Log.i("Nate", "In update members");
        for (Object key : members.keySet()) {
            Log.i("Nate", "MEMBERS "+key.toString());
            Log.i("Nate", "Role "+members.get(key).toString());
        }
        c.setText("mems", "Members: "+ String.valueOf(members.size()));
        Log.i("Nate", "\n\n");
    }

    public static void update_events(ArrayList<HashMap> events, MainActivity m) {
        MainActivity.events = events;

        m.setText("events", "Events: "+String.valueOf(events.size()));
    }

    public static void add_member(View v){
        FirebaseHelper.addMember("Hot Dog Eating Contest", "Trevin B");
        FirebaseHelper.addMember("Hot Dog Eating Contest", "Young Tizzy");
        FirebaseHelper.addMember("Hot Dog Eating Contest", "Tree 360");
    }
    public static void remove_member(View v){
        FirebaseHelper.removeMember("Hot Dog Eating Contest", "Trevin B");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseHelper.newEvent("Nates elbow stretching class4", "The hospital", "12-12-23", "Nate Hepp", "Big boys eating big hogs");
        FirebaseHelper.pastEvent("Nates elbow stretching class3");
        //FirebaseHelper.addMember("Nates elbow stretching class", "Bungaloooo Jones");
        //FirebaseHelper.removeMember("Hot Dog Eating Contest", "Hitch Moneymaker");
        //String org = FirebaseHelper.getOrg("Hot Dog Eating Contest");
        //String loc = FirebaseHelper.getLocation("Hot Dog Eating Contest");
        //String date = FirebaseHelper.getDate("Nates elbow stretching class");
        //Map<String, Object> mem = FirebaseHelper.getMembers("Hot Dog Eating Contest");
        //Log.e("org", org);
        // ArrayList<String> events = FirebaseHelper.allEvents();
        // Log.e("event1",events.get(0));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void post_location(View v){
        TextView text = (TextView) findViewById(R.id.txtViewHome);
        String eventName = "Hot Dog Eating Contest";
        //String response = FirebaseHelper.getLocation("Hot Dog Eating Contest", text, this);
        String s = FirebaseHelper.getLocation(eventName, text, this);
        String s1 = FirebaseHelper.getOrg(eventName, this);
        String s2 = FirebaseHelper.getDate(eventName, this);
        String s3 = FirebaseHelper.getMembers(eventName, this);
        String s4 = FirebaseHelper.getAllEvents(this);
        return;
        // text.setText(response);
    }

    public void setText(String field, String text){
        TextView tv = null;
        if (field.equals("loc")) {
            tv = (TextView) findViewById(R.id.txtViewHome);
        }
        else if (field.equals("date")) {
            tv = (TextView) findViewById(R.id.datetxt);
        }
        else if (field.equals("org")) {
            tv = (TextView) findViewById(R.id.orgtxt);
        }
        else if (field.equals("events")) {
            tv = (TextView) findViewById(R.id.allevents);
        }
        else if (field.equals("mems")) {
            tv = (TextView) findViewById(R.id.allmems);
        }
        if (tv == null){
            return;
        }
        // String response = FirebaseHelper.getLocation("Hot Dog Eating Contest", text, this);
        tv.setText(text);
        }


}