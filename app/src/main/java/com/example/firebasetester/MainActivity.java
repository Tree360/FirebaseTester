package com.example.firebasetester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //FirebaseHelper.newEvent("Nates elbow stretching class3", "The hospital", "12-12-23", "Nate Hepp");
        //FirebaseHelper.pastEvent("Nates elbow stretching class3");
        //FirebaseHelper.addMember("Nates elbow stretching class", "Bungalo Jones");
        //FirebaseHelper.removeMember("Hot Dog Eating Contest", "Hitch Moneymaker");
        //String org = FirebaseHelper.getOrg("Hot Dog Eating Contest");
        //String loc = FirebaseHelper.getLocation("Hot Dog Eating Contest");
        String date = FirebaseHelper.getDate("Nates elbow stretching class");
        //Map<String, Object> mem = FirebaseHelper.getMembers("Hot Dog Eating Contest");
        //Log.e("org", org);
        Log.e("date", date.toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}