package com.saif.sslassignment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;


import Fragments.FragmentLogin;
import Preference.SSLSharedPreferences;
import Utils.FragmentUtilities;

public class MainActivity extends AppCompatActivity {
    private FragmentLogin fragmentLogin;
   // private FragmentAdData fragmentAdData;
    private SSLSharedPreferences sharedPreference;

    public static FrameLayout viewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreference=SSLSharedPreferences.getSharedPreferences(this);
        viewContainer=findViewById(R.id.container);
      //  fragmentAdData = new FragmentAdData();
        fragmentLogin=new FragmentLogin();
        if(sharedPreference.getLogInState())
        {
            //new FragmentUtilities(this).addFragment(R.id.container, fragmentAdData);
        }
        else {
            new FragmentUtilities(this).addFragment(R.id.container, fragmentLogin);
        }



    }
}