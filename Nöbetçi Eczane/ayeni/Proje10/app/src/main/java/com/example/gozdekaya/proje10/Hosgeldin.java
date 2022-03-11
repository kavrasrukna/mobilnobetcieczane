package com.example.gozdekaya.proje10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Hosgeldin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosgeldin);
    }

    public void git(View view) {
        Intent girisekrani=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(girisekrani);
        finish();
    }
}
