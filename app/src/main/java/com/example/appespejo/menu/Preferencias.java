package com.example.appespejo.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appespejo.menu.PreferenciasFragment;

public class Preferencias extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenciasFragment()).commit();
    }

}
