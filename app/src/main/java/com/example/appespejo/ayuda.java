package com.example.appespejo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ayuda extends AppCompatActivity {


    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    Button web;
    Button llamar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayuda);

        web = findViewById(R.id.web);
        llamar = findViewById(R.id.llamar);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPagina(null);
            }
        });

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarTelefono(null);
            }
        });

    }

    public void abrirPagina(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.androidcurso.com/"));
        startActivity(intent);
    }

    public void llamarTelefono(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:962849347"));
        startActivity(intent);
    }


}
