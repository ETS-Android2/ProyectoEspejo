package com.example.appespejo.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appespejo.CambiarContra;
import com.example.appespejo.R;

public class seguridad extends AppCompatActivity {

    Button contrasenya; //botón para abrir el onclick de la contraseña

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seguridad);





        contrasenya = findViewById(R.id.contrasenya);

        contrasenya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirContrasenya(null);
            }
        });





    }

    public void abrirContrasenya(View view){
        Intent i = new Intent(this, CambiarContra.class);
        startActivity(i);
    }
}

