package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Informacion extends AppCompatActivity {

    Button politica;
    Button condiciones;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion);

        politica = findViewById(R.id.politica_de_datos);
        condiciones = findViewById(R.id.condiciones);

        condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCondiciones(null);
            }
        });

        politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPolitica(null);
            }
        });


    }


    public void abrirPolitica(View view){
        Intent i = new Intent(this, politica_de_datos.class);
        startActivity(i);
    }

    public void abrirCondiciones(View view){
        Intent i = new Intent(this, condiciones_de_uso.class);
        startActivity(i);
    }
}
