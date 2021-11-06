package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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
        Intent i = new Intent(this, cambiar_contra.class);
        startActivity(i);
    }
}

