package com.example.appespejo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class cambiar_contra extends AppCompatActivity {

    //Variables firebase
    FirebaseAuth mAuth;
    FirebaseUser usuario;
    String contrasena;
    FirebaseFirestore db;


    private String con_actual = "";
    private String con_nueva = "";
    private String repetir="";
    private TextView prueba;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contra);
        setup();
        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser();


/**
        //Para asignar la contraseña de un usuario a una variable.

        db.collection("Users")
                .document(Objects.requireNonNull(usuario.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            contrasena = task.getResult().getString("Email");
                            prueba.setText(contrasena);
                            //contrasena = task.getResult().getString("Contrasena");

                        } else {
                            Log.e("Firestore", "Error al leer", task.getException());
                        }
                    }
                });
**/

    }
    private void setup(){
        EditText contra_actual = this.findViewById(R.id.contra_actual);
        EditText contra_nueva = this.findViewById(R.id.contra_nueva);
        EditText repe = this.findViewById(R.id.repe_contra_nueva);
        Button cambiar_contra = this.findViewById(R.id.cambiar_contra);


        cambiar_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                con_actual=contra_actual.getText().toString();
                con_nueva = contra_nueva.getText().toString();
                repetir=repe.getText().toString();

                if(!con_actual.isEmpty() || !con_nueva.isEmpty()||!repetir.isEmpty()){
                    if(con_nueva.length() < 6 ||repetir.length() <6){
                        Toast.makeText(cambiar_contra.this, "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    else if(!repetir.equals(con_nueva)){
                        Toast.makeText(cambiar_contra.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        cambiar();
                    }
                }
                else{
                    Toast.makeText(cambiar_contra.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void cambiar(){

    }


    }








