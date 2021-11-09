package com.example.appespejo;

import android.os.Bundle;
import android.util.Log;
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

public class CambiarContra extends AppCompatActivity {

    //Variables firebase
    FirebaseAuth mAuth;
    FirebaseUser usuario;
    String contrasena;
    FirebaseFirestore db;


    private String con_actual = "";
    private String con_nueva = "";
    private String repetir="";
    private TextView prueba;
    EditText contra_actual,contra_nueva,repe;
    Button cambiar_contra;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contra);
        setup();
        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser();

    }


    private void setup(){

        contra_actual = this.findViewById(R.id.contra_actual);
        contra_nueva = this.findViewById(R.id.contra_nueva);
        repe = this.findViewById(R.id.repe_contra_nueva);
        cambiar_contra = this.findViewById(R.id.cambiar_contra);

        cambiar_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                con_actual=contra_actual.getText().toString();
                con_nueva = contra_nueva.getText().toString();
                repetir=repe.getText().toString();


                db.collection("Users")
                        .document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                String password = task.getResult().getString("Contrasena");

                                if(!con_actual.isEmpty() || !con_nueva.isEmpty()||!repetir.isEmpty()){
                                    if(con_nueva.length() < 6 ||repetir.length() <6){
                                        Toast.makeText(CambiarContra.this, "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(!repetir.equals(con_nueva)){
                                        Toast.makeText(CambiarContra.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(con_actual == password){
                                        Log.d("Demo", "He entrado a funcion cambiar");
                                        cambiar();
                                    }

                                }
                                else{
                                    Toast.makeText(CambiarContra.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



            }
        });
    }

    private void cambiar(){

        mAuth.getCurrentUser().updatePassword(con_nueva).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Demo", "He cambiado la contrasenya de mAuth");
            }
        });

        db.collection("Users")
                .document(Objects.requireNonNull(usuario.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            String contrasena = task.getResult().getString("Contrasena");
                            Log.d("Demo", "La contrasenya actual es: " + contrasena);
                            Log.d("Demo", "La contrasenya nueva es: " + con_nueva.toString());
                            db.collection("Users")
                                    .document(usuario.getUid())
                                    .update("Contrasena",con_nueva);
                        } else {
                            Log.d("Firestore", "Error al leer", task.getException());
                        }
                    }
                });
    }
}








