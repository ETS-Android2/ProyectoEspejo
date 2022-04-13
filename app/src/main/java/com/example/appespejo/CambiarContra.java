package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appespejo.menu.seguridad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class CambiarContra extends AppCompatActivity {

    //Variables firebase
    FirebaseAuth mAuth;
    FirebaseUser usuario;
    String contrasena;
    FirebaseFirestore db;
    String password;
    private TextView prueba;
    TextInputEditText contra_actual,contra_nueva,repe;
    Button cambiar_contra;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contra);

        contra_actual = findViewById(R.id.contra_actual);
        contra_nueva = findViewById(R.id.contra_nueva);
        repe = findViewById(R.id.repe_contra_nueva);
        cambiar_contra = findViewById(R.id.cambiar_contra);

        setup();
        mAuth = FirebaseAuth.getInstance();
        usuario = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

    }


    private void setup(){

        cambiar_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si los campos estan relenados
                if(!contra_actual.getText().toString().isEmpty() && !contra_nueva.getText().toString().isEmpty() && !repe.getText().toString().isEmpty()){

                    // comprobando si las nuevas contraseñas son iguales
                    if(!contra_nueva.getText().toString().equals(repe.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Las nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                    if(contra_nueva.getText().toString().length() < 6 || repe.getText().toString().length() <6){
                        Toast.makeText(getApplicationContext(), "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    // Si esta rellenado, hace consula a bd para sacar la contraseña
                    else{

                        //Sacando la contrasenya de bd
                        db.collection("Users")
                                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){

                                            //La guardamos en la variable
                                            password = task.getResult().getString("Contrasena");

                                            //Comprobamos si la introducida contrasenya actual de usuario es igual a la cual la tiene ahora
                                            if(password.equals(contra_actual.getText().toString())){

                                                //Si las contraseñas se coinciden podemos cambiarla sacando credenciales del usuario
                                                AuthCredential credential = EmailAuthProvider.getCredential(usuario.getEmail().toString(), password);

                                                usuario.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        usuario.updatePassword(contra_nueva.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                db.collection("Users")
                                                                        .document(usuario.getUid())
                                                                        .update("Contrasena", contra_nueva.getText().toString());
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Error en cambiar la contraseña", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                                Toast.makeText(getApplicationContext(), "Tu contraseña ha sido cambiada correctamente", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CambiarContra.this, seguridad.class));

                                            } else{
                                                Toast.makeText(getApplicationContext(), "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}



