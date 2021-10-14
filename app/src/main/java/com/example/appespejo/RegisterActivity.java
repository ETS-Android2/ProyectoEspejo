package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private String name = "";
    private String pass = "";
    private String repetir="";
    private String nombree = "";
    private String apellido = "";
    FirebaseUser usuarioo;
    private final static int RC_SIGN_IN = 123;

    FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setup();

        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void setup(){

        Button register = this.findViewById(R.id.registrarmeButton);
        EditText usuario = this.findViewById(R.id.EditTextRegUsername);
        EditText nombre = this.findViewById(R.id.Nombre);
        EditText apellidos = this.findViewById(R.id.Apellido);
        TextInputEditText contrasena = this.findViewById(R.id.textInputEditText);
        TextInputEditText repit = this.findViewById(R.id.repitPassword);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                name = usuario.getText().toString();
                pass = contrasena.getText().toString();
                repetir = repit.getText().toString();
                nombree = nombre.getText().toString();
                apellido = apellidos.getText().toString();

                if(!name.isEmpty() || !pass.isEmpty()||!repetir.isEmpty()||!nombree.isEmpty()||!apellido.isEmpty()){
                    if(pass.length() < 6 ||repetir.length() <6){
                        Toast.makeText(RegisterActivity.this, "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    else if(!repetir.equals(pass)){
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
//                    else if(name.equals(usuarioo.getEmail())){
//                        Toast.makeText(RegisterActivity.this, "El usuario ya esta registrado", Toast.LENGTH_SHORT).show();
//                    }
                    else{
//                        verificamos correo
//                        Toast.makeText(RegisterActivity.this, "El mensaje para verificar tu correo ha sido enviado correctamente", Toast.LENGTH_SHORT).show();
//                        Si correo esta verificado registramos al usuario.
                        registerUser();
                    }
                } else{
                    Toast.makeText(RegisterActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(name,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
/*
                    String id = mAuth.getCurrentUser().getUid();
        EditText usuario = this.findViewById(R.id.EditTextRegUsername);
        EditText nombre = this.findViewById(R.id.Nombre);
        EditText apellidos = this.findViewById(R.id.Apellido);
        TextInputEditText contrasena = this.findViewById(R.id.textInputEditText);

        name = usuario.getText().toString();
        pass = contrasena.getText().toString();
        nombree = nombre.getText().toString();
        apellido = apellidos.getText().toString();

        // Create a new user with a first and last name
        Map<String, String> user = new HashMap<>();
        user.put("Apellido", apellido);
        user.put("Contrasena", pass);
        user.put("Email", name);
        user.put("Nombre", nombree);

        db.collection("Usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RegisterActivity.this, "Gracias por tu registro"+nombree, Toast.LENGTH_SHORT).show();

                        Intent intent2 = new Intent(RegisterActivity.this, HomeActivity.class);
                        startActivity(intent2);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario ",Toast.LENGTH_LONG).show();
                    }
                });


//        mAuth.createUserWithEmailAndPassword(name,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//
///*                    Map<String,Object> map = new HashMap<>();
//                    map.put("Usuario",name);
//                    map.put("Contrasena",pass);
//
//
//
//                    String id = mAuth.getCurrentUser().getUid();
//
//                    mDataBase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> taskZ) {
//                            if(taskZ.isSuccessful()){
//*/
                    //si está verificado pasa esto
                    login();
                    Log.d("Demo","El usuario ha sido registrado"+usuarioo.getEmail());
//                    usuarioo.sendEmailVerification();
//                    Toast.makeText(RegisterActivity.this, "Se ha enviado un correo de confirmación", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent2);

                }else{
                    Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario "
                            +task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void login() {

        usuarioo = FirebaseAuth.getInstance().getCurrentUser();
//        if (usuarioo.isEmailVerified() && usuarioo != null) {
//            Toast.makeText(this, "inicia sesión: "+usuarioo.getDisplayName()+
//                    " - "+ usuarioo.getEmail(),Toast.LENGTH_LONG).show();
//            Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
//            startActivity(i);
//        }
//        else {

//            if (usuarioo != null) {
                usuarioo.sendEmailVerification();
                Log.d("Demo", "El correo ha sido enviado");
                Toast.makeText(this, "Se ha enviado un correo de confirmación", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
//            }
//        }
    }
}