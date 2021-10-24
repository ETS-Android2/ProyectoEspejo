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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private String mail = "";
    private String pass = "";
    private String repetir="";
    private String nombree = "";
    private String apellido = "";
    FirebaseUser usuarioo;
    private final static int RC_SIGN_IN = 123;

    FirebaseAuth mAuth;
    DatabaseReference mDataBase;
    FirebaseFirestore db;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setup();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
    }

    private void setup(){

        Button register = this.findViewById(R.id.registrarmeButton);
        EditText usuario = this.findViewById(R.id.Email);
        EditText nombre = this.findViewById(R.id.Nombre);
        EditText apellidos = this.findViewById(R.id.Apellido);
        TextInputEditText contrasena = this.findViewById(R.id.password);
        TextInputEditText repit = this.findViewById(R.id.repitPassword);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mail = usuario.getText().toString();
                pass = contrasena.getText().toString();
                repetir = repit.getText().toString();
                nombree = nombre.getText().toString();
                apellido = apellidos.getText().toString();

                if(!mail.isEmpty() || !pass.isEmpty()||!repetir.isEmpty()||!nombree.isEmpty()||!apellido.isEmpty()){
                    if(pass.length() < 6 ||repetir.length() <6){
                        Toast.makeText(RegisterActivity.this, "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    else if(!repetir.equals(pass)){
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerUser();
                    }
                } else{
                    Toast.makeText(RegisterActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerFirestore(){

        EditText email = findViewById(R.id.Email);
        EditText nombre = findViewById(R.id.Nombre);
        EditText apellidos = findViewById(R.id.Apellido);
        TextInputEditText contrasena = findViewById(R.id.password);
//        db = FirebaseFirestore.getInstance();

        mail = email.getText().toString();
        pass = contrasena.getText().toString();
        nombree = nombre.getText().toString();
        apellido = apellidos.getText().toString();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Apellido", apellido);
        user.put("Contrasena", pass);
        user.put("Email", mail);
        user.put("Nombre", nombree);

        db.collection("Users")
                .document(apellido)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        Log.d("Demo", db.collection("Users").document(apellido).getFirestore().toString());
//                        Toast.makeText(RegisterActivity.this, db.collection("Users").document(apellido).get().toString(), Toast.LENGTH_SHORT).show();
//                        db.collection("Users").get().toString();
//                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));



//                        db.collection("Users").document(apellido).get()
//                                .addOnCompleteListener(
//                                        new OnCompleteListener<DocumentSnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
//                                                if (task.isSuccessful()) {
//                                                    String correo = task.getResult().getString("Email");
////                                                    double dato2 = task.getResult().getDouble("Nombre");
//
//
//                                                    Log.d("Firestore", "Email " + correo);
//                                                } else {
//                                                    Log.e("Firestore", "Error al leer", task.getException());
//                                                } }
//                                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });


//        db.collection("Users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("FireStore", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("FireStore", "Error adding document", e);
//                        Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_SHORT).show();
//                    }
//                });



    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

//                    String id = mAuth.getCurrentUser().getUid();
                    EditText usuario = findViewById(R.id.Email);
                    EditText nombre = findViewById(R.id.Nombre);
                    EditText apellidos = findViewById(R.id.Apellido);
                    TextInputEditText contrasena = findViewById(R.id.password);

                    mail = usuario.getText().toString();
                    pass = contrasena.getText().toString();
                    nombree = nombre.getText().toString();
                    apellido = apellidos.getText().toString();


                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("Apellido", apellido);
                    user.put("Contrasena", pass);
                    user.put("Email", mail);
                    user.put("Nombre", nombree);

                    db.collection("Users")
                            .document(mail)
                            .set(user);


                    login();
                    Log.d("Demo","El usuario ha sido registrado "+usuarioo.getEmail());
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

        usuarioo.sendEmailVerification();
        Log.d("Demo", "El correo ha sido enviado");
        Toast.makeText(this, "Se ha enviado un correo de confirmación", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}