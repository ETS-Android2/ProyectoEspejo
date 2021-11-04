package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.security.Policy;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private String mail = "";
    private String pass = "";
    private String repetir="";
    private String nombree = "";
    private String apellido = "";
    private String accountt = "";
    private boolean isActivatedButton;
    FirebaseUser usuarioo;
    RadioButton radioButton;
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
        EditText account = this.findViewById(R.id.Account);
        TextView politica = this.findViewById(R.id.politicaDePrivacidad);
        TextInputEditText contrasena = this.findViewById(R.id.password);
        TextInputEditText repit = this.findViewById(R.id.repitPassword);
        radioButton = findViewById(R.id.radioButton);

//        -----------------------------------------------------------------------------------
//        PARA ACTIVAR Y DESACTIVAR BOTON
//        -----------------------------------------------------------------------------------
        isActivatedButton = radioButton.isChecked(); //DESACTIVADO

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        ACTIVADO
                if(isActivatedButton){radioButton.setChecked(false);}
                isActivatedButton = radioButton.isChecked();
             }
        });

        politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Dialog dialogSheetDialog = new Dialog(requireContext());
                Dialog dialogPolitica = new Dialog(RegisterActivity.this);

                View dialogSheetView = LayoutInflater.from(RegisterActivity.this)
                        .inflate(R.layout.politica_privacidad,null);
                dialogPolitica.setContentView(dialogSheetView);

                dialogPolitica.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialogPolitica.show();
            }
        });

//        -----------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                mail = usuario.getText().toString();
                pass = contrasena.getText().toString();
                repetir = repit.getText().toString();
                nombree = nombre.getText().toString();
                apellido = apellidos.getText().toString();
                accountt = account.getText().toString();


                if(!mail.isEmpty() || !pass.isEmpty()||!repetir.isEmpty()||!nombree.isEmpty()||!apellido.isEmpty()|| !radioButton.isChecked()){
                    if(pass.length() < 6 ||repetir.length() <6){
                        Toast.makeText(RegisterActivity.this, "La contrasena debe consistir al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                    else if(!repetir.equals(pass)){
                        Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                    else if(!radioButton.isChecked()){
                        Toast.makeText(RegisterActivity.this, "Debes leer la politica de la privacidad", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        registerUser();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(){

//        if(mAuth.getCurrentUser().getEmail().equals("")){
//
//            EditText usuario = findViewById(R.id.Email);
//            EditText nombre = findViewById(R.id.Nombre);
//            EditText apellidos = findViewById(R.id.Apellido);
//            TextInputEditText contrasena = findViewById(R.id.password);
//
//            mail = usuario.getText().toString().trim();
//            pass = contrasena.getText().toString();
//            nombree = nombre.getText().toString().trim();
//            apellido = apellidos.getText().toString().trim();
//
//            AuthCredential credential = EmailAuthProvider.getCredential(mail, pass);
//
//            Map<String, Object> user = new HashMap<>();
//            user.put("Apellido", apellido);
//            user.put("Contrasena", pass);
//            user.put("Email", mail);
//            user.put("Nombre", nombree);
//            user.put("Account", accountt);
//
//            db.collection("Users")
//                    .document(usuarioo.getUid())
//                    .set(user);
//
//
//            Log.d("Demo","El usuario ha sido registrado "+usuarioo.getEmail());
//            Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(intent2);
//
//        }else{
            mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

//                    String id = mAuth.getCurrentUser().getUid();
                        EditText usuario = findViewById(R.id.Email);
                        EditText nombre = findViewById(R.id.Nombre);
                        EditText apellidos = findViewById(R.id.Apellido);
                        TextInputEditText contrasena = findViewById(R.id.password);

                        mail = usuario.getText().toString().trim();
                        pass = contrasena.getText().toString();
                        nombree = nombre.getText().toString().trim();
                        apellido = apellidos.getText().toString().trim();


                        // Create a new user with a first and last name
                        Map<String, Object> user = new HashMap<>();
                        user.put("Apellido", apellido);
                        user.put("Contrasena", pass);
                        user.put("Email", mail);
                        user.put("Nombre", nombree);
                        user.put("Account", accountt);
//                    user.put("Verificado", false);

                        login();

                        db.collection("Users")
                                .document(usuarioo.getUid())
                                .set(user);

                        Log.d("Demo","El usuario ha sido registrado "+usuarioo.getEmail());
                        Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent2);

                    }else{
                        Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario "
                                +task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
//        }
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