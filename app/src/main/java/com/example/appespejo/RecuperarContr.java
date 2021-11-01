package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RecuperarContr extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private EditText emaill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contr);
        setup();
    }


    private void setup(){

        Button enviar;

        emaill = this.findViewById(R.id.correo);
        enviar = this.findViewById(R.id.enviar);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emaill.getText().toString();

//                On click complueba si hay usuario con correo asi y luego envia el correo con la link para recuperar password
                if(!email.isEmpty()){
                    mDialog.setMessage("Espera un momento...");
                    mDialog.setCanceledOnTouchOutside(false); //Para que el usuario no pueda quitar este mensaje
                    mDialog.show();

                    enviarCorreo();
//                    Anadir comprobacion de que el correo de verdad se ha enviado. Describir errores.
//                    Toast.makeText(RecuperarContr.this, "Tu correo ha sido enviado", Toast.LENGTH_SHORT).show();

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(RecuperarContr.this, "Ingresa tu correo", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    Toast.makeText(RecuperarContr.this,"Completa tu email",Toast.LENGTH_SHORT).show();
                }

                mDialog.dismiss();
            }
        });
    }

//    Implementar la funcion que envie el correo

    private void enviarCorreo(){

        String email;
        EditText mail = this.findViewById(R.id.correo);
        email = mail.getText().toString();
        mAuth.setLanguageCode("es");
//        String emailAdress = email;


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecuperarContr.this,"Correo enviado",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RecuperarContr.this,"Error en enviar el correo",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}