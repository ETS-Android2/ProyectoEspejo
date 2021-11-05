package com.example.appespejo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class cambiar_contra extends AppCompatActivity {

    FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    private EditText contra_actual;
    private EditText contra_nueva;
    private EditText repe_contra_nueva;
    private Button cambiar_contra;

    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_contra);
        setup();
    }


    private void setup(){

        contra_actual = this.findViewById(R.id.contra_actual);
        contra_nueva = this.findViewById(R.id.contra_nueva);
        repe_contra_nueva = this.findViewById(R.id.repe_contra_nueva);
        cambiar_contra = this.findViewById(R.id.cambiar_contra);

        cambiar_contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String contra1 = contra_actual.getText().toString();

//

            }
        });
    }
}
