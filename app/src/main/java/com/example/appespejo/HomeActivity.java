package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    TextView nombre,correo,name;
    Button logout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setup();
    }

    private void setup(){

        logout = this.findViewById(R.id.logout);
        nombre = this.findViewById(R.id.textView);
        correo = this.findViewById(R.id.textView2);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);


        if(signInAccount != null){
//            nombre.setText(signInAccount.getIdToken());
//            correo.setText(mAuth.getCurrentUser().getEmail());
        } else{
            nombre.setText("Nombre");
            correo.setText("Correo");
        }


//        if(usuario !=null){
//            db.collection("Usuarios")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
////                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    Toast.makeText(HomeActivity.this,document.getId() + " => " + document.getData() , Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(HomeActivity.this, "Error en home", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//            nombre.setText(usuario.getEmail());
//            correo.setText(signInAccount.getEmail());
//        }




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

}