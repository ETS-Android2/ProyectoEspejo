package com.example.appespejo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Tab4 extends Fragment {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    public Tab4(){
        // require a empty public constructor
    }

    FirebaseFirestore db;
    FirebaseUser usuario;
    private TextView perfilDelNombre,perfilDelApellido, perfilDelCorreo;
    private TextView prueba;
    private String name,correo,apellido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab4, container, false);

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        perfilDelNombre = (TextView) v.findViewById(R.id.perfilNombre);
        perfilDelApellido = v.findViewById(R.id.perfilApellido);
        perfilDelCorreo = v.findViewById(R.id.perfilCorreo);

        db.collection("Users")
                .document(Objects.requireNonNull(usuario.getEmail()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            name = task.getResult().getString("Nombre");
                            correo = task.getResult().getString("Email");
                            apellido = task.getResult().getString("Apellido");



                            Log.d("Demo", name);

//      -----------------------------------------Tab4----------------------------------------------------
                                perfilDelNombre.setText(name);
                                perfilDelApellido.setText(apellido);
                                perfilDelCorreo.setText(correo);

//                                Glide.with(HomeActivity.this).load(usuario.getPhotoUrl()).into(fotoUsuario);
                        } else {
                            Log.e("Firestore", "Error al leer", task.getException());
                        }
                    }
                });


        return v;
    }

}
