package com.example.appespejo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Tab4 extends Fragment {

    public Tab4(){
        // require a empty public constructor
    }

    FirebaseFirestore db;
    FirebaseUser usuario;
    FirebaseAuth mAuth;
    private TextView perfilDelNombre,perfilDelApellido, perfilDelCorreo;
    private TextView prueba;
    private String name,correo,apellido,foto,fotoGoogle;
    ImageView perfilDelFoto;
    Dialog dialog;
    Animation animacion2;
    Button guardarCambios;
    EditText nuevoApellido,nuevoCorreo,nuevoNombre;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.tab4, container, false);


        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        perfilDelNombre = v.findViewById(R.id.perfilNombre);
        perfilDelApellido = v.findViewById(R.id.perfilApellido);
        perfilDelCorreo = v.findViewById(R.id.perfilCorreo);
        perfilDelFoto = v.findViewById(R.id.perfilFoto);
        ConstraintLayout noVerificado = v.findViewById(R.id.correoNoVerificado);


//        -----------------------------------------------------------------------------------
//        Si el usuario al cambiar el correo no lo tiene verificado aparece Verifica tu correo
//        -----------------------------------------------------------------------------------

//        if(!usuario.isEmailVerified()){
//            noVerificado.setVisibility(View.VISIBLE);
//        }

        animacion2 = AnimationUtils.loadAnimation(getContext().getApplicationContext(),R.anim.desplazamiento_abajo);

        setOnClick(v);



        db.collection("Users")
                .document(Objects.requireNonNull(usuario.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            name = task.getResult().getString("Nombre");
                            correo = task.getResult().getString("Email");
                            apellido = task.getResult().getString("Apellido");
                            foto = task.getResult().getString("photoUrl");


//      -----------------------------------------Tab4----------------------------------------------------
                                perfilDelNombre.setText(name);
                                perfilDelApellido.setText(apellido);
                                perfilDelCorreo.setText(correo);
                                Glide.with(Tab4.this)
                                        .load(foto)
                                        .into(perfilDelFoto);

                        } else {
                            Log.e("Firestore", "Error al leer", task.getException());
                        }
                    }
                });

        return v;
    }

//        -----------------------------------------------------------------------------------
//    Funcion para todos los setOnClickListener
//        -----------------------------------------------------------------------------------

    private void setOnClick(View view){

        perfilDelCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.cambiar_correo,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);
                TextView direccionActual = bottomSheetView.findViewById(R.id.nombreActual);
                nuevoCorreo = bottomSheetView.findViewById(R.id.cambiarNombre);

                direccionActual.setText("Tu direccion de correo electronico actual es \n" + usuario.getEmail() + " \n¿Por cual te gustaria cambiarla?");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection("Users")
                                .document(usuario.getUid())
                                .update("Email",nuevoCorreo.getText().toString());

                        Log.d("Demo", "El correo cogido es: " + nuevoCorreo.getText().toString());
                        Toast.makeText(getContext(), "Tu correo ha sido cambiado correstamente", Toast.LENGTH_SHORT).show();

                        login();
                        bottomSheetDialog.cancel();
                        updateUI(mAuth.getCurrentUser());
                    }
                });
            }
        });

        perfilDelNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.cambiar_nombre,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);

                TextView nombreActual = bottomSheetView.findViewById(R.id.nombreActual);
                nuevoNombre = bottomSheetView.findViewById(R.id.cambiarNombre);

                nombreActual.setText("Tu nombre debe contene no mas que 50 caracteres");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection("Users")
                                .document(usuario.getUid())
                                .update("Nombre", nuevoNombre.getText().toString());

                        Toast.makeText(getContext(), "Tu nombre ha sido cambiado correstamente", Toast.LENGTH_SHORT).show();
                        Log.d("Demo", "El nombre nuevo es: " + nuevoNombre.getText().toString());

                        bottomSheetDialog.cancel();
                        updateUI(mAuth.getCurrentUser());
                    }
                });

            }
        });

        perfilDelApellido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.cambiar_apellido,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);

                TextView apellidoActual = bottomSheetView.findViewById(R.id.nombreActual);
                nuevoApellido = bottomSheetView.findViewById(R.id.cambiarNombre);

                apellidoActual.setText("Tu apellido debe contene no mas que 50 caracteres");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection("Users")
                                .document(usuario.getUid())
                                .update("Apellido", nuevoApellido.getText().toString());

                        Toast.makeText(getContext(), "Tu nombre ha sido cambiado correstamente", Toast.LENGTH_SHORT).show();
                        Log.d("Demo", "El nombre nuevo es: " + nuevoApellido.getText().toString());

                        bottomSheetDialog.cancel();
                        updateUI(mAuth.getCurrentUser());
                    }
                });

            }
        });
    }



//        -----------------------------------------------------------------------------------
//    Funcion para cambiar el correo y mandar el correo de verificacion
//        -----------------------------------------------------------------------------------
    private void login() {

        usuario = FirebaseAuth.getInstance().getCurrentUser();

        usuario.sendEmailVerification();
        Log.d("Demo", "El correo ha sido enviado");
        Toast.makeText(getContext().getApplicationContext(), "Se ha enviado un correo de confirmación", Toast.LENGTH_LONG).show();

    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(getContext().getApplicationContext(), getContext().getClass()));
        }else{
            Toast.makeText(getContext(), "Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }


}
