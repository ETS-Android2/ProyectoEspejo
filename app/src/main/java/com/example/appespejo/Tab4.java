package com.example.appespejo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appespejo.cambiardatos.CambiarNombre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
    private String name,correo,apellido,foto,fotoGoogle;
    ImageView perfilDelFoto;
    Dialog dialog;
    Animation animacion2;
    Button guardarCambios;
    String nuevoEmail;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.tab4, container, false);
//        View dialogNombre = inflater.inflate(R.layout.cambiarnombre, container, false);

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        perfilDelNombre = (TextView) v.findViewById(R.id.perfilNombre);
        perfilDelApellido = v.findViewById(R.id.perfilApellido);
        perfilDelCorreo = v.findViewById(R.id.perfilCorreo);
        perfilDelFoto = v.findViewById(R.id.perfilFoto);
//        guardarCambios = dialogNombre.findViewById(R.id.guardarCambios);

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

//    Funcion para todos los setOnClickListener

    private void setOnClick(View view){

        perfilDelCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.cambiarnombre,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);
                TextView direccionActual = bottomSheetView.findViewById(R.id.direccionActual);
                EditText nuevoCorreo = bottomSheetView.findViewById(R.id.cambiarCorreo);
                nuevoEmail = nuevoCorreo.getText().toString();


                direccionActual.setText("Tu direccion de correo electronico actual es \n" + usuario.getEmail() + " \n¿Por cual te gustaria cambiarla?");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        db.collection("Users")
//                                .document(usuario.getUid())
//                                .update("Email",nuevoEmail);

                        Log.d("Demo", "El correo cogido es: " + nuevoEmail);

                    }
                });

//                final Dialog dialog = new Dialog(getContext());
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                View dialogView = LayoutInflater.from(getContext().getApplicationContext())
//                        .inflate(R.layout.cambiarnombre,null);
////                dialog.setContentView(R.layout.cambiarnombre);
//                dialog.setContentView(dialogView);
//                dialog.show();
//                LinearLayout guardar = dialog.findViewById(R.id.guardarCambios);

            }
        });
    }

}
