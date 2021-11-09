package com.example.appespejo;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.api.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tab4 extends Fragment {

    public Tab4(){
        // require a empty public constructor
    }

    public Tab4(Context context){
        this.context=context;
    }

    FirebaseFirestore db;
    FirebaseUser usuario;
    FirebaseAuth mAuth;
    TextView perfilDelNombre,perfilDelApellido, perfilDelCorreo, perfilDelAccount;
    String name,correo,apellido,foto,account;
    ImageView perfilDelFoto;
    Dialog dialog;
    Animation animacion2;
    ConstraintLayout dialogWindow;
    NestedScrollView infoPersona;
    Button cerrarSesion,registrarAnonimo, verificado;
    EditText nuevoApellido,nuevoCorreo,nuevoNombre, nuevoAccount;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.tab4, container, false);


        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        this.context=context;

        perfilDelNombre = v.findViewById(R.id.perfilNombre);
        perfilDelApellido = v.findViewById(R.id.perfilApellido);
        perfilDelCorreo = v.findViewById(R.id.perfilCorreo);
        perfilDelAccount = v.findViewById(R.id.perfilAccount);
        perfilDelFoto = v.findViewById(R.id.perfilFoto);
        dialogWindow = v.findViewById(R.id.dialogWindow);
        registrarAnonimo = v.findViewById(R.id.registrarAnonimo);
        cerrarSesion = v.findViewById(R.id.cerrarSesion);
        infoPersona = v.findViewById(R.id.infoPersona);

        animacion2 = AnimationUtils.loadAnimation(getContext().getApplicationContext(),R.anim.desplazamiento_abajo);

        setOnClick(v);


//        -----------------------------------------------------------------------------------
//      Para mostrar por la pantalla los datos del usuario
//        -----------------------------------------------------------------------------------

        if(mAuth.getCurrentUser() == null){

        }

        if(mAuth.getCurrentUser().getEmail().equals("")){
//    El codigo para mostrar el dialog window
            perfilDelNombre.setText("Name");
            perfilDelApellido.setText("Apellido");
            perfilDelCorreo.setText("Correo");
            perfilDelAccount.setText("Account");
            dialogWindow.setVisibility(View.VISIBLE);
            cerrarSesion.setVisibility(View.INVISIBLE);
            infoPersona.setVisibility(View.INVISIBLE);
        }

        else{
            cerrarSesion.setVisibility(View.VISIBLE);
            infoPersona.setVisibility(View.VISIBLE);
            dialogWindow.setVisibility(View.INVISIBLE);

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
                                account = task.getResult().getString("Account");
                                foto = task.getResult().getString("photoUrl");

//      -------------------------------------Tab4-------------------------------------------
                                perfilDelNombre.setText(name);
                                perfilDelApellido.setText(apellido);
                                perfilDelCorreo.setText(correo);
                                perfilDelAccount.setText("@"+account);
                                if(foto!=null){
                                    Glide.with(Tab4.this)
                                            .load(foto)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(perfilDelFoto);
                                }

                                if(account==null){
                                    perfilDelAccount.setText("Anyadir usernane");
                                    perfilDelAccount.setTextColor(0xff555555);
                                }

                            }
                            else {
                                Log.e("Firestore", "Error al leer", task.getException());
                            }
                        }
                    });
        }
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

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);
                TextView direccionActual = bottomSheetView.findViewById(R.id.nombreActual);
                nuevoCorreo = bottomSheetView.findViewById(R.id.cambiarNombre);

                direccionActual.setText("Tu direccion de correo electronico actual es \n" + perfilDelCorreo.getText().toString() + " \n¿Por cual te gustaria cambiarla?");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Objects.requireNonNull(mAuth.getCurrentUser()).updateEmail(nuevoCorreo.getText().toString());

                        Log.d("Demo", "El correo cogido es: " + nuevoCorreo.getText().toString());
                        Toast.makeText(getContext(), "Tu correo ha sido cambiado correstamente", Toast.LENGTH_SHORT).show();

                        login();


                        Dialog dialogSheetDialog = new Dialog(requireContext());

                        View dialogSheetView = LayoutInflater.from(getContext())
                                .inflate(R.layout.correo_no_verificado,null);
                        dialogSheetDialog.setContentView(dialogSheetView);
                        dialogSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));


                        bottomSheetDialog.cancel();

                        dialogSheetDialog.show();
                        verificado = dialogSheetView.findViewById(R.id.verificado);

                        verificado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(usuario.isEmailVerified()){

                                    dialogSheetDialog.cancel();

                                    db.collection("Users")
                                            .document(usuario.getUid())
                                            .update("Email", nuevoCorreo.getText().toString());
//                                .update(user);

                                }else{

                                    Toast.makeText(getContext(), "Verifica tu correo antes por favor", Toast.LENGTH_SHORT).show();
                                    dialogSheetDialog.show();
                                    dialogSheetDialog.setCancelable(false);
                                    dialogSheetDialog.setCanceledOnTouchOutside(false);
                                }
                            }
                        });

//
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

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
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

        perfilDelAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                View bottomSheetView = LayoutInflater.from(getContext().getApplicationContext())
                        .inflate(R.layout.cambiar_account,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                bottomSheetDialog.show();

                Button guardar = bottomSheetView.findViewById(R.id.guardarCambios);

                TextView accountActual = bottomSheetView.findViewById(R.id.nombreActual);
                nuevoAccount = bottomSheetView.findViewById(R.id.cambiarNombre);

                accountActual.setText("Tu account actual es: " + perfilDelAccount.getText().toString() + "\nA cual lo quieres cambiar?");

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        db.collection("Users")
                                .document(usuario.getUid())
                                .update("Account", nuevoAccount.getText().toString());

                        Toast.makeText(getContext(), "Tu account ha sido cambiado correstamente", Toast.LENGTH_SHORT).show();
                        Log.d("Demo", "El account nuevo es: " + nuevoAccount.getText().toString());

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
