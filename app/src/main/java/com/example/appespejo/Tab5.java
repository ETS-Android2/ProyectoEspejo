package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tab5 extends Fragment {

//    private TextView username;
      FirebaseUser usuario;
      Button logout;
      FirebaseAuth mAuth;
      View HomeActivity;
    ConstraintLayout dialogWindow;


   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/
   public Tab5(){

       usuario = FirebaseAuth.getInstance().getCurrentUser();
       // require a empty public constructor
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab5, container, false);

//        -----------------------------------------------------------------------------------
//        Base de datos
//        -----------------------------------------------------------------------------------

        mAuth = FirebaseAuth.getInstance();

//        -----------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------


        if(mAuth.getCurrentUser().getEmail().equals("")){

            dialogWindow.setVisibility(View.VISIBLE);
        }

        return v;
    }

//        -----------------------------------------------------------------------------------
//    Funciones
//        -----------------------------------------------------------------------------------

    private void prueba(){
//        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
    }


}
