package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tab5 extends Fragment {

    private TextView username;
    private FirebaseUser usuario;
    private Button logout;
    String hello = "";


   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/
   public Tab5(){
       // require a empty public constructor
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1, container, false);
        username = view.findViewById(R.id.nombreUsuario);
        logout = view.findViewById(R.id.logout);

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        hello = hello.concat(" ").concat(usuario.getEmail());
        username.setText(hello);

        setup();
        return inflater.inflate(R.layout.tab5, container, false);
    }

    private void setup() {

    }

}
