package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tab5 extends Fragment {

//    private TextView username;
      FirebaseUser usuario;
      Button logout;
      FirebaseAuth mAuth;
      View HomeActivity;
//    String hello = "";


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
        final View view=inflater.inflate(R.layout.tab1, container, false);

        mAuth = FirebaseAuth.getInstance();
//        logout = (Button) view.findViewById(R.id.logOut);

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logOut();
//            }
//        });

        return inflater.inflate(R.layout.tab5, container, false);
    }

    private void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
    }


}
