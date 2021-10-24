package com.example.appespejo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class Tab4 extends Fragment {

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    public Tab4(){
        // require a empty public constructor
    }

    private Activity view;
    public TextView perfilNombre = view.findViewById(R.id.perfilNombre);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.tab1, container, false);



        return inflater.inflate(R.layout.tab4, container, false);
    }

}
