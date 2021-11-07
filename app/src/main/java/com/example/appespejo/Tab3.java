package com.example.appespejo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Tab3 extends Fragment {

    ConstraintLayout dialogWindow;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageRef;

    public Tab3(){
        // require a empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab3, container, false);

//        -----------------------------------------------------------------------------------
//        Base de datos
//        -----------------------------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();



//        -----------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------

        dialogWindow = v.findViewById(R.id.dialogWindow);
        dialogWindow.setVisibility(View.INVISIBLE);

//        -----------------------------------------------------------------------------------
//        -----------------------------------------------------------------------------------

        if(mAuth.getCurrentUser().getEmail().equals("")){

            dialogWindow.setVisibility(View.VISIBLE);
        }

//        -----------------------------------------------------------------------------------
//      Para sacar las fotos de bd
//        -----------------------------------------------------------------------------------



        return v;
    }

//        -----------------------------------------------------------------------------------
//    Funciones
//        -----------------------------------------------------------------------------------




}
