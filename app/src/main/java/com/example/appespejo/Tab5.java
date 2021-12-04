package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Tab5 extends Fragment {


    Button preferences;
    Button acercade;
    Button informacion;
    Button seguridad;
    Button ayuda, perfil;

    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }*/
    public Tab5() {

        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab5, container, false);

        preferences = v.findViewById(R.id.preferencias);
        acercade = v.findViewById(R.id.acercade);
        informacion = v.findViewById(R.id.informacion);
        seguridad = v.findViewById(R.id.seguridad);
        ayuda = v.findViewById(R.id.ayuda);
        perfil = v.findViewById(R.id.perfil_ajustes);

        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirPreferencias(v);
            }
        });

        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAyuda(v);
            }
        });

        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAcercaDe(v);
            }
        });

        informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInformación(v);
            }
        });

        seguridad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirSeguridad(v);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), Tab4.class));
            }
        });

        return v;

    }

    public void abrirAyuda(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), ayuda.class);
        startActivity(i);
    }

    public void abrirPerfil(View view){
        startActivity(new Intent(getActivity().getApplicationContext(), Tab4.class));
    }


    public void AbrirPreferencias(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), Preferencias.class);
        startActivity(i);
    }

    public void abrirSeguridad(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), seguridad.class);
        startActivity(i);
    }

    public void abrirInformación(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), Informacion.class);
        startActivity(i);
    }

    public void lanzarAcercaDe(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), AcercaDeActivity.class);
        startActivity(i);
    }

}
