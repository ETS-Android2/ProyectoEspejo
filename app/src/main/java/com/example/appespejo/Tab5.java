package com.example.appespejo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blautic.pikkuAcademyLib.PikkuAcademy;
import com.blautic.pikkuAcademyLib.ScanInfo;
import com.blautic.pikkuAcademyLib.StatusDevice;
import com.blautic.pikkuAcademyLib.ble.gatt.ConnectionState;
import com.blautic.pikkuAcademyLib.callback.ConnectionCallback;
import com.blautic.pikkuAcademyLib.callback.ScanCallback;
import com.blautic.pikkuAcademyLib.callback.StatusDeviceCallback;

public class Tab5 extends Fragment {


    Button preferences;
    Button acercade;
    Button informacion;
    Button seguridad;
    Button ayuda, perfil,escanearBoton,conectarBoton;
    PikkuAcademy pikku;
    TextView textView40;

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
        escanearBoton = v.findViewById(R.id.escanearPikku);
        conectarBoton = v.findViewById(R.id.conectarPikku);
        pikku = PikkuAcademy.getInstance(getContext());
        pikku.enableLog();
        textView40 = v.findViewById(R.id.textView40);

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



        /*
        pikkuBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pikkuBoton.getText().equals(" Escanear Pikku")){
//
//                    onClickScan(v);
                    Log.d("Pikku","Pulsa el botón Pikku 1 para ser scaneado" );
//        binding.textScan.setText("Pulsa el botón Pikku 1 para ser scaneado");
                    pikku.scan(true, new ScanCallback() {
                        @Override
                        public void onScan(ScanInfo scanInfo) {
                            pikku.saveDevice(scanInfo);
// guardar dispositivo para futuras conexiones
                            Log.d("Pikku", scanInfo.toString());
                            pikkuBoton.setText(" Conectar Pikku");
                        }
                    });

                } else if(pikkuBoton.getText().equals(" Conectar Pikku")){

                    onClickConnect(v);
                    pikkuBoton.setText(" Disconectar Pikku");

                } else if(pikkuBoton.getText().equals(" Disconectar Pikku")){
                    pikku.disconnect();
                }
            }
        });*/


        escanearBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView40.setText("Pulsa el botón Pikku 1 para ser scaneado");
                pikku.scan(true, new ScanCallback() {
                    @Override
                    public void onScan(ScanInfo scanInfo) {
                        pikku.saveDevice(scanInfo);
                        // guardar dispositivo para futuras conexiones
                        Log.d("Pikku", scanInfo.toString());
                        textView40.setText("Encontrado:"+pikku.getAddressDevice());
                        conectarBoton.setEnabled(true);
                    }
                });


            }
        });

        conectarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conectarBoton.getText().equals(" Conectar Pikku")){
                textView40.setText("Conectando...");
                pikku.connect(new ConnectionCallback() {
                    @Override
                    public void onConnect(ConnectionState state) {
                        if (state == ConnectionState.CONNECTED) {
                            textView40.setText("Conectado: " + pikku.getAddressDevice());
                            escanearBoton.setEnabled(false);


                            textView40.setText("Conectado: " + pikku.getAddressDevice());
                            escanearBoton.setEnabled(false);
                            conectarBoton.setText("DESCONECTAR");
                        }
                    }
                });
            }else {
                    pikku.disconnect();
                    textView40.setText("No conectado");
                    escanearBoton.setEnabled(true);
                    textView40.setText("Desconectado");
                    conectarBoton.setText("CONECTAR");
                    conectarBoton.setEnabled(false);
                }
            }
        });



        return v;

    }

    /*
    public void onClickConnect(View view) {
//        binding.textConnect.setText("Conectando...");
        Log.d("Pikku","Conectando..." );

        pikku.connect(new ConnectionCallback() {
            @Override
        public void onConnect(ConnectionState state) {
                if (state == ConnectionState.CONNECTED) {
//            binding.textConnect.setText("Conectado: " + pikku.getAddressDevice());
//            binding.buttonScan.setEnabled(false);
            Log.d("Pikku","Conectado" );
        }
        } });
    }

    public void onClickScan(View view) {
        Log.d("Pikku","Pulsa el botón Pikku 1 para ser scaneado" );
//        binding.textScan.setText("Pulsa el botón Pikku 1 para ser scaneado");
        pikku.scan(true, new ScanCallback() {
            @Override
            public void onScan(ScanInfo scanInfo) {
                pikku.saveDevice(scanInfo);
// guardar dispositivo para futuras conexiones
                Log.d("Pikku", scanInfo.toString());
            }
        });


    }
    */



    public void abrirAyuda(View view) {
        Intent i = new Intent(getActivity().getApplicationContext(), ayuda.class);
        startActivity(i);
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
