package com.example.appespejo;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorSliderView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class Tab1 extends Fragment {
    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }*/
    public Tab1(){
        // require a empty public constructor
    }
    SharedPreferences preferences;
    RecyclerView recyclerView;
    ColorListAdapter  adaptador;
    ColorPickerView colorPickerView;
    List<NuevoColor> elements;
    ImageButton newLuces;
    FirebaseDatabase database;
    FirebaseFirestore db;
    FirebaseUser usuario;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    SeekBar seekBar;
    TextView intensidad;
    Button guardar;
    EditText nombreModo;
    Context context;
    int pos=100;
    private boolean FLAG_PALETTE = false;
    private boolean FLAG_SELECTOR = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);
        Timber.plant(new Timber.DebugTree());

//        database = FirebaseDatabase.getInstance();
//        databaseReference = database.getReference("prueba1-9bb89-default-rtdb");
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        elements = new ArrayList<>();
        preferences = getContext().getSharedPreferences("Seekbar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        intensidad = v.findViewById(R.id.intensidad);
        newLuces = v.findViewById(R.id.anyadirLuces);

        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setProgress(pos);
        seekBar.setMax(100);

        editor.apply();

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //hace un llamado a la perilla cuando se arrastra
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        intensidad.setText("Intensidad: " + String.valueOf(progress)+" %");
                        pos = seekBar.getProgress();
                        editor.putInt("Seekbar", seekBar.getProgress());
                        editor.apply();
                    }

                    //hace un llamado  cuando se toca la perilla
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        pos = seekBar.getProgress();
                        editor.putInt("Seekbar", seekBar.getProgress());
                        editor.apply();
                    }

                    //hace un llamado  cuando se detiene la perilla
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(pos);
                        editor.putInt("Seekbar", seekBar.getProgress());
                        editor.apply();
                    }
                });


        colorPickerView = v.findViewById(R.id.colorPickerView);
        BubbleFlag bubbleFlag = new BubbleFlag(getContext());
        bubbleFlag.setFlagMode(FlagMode.ALWAYS);
        colorPickerView.setFlagView(bubbleFlag);

        colorPickerView.setColorListener(
                (ColorEnvelopeListener)
                        (envelope, fromUser) -> {
                            Timber.d("color: %s", envelope.getHexCode());
                            setColor(envelope);

                        });

        colorPickerView.setFlagView(new BubbleFlag(getContext()));

//        recyclerView = v.findViewById(R.id.recyclerModos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        adaptador = new ColorListAdapter(getContext(), elements);
//
//        recyclerView.setAdapter(adaptador);

        modos(v);

        return v;
    }

    public void modos(View view){

//        HashMap<String, Object> modo = new HashMap<>();

        db.collection("Luces")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                Codigo para sacar el documento
                        if(task.isSuccessful()){
                            List<HashMap> prueba = new ArrayList<HashMap>();
//                    Coge el nombre de objetos, no index
                            for(int i=0; i<task.getResult().getData().size(); i++) {
                                prueba.add(i, (HashMap) task.getResult().getData().get("Prueba"+i));
                            }
                            Log.d("PruebaArray", prueba.toString());

                            recyclerView = view.findViewById(R.id.recyclerModos);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            adaptador = new ColorListAdapter(getContext(), prueba);
                            recyclerView.setAdapter(adaptador);

                        }
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        int defaultValue = getResources().getInteger(seekBar.getProgress());
//        seekBar.setProgress(defaultValue);
    }

    @Override
    public void onResume() {
        super.onResume();
//        modos(v);
//        int defaultValue = getResources().getInteger(seekBar.getProgress());
//        seekBar.setProgress(defaultValue);
    }

    @SuppressLint("SetTextI18n")
    private void setColor(ColorEnvelope envelope) {

        int i = Integer.decode(String.valueOf(envelope.getColor()));
        int colorR = Color.red(i);
        int colorG = Color.green(i);
        int colorB = Color.blue(i);

        Map<String, Object> lucess = new HashMap<>();

        NuevoColor colour = new NuevoColor(colorR, colorG, colorB, seekBar.getProgress());

//        Si le enviamos el nombre del color y en este nombre le ponemos un array

        newLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("Luces")
                        .document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                Codigo para sacar el documento
                                if(task.isSuccessful()){
//                    Coge el nombre de objetos, no index
                                    lucess.put("Prueba" + task.getResult().getData().size(), colour);

                                    db.collection("Luces")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .update(lucess);

                                    Toast.makeText(getContext(), "Tu nuevo modo ha sido guardado correctamente", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                startActivity(new Intent(getContext(), getContext().getClass()));


//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
//                View bottomSheetView = LayoutInflater.from(getContext())
//                        .inflate(R.layout.nuevo_color_name,null);
//                bottomSheetDialog.setContentView(bottomSheetView);
//
//                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                bottomSheetDialog.show();
//
//                guardar = bottomSheetView.findViewById(R.id.guardarCambios3);
//                nombreModo = bottomSheetView.findViewById(R.id.nombreModo);
//
//                guardar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        lucess.put(nombreModo.getText().toString(), colour);
//
//                        db.collection("Luces")
//                                .document(mAuth.getCurrentUser().getUid())
//                                .update(lucess);
//
//                        Toast.makeText(getContext(), "Tu nuevo modo ha sido guardado correctamente", Toast.LENGTH_SHORT).show();
//                        Log.d("Demo", "El nombre nuevo es: " + nombreModo.getText().toString());
//
//                        bottomSheetDialog.cancel();
////                        updateUI(mAuth.getCurrentUser());
//                    }
//                });
            }
        });

        Log.d("Demo", "Rojo " + colorR + " Green " + colorG + " Blue " + colorB);
    }

}