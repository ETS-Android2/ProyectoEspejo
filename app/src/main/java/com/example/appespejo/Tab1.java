package com.example.appespejo;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
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
    Context context;
    int aux=0;
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

        recyclerView = v.findViewById(R.id.recyclerModos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new ColorListAdapter(getContext(), elements);

        recyclerView.setAdapter(adaptador);

        return v;
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
//        int defaultValue = getResources().getInteger(seekBar.getProgress());
//        seekBar.setProgress(defaultValue);
    }

    @SuppressLint("SetTextI18n")
    private void setColor(ColorEnvelope envelope) {


        int i = Integer.decode(String.valueOf(envelope.getColor()));
        int colorR = Color.red(i);
        int colorG = Color.green(i);
        int colorB = Color.blue(i);

        NuevoColor colour = new NuevoColor(colorR, colorG, colorB, seekBar.getProgress());

        db.collection("Luces").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot document = task.getResult();

                    String doc = document.getData().entrySet().toArray()[0].toString().split("Modo=")[1];
//                    cada color
//                    En split(", \\{")[0]. ponemos un i en vez de 1 que es igual a tamanyo del array(!)
                    String[] unpackedDocRed = doc.split(", \\{")[0].split(",")[0].split("=");
                    String unpackedDocGreen = doc.split(", \\{")[0].split(",")[1].split("=")[1];
                    String unpackedDocBlue = doc.split(", \\{")[0].split(",")[2].split("=")[1];
                    String unpackedDocInts = doc.split(", \\{")[0].split(",")[3].split("=")[1].split("\\}")[0];

                    Log.d("Object", unpackedDocRed[1]);
                    Log.d("Object", unpackedDocGreen);
                    Log.d("Object", unpackedDocBlue);
                    Log.d("Object", unpackedDocInts);

//                    Un objeto
//                    doc.split("\\[")[1].split("\\]")[0].split("\\{")[1].split("\\}")[0].split(",");

//                    Log.d("Doc", doc.split("\\[")[1].split("\\]")[0].split("\\{")[1].split("\\}")[0].split(","));

                }
            }
        });

        Map<String, Object> luces = new HashMap<>();

        for(int j = 0; j< luces.size()+1; j++){
            luces.put("Modo", elements);
        }

        newLuces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                elements.add(colour);
//                Empezamos un for?
                db.collection("Luces")
                        .document(mAuth.getCurrentUser().getUid())
                        .set(luces);
            }
        });

        Log.d("Demo", "Rojo " + colorR + "Green " + colorG + "Blue " + colorB);
    }

}