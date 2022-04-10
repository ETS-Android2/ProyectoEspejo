package com.example.appespejo;


import static com.example.escripn.mqtt.Mqtt.TAG;
import static com.example.escripn.mqtt.Mqtt.broker;
import static com.example.escripn.mqtt.Mqtt.clientId;
import static com.example.escripn.mqtt.Mqtt.qos;
import static com.example.escripn.mqtt.Mqtt.topicRoot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class Tab1 extends Fragment implements MqttCallback{
    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }*/
    public Tab1(){
        // require a empty public constructor
    }

    SharedPreferences preferences;
    private static MqttClient client;
    RecyclerView recyclerView;
    ColorListAdapter  adaptador;
    ColorPickerView colorPickerView;
    List<NuevoColor> elements;
    ImageButton newLuces;
    ImageView apagarEncender;
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


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);
        Timber.plant(new Timber.DebugTree());

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        elements = new ArrayList<>();

        newLuces = v.findViewById(R.id.anyadirLuces);

        apagarEncender = v.findViewById(R.id.apagarEncender);
        apagarEncender.setTag((Object) sharedPref.getString("botonON", "rojo"));
        Log.d("Algos", apagarEncender.getTag().toString());
        Log.d("Algos", sharedPref.getString("botonON", "rojo"));
//        apagarEncender.setColorFilter(0xFF000000);

        intensidad = v.findViewById(R.id.intensidad);
        intensidad.setText("Intensidad: " + String.valueOf(sharedPref.getInt("seekbar", 100))+" %");

        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        seekBar.setProgress(sharedPref.getInt("seekbar", 100));
        seekBar.setMax(100);

        Log.d("Algo", String.valueOf(sharedPref.getInt("seekbar", 100)));

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //hace un llamado a la perilla cuando se arrastra
                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        intensidad.setText("Intensidad: " + String.valueOf(progress)+" %");
                        pos = seekBar.getProgress();
//                        Log.d("SeekbarPogess", String.valueOf(seekBar.getProgress()));
                        editor.putInt("seekbar", seekBar.getProgress());
                        editor.apply();
                        publicarMqtt("color/intensidad", String.valueOf(seekBar.getProgress()));
                    }

                    //hace un llamado  cuando se toca la perilla
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        pos = seekBar.getProgress();
//                        Log.d("SeekbarStart", String.valueOf(seekBar.getProgress()));
                    }

                    //hace un llamado  cuando se detiene la perilla
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekBar.setProgress(pos);
//                        Log.d("SeekbarStop", String.valueOf(seekBar.getProgress()));
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
//                            aqui va mqtt--------------------------------------------------------------
//                            publicarMqtt("color/actual", envelope.getHexCode());

                            int i = Integer.decode(String.valueOf(envelope.getColor()));
                            int colorR = Color.red(i);
                            int colorG = Color.green(i);
                            int colorB = Color.blue(i);

                            publicarMqtt("color/rojo", String.valueOf(colorR));
                            publicarMqtt("color/verde", String.valueOf(colorG));
                            publicarMqtt("color/azul", String.valueOf(colorB));

                            setColor(envelope);
                        });

        colorPickerView.setFlagView(new BubbleFlag(getContext()));

        modos(v);
        conectarMqtt();

        if (apagarEncender.getTag().equals("verde")){
            apagarEncender.setColorFilter(Color.rgb(164,24, 22));
            apagarEncender.setTag("rojo");
            editor.putString("botonON", apagarEncender.getTag().toString());
            editor.apply();
//                    Aqui apaga
            publicarMqtt("status/apagar","Apagar");
            Log.d("Encender",  "apagar");
        }
        else if (apagarEncender.getTag().equals("rojo")){
            apagarEncender.setTag("verde");
            apagarEncender.setColorFilter(Color.rgb(100,168, 103));
            editor.putString("botonON", apagarEncender.getTag().toString());
            editor.apply();
//                    Aqui enciende
            Log.d("Encender",  "encender");
            publicarMqtt("status/encender","Encender");
        }

        allclicks(v);

        return v;
    }


    public void allclicks(View view){

        apagarEncender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (apagarEncender.getTag()=="verde"){
                    apagarEncender.setColorFilter(Color.rgb(164,24, 22));
                    apagarEncender.setTag("rojo");
//                    Aqui apaga
                    publicarMqtt("status/apagar","Apagar");
                    Log.d("Encender",  "apagar");
                }
                else if (apagarEncender.getTag()=="rojo"){
                    apagarEncender.setTag("verde");
                    apagarEncender.setColorFilter(Color.rgb(100,168, 103));
//                    Aqui enciende
                    Log.d("Encender",  "encender");
                    publicarMqtt("status/encender","Encender");
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void modos(View view){

        //  Dialog para cambier el correo
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View bottomSheetView = LayoutInflater.from(this.getContext())
                .inflate(R.layout.modos_recycler,null);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));


        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                conectarMqtt();
                Log.d("Vida", "On cancel");
            }
        });


        TextView prueba = view.findViewById(R.id.textView40);
        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();

                db.collection("Luces")
                        .document(mAuth.getCurrentUser().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                Codigo para sacar el documento
                                if(task.isSuccessful()){

                                    if(task.getResult().getData() == null){

                                    } else {
                                        TextView instrucciones = bottomSheetView.findViewById(R.id.textView44);
                                        instrucciones.setVisibility(View.GONE);
                                        List<HashMap> prueba = new ArrayList<HashMap>();
//                    Coge el nombre de objetos, no index
                                        for(int i=0; i<task.getResult().getData().size(); i++) {
                                            prueba.add(i, (HashMap) task.getResult().getData().get("Prueba"+i));
                                        }
                                        Log.d("PruebaArray", String.valueOf(prueba.size()));
                                        TextView text = bottomSheetView.findViewById(R.id.textView44);

                                        if (prueba.size() != 0) {
                                            text.setVisibility(View.GONE);
                                            recyclerView = bottomSheetView.findViewById(R.id.recyclerModos);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                            adaptador = new ColorListAdapter(getContext(), prueba);
                                            recyclerView.setAdapter(adaptador);
                                        }
                                    }


                                } else{
                                    Toast.makeText(getContext(), "Ha ocurrido error al leer base de datos", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Vida", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Vida", "onPause");
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Vida", "onResume");
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
                                    if(task.getResult().getData() == null){
                                        Map<String, Object> firstModo = new HashMap<>();
                                        firstModo.put("Prueba0", colour);
                                        db.collection("Luces")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .set(firstModo);
                                    } else {
                                        lucess.put("Prueba" + task.getResult().getData().size(), colour);

                                        db.collection("Luces")
                                                .document(mAuth.getCurrentUser().getUid())
                                                .update(lucess);
                                    }

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

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Vida", "onStart");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("SeekBar", 64);
        outState.putString("TagBoton", apagarEncender.getTag().toString());
//        outState.putString("TagBoton", "verde");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!= null){
            Log.d("Guardado", "Hay algo guardado");
        } else{
            Log.d("Guardado", "Que te den");
        }
    }

    public static void conectarMqtt() {
        try {
            Log.i("MQTTAdapter", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic","App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al conectar.", e);
        }
    }

    public static void publicarMqtt(String topic, String mensageStr) {
        try {
            MqttMessage message = new MqttMessage(mensageStr.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            Log.i(TAG, "Publicando mensaje: " + topic+ "->"+mensageStr);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar." + e);
        }
    }

    public static void publicarMqttInt(String topic, String mensageStr) {
        try {
            MqttMessage message = new MqttMessage(mensageStr.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            Log.i(TAG, "Publicando mensaje: " + topic+ "->"+mensageStr);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar." + e);
        }
    }

    @Override public void connectionLost(Throwable cause) {
        Log.d("MQTT", "Conexión perdida");
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTT", "Entrega completa");
    }

    @Override public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String payload = new String(message.getPayload());
        Log.d("MQTT", "Recibiendo: " + topic + "->" + payload);
    }


}