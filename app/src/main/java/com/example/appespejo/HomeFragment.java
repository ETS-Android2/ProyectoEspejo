package com.example.appespejo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blautic.pikkuAcademyLib.PikkuAcademy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.escripn.mqtt.Mqtt.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class HomeFragment extends Fragment implements MqttCallback{

    List<TareasList> elements;
    RecyclerView recyclerView;
    TareasListAdaptador adaptador;
    FirebaseAuth mAuth;
    FirebaseUser usuario;
    FirebaseFirestore db;
    TextView welcome, fecha, grados, location, cancion,artista , intensidad, tempaHome, songTime;
    EditText nuevaTarea;
    ImageView iconWeather,album;
    SeekBar songDuration;
    Button pause, back, next, play,spotify;
    String name,mAccessToken, intensidadMqtt, temperaturaMqtt;
    Switch switchLuz;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    private PikkuAcademy pikku;
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=Gandia&units=metric&appid=e96051f26738be95560f9d1a8d60feb6";
    private static final String CLIENT_ID = "79c5d539ac3e46d199ef6b06f3530d5c";
    private static final String REDIRECT_URI = "SpotifyTestApp://authenticationResponse";
    private SpotifyAppRemote mSpotifyAppRemote;
    final int REQUEST_CODE = 1337;
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    private static MqttClient client;
    ImageView apagarEncenderr;
    Thread updateseekbar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public HomeFragment() {

        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        View tab1 = inflater.inflate(R.layout.tab1, container,false);

//        apagarEncenderr = tabb1.findViewById(R.id.apagarEncender);
//        apagarEncenderr.getTag().toString();
//        Log.d("ApagarOn", apagarEncenderr.getTag().toString());

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        welcome = v.findViewById(R.id.welcome);
        fecha = v.findViewById(R.id.fecha);
        grados = v.findViewById(R.id.tiempoGrados);
        location = v.findViewById(R.id.localizacion);
        iconWeather = v.findViewById(R.id.iconWeather);
        SeekBar seekBar = tab1.findViewById(R.id.seekBar);
        intensidad = v.findViewById(R.id.intensidadLucesHome);
        intensidadMqtt = sharedPref.getString("intensidadMqtt", "60");
        intensidad.setText("Intensidad " + intensidadMqtt + "%");
        tempaHome = v.findViewById(R.id.tempaHome);
        tempaHome.setText("Temperatura " + sharedPref.getString("tempaMqtt", "18") + "ºC");
        songTime = v.findViewById(R.id.songTime);
        songDuration = v.findViewById(R.id.songDuration);
        switchLuz = v.findViewById(R.id.switchLuces);

        album = v.findViewById(R.id.homeSpotyImage);
        pause = v.findViewById(R.id.spotyPause);
        back = v.findViewById(R.id.homeSpotyPrevious);
        next = v.findViewById(R.id.spotyNext);
        play = v.findViewById(R.id.spotyPlay);
        play.setVisibility(View.GONE);
//        spotify = v.findViewById(R.id.spotify);
        cancion = v.findViewById(R.id.homeSpotyCancion);
        artista = v.findViewById(R.id.spotyHomeArtista);
        nuevaTarea = v.findViewById(R.id.nuevaTarea);
        pikku = PikkuAcademy.getInstance(getContext());
        pikku.enableLog();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        conectarMqtt();
        suscribirMqtt("tempa", this);
        suscribirMqtt("color/intensidad", this);

        switchLuz.setChecked(sharedPref.getBoolean("switchLuz", true));

        if(switchLuz.isChecked()){
            intensidad.setText("Intensidad " + intensidadMqtt + "%");
        } else if(!switchLuz.isChecked()){
            intensidad.setText("Intensidad 0%");
        }

        switchLuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchLuz.isChecked()){
//                    Sera encendida
                    Log.d("Switch", "true");
                    editor.putBoolean("switchLuz", switchLuz.isChecked());
                    editor.apply();
                    publicarMqtt("status/encender","Encender");
                    intensidad.setText("Intensidad " + intensidadMqtt + "%");

                } else if(!switchLuz.isChecked()){
//                    Apagada
                    Log.d("Switch", "false");
                    editor.putBoolean("switchLuz", switchLuz.isChecked());
                    editor.apply();
                    publicarMqtt("status/apagar","Apagar");
                    intensidad.setText("Intensidad 0%");
                }
            }
        });



        // Request code will be used to verify if result comes from the login activity.
        // Can be set to any integer.

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        builder.setShowDialog(true);

        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);

        allClicks();

        Date d = new Date();
        CharSequence s = DateFormat.format("d MMMM yyyy ", d.getTime());
        fecha.setText(s);


//        Poner el welcome usuario arriba de la pagina
        db.collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    name = task.getResult().getString("Nombre");
                    welcome.setText("Welcome " + name);

                }
            }
        });


//        API del tiempo arriba de la pagina
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    int tempa = jsonObjectMain.getInt("temp");
                    String city = jsonResponse.getString("name");
                    String icon = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

                    String urlfoto  = "http://openweathermap.org/img/wn/" + icon + ".png";
                    Log.d("Demo" , urlfoto);

                    grados.setText(String.valueOf(tempa) + "°C");
                    location.setText(city);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        setup(v);
        return v;
    }

    public void setup(View view){

        elements = new ArrayList<>();
        List<String> leer = new ArrayList<>();

        db.collection("Tareas")
                .document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().getString("Tarea0") != null){
                        for(int i=0; i<task.getResult().getData().size(); i++){
                            leer.add(i, task.getResult().getString("Tarea"+i));
                        }

                        TextView tareas = view.findViewById(R.id.aquiTareas);
                        tareas.setVisibility(View.GONE);
                        Log.d("Leer", leer.toString());
                        Log.d("Tareas", String.valueOf(task.getResult().getData().size()));

                        recyclerView = view.findViewById(R.id.recycleTareas);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adaptador = new TareasListAdaptador(getContext(), leer);
                        recyclerView.setAdapter(adaptador);
                    } else{

                        Log.d("Tareas", "No hay tareas disponibles");
                    }
                }
            }
        });

        nuevaTarea.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    Map<String, Object> tareas = new HashMap<>();

                    TareasList taskk = new TareasList(nuevaTarea.getText().toString());

                    db.collection("Tareas")
                            .document(mAuth.getCurrentUser().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                Codigo para sacar el documento
                                    if(task.isSuccessful()){
//                    Coge el nombre de objetos, no index
//                                        Si el documento aun no existe
                                        if(task.getResult().getData() == null){
                                            Map<String, Object> fisrtTarea = new HashMap<>();
                                            fisrtTarea.put("Tarea0", nuevaTarea.getText().toString());
                                            db.collection("Tareas")
                                                    .document(mAuth.getInstance().getCurrentUser().getUid())
                                                    .set(fisrtTarea);
                                        } else {
                                            tareas.put("Tarea" + task.getResult().getData().size(), nuevaTarea.getText().toString());

                                            db.collection("Tareas")
                                                    .document(mAuth.getCurrentUser().getUid())
                                                    .update(tareas);
                                        }
                                        Toast.makeText(getContext(), "Tu nueva tarea ha sido agregada correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    startActivity(new Intent(getContext(), getContext().getClass()));
                    return true;
                }
                return false;
            }
        });
    }

    private void allClicks() {

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().pause();
                pause.setVisibility(View.INVISIBLE);
                play.setVisibility(View.VISIBLE);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().resume();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                Log.d("Demo", mSpotifyAppRemote.getPlayerApi().getPlayerState().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().skipPrevious();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().skipNext();
            }
        });

//        Para la linea con tiempo de la cancion

//        if(mSpotifyAppRemote.isConnected()){
//            mSpotifyAppRemote.getPlayerApi().subscribeToPlayerState().setEventCallback(playerState ->{
//                final Track track = playerState.track;
//                if (track != null){
//
//
//                    updateseekbar = new Thread(){
//                        @Override
//                        public void run() {
//                            long totalDuration = track.duration;
//                            long currrentpos = 0;
//
//                            while(currrentpos < totalDuration){
//                                try{
//                                    sleep(1000);
//                                    currrentpos = playerState.playbackPosition;
//                                    songDuration.setProgress((int) currrentpos);
//                                }
//                                catch(InterruptedException | IllegalStateException e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    };
//
//                    songDuration.setMax((int) track.duration);
//                    updateseekbar.start();
//                    songDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                                songDuration.setProgress(songDuration.getProgress());
////                                mSpotifyAppRemote.getPlayerApi().seekTo(songDuration.getProgress());
//                            seekBar.setProgress(seekBar.getProgress());
//                        }
//
//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) {
////                                mSpotifyAppRemote.getPlayerApi().seekTo(songDuration.getProgress());
//                        }
//
//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar) {
//                            mSpotifyAppRemote.getPlayerApi().seekTo(seekBar.getProgress());
//                            seekBar.setProgress(seekBar.getProgress());
//                        }
//                    });
//                }
//            });
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // We will start writing our code here.

        // Set the connection parameters

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getContext(), connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("Demo", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("Demo", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here

                        Log.d("Demo", "No se ha connectado con el spotify");
                    }
                });
    }

//            -----------------------------------------------------------------------------------
//        Para mostrar por la pantalla los datos de la cancion
//        -----------------------------------------------------------------------------------
    private void connected() {
        // Then we will write some more code here.
        // Play a playlist

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("Demo", track.name + " by " + track.artist.name);
//        -----------------------------------------------------------------------------------
//        Para sacar los datos necesarios de la cancion
//        -----------------------------------------------------------------------------------
                        mSpotifyAppRemote.getImagesApi()
                                .getImage(track.imageUri, Image.Dimension.MEDIUM)
                                .setResultCallback(new CallResult.ResultCallback<Bitmap>(){
                                    @Override public void onResult(Bitmap bitmap) {
                                        album.setImageBitmap(bitmap);
                                    }
                                });
//
//                        updateseekbar = new Thread(){
//                            @Override
//                            public void run() {
//                                long totalDuration = track.duration;
//                                long currrentpos = 0;
//
//                                while(currrentpos < totalDuration){
//                                    try{
//                                        sleep(1000);
//                                        currrentpos = playerState.playbackPosition;
//                                        songDuration.setProgress((int) currrentpos);
//                                    }
//                                    catch(InterruptedException | IllegalStateException e){
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        };

                        if(playerState.isPaused){
                            pause.setVisibility(View.INVISIBLE);
                            play.setVisibility(View.VISIBLE);
                        }
//                        songDuration.setMax((int) track.duration);
//                        updateseekbar.start();

//                        songDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                            @Override
//                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                                songDuration.setProgress(songDuration.getProgress());
////                                mSpotifyAppRemote.getPlayerApi().seekTo(songDuration.getProgress());
//                                seekBar.setProgress(seekBar.getProgress());
//                            }
//
//                            @Override
//                            public void onStartTrackingTouch(SeekBar seekBar) {
////                                mSpotifyAppRemote.getPlayerApi().seekTo(songDuration.getProgress());
//                            }
//
//                            @Override
//                            public void onStopTrackingTouch(SeekBar seekBar) {
//                                mSpotifyAppRemote.getPlayerApi().seekTo(seekBar.getProgress());
//                                seekBar.setProgress(seekBar.getProgress());
//                            }
//                        });

                        cancion.setText(track.name);
                        artista.setText(track.artist.name);
                        songTime.setText(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(track.duration)) + ":" + String.valueOf(TimeUnit.MILLISECONDS.toSeconds(track.duration -TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(track.duration)))));
                    }
                    else{
                        Log.d("Demo", "No ha pillado el track");
                    }
                });
    }

    public static void conectarMqtt() {
        try {
            Log.i("MQTTHome", "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic","App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);
        } catch (MqttException e) {
            Log.e("MQTTHome", "Error al conectar.", e);
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

    public static void suscribirMqtt(String topic, MqttCallback listener) {
        try {
            Log.i("MQTTHome", "Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e("MQTTHome", "Error al suscribir.", e);
        }
    }

    @Override public void connectionLost(Throwable cause) {
        Log.d("MQTTHome", "Conexión perdida");
    }
    @Override public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTTHome", "Entrega completa");
    }
    @Override public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String payload = new String(message.getPayload());

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if(topic.equals("eskrip/practica/tempa")){
                temperaturaMqtt = payload;
                tempaHome.setText("Temperatura " + temperaturaMqtt + "ºC");
                Log.d("MQTTHome", "Hola: " + topic + "->" + payload);
                editor.putString("tempaMqtt", temperaturaMqtt);
                editor.apply();
        }
        if(topic.equals("eskrip/practica/color/intensidad")){
            if(!payload.equals(null)){
                intensidadMqtt = payload;
                if(switchLuz.isChecked()){
                    intensidad.setText("Intensidad " + intensidadMqtt + "%");
                }
                Log.d("MQTTHome", "Hola: " + topic + "->" + payload);
                editor.putString("intensidadMqtt", intensidadMqtt);
                editor.apply();
            }
        }

        Log.d("MQTTHome", "Recibiendo: " + topic + "->" + payload);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tempaMqtt",temperaturaMqtt);
        outState.putString("intensidadMqtt",intensidadMqtt);
    }
}