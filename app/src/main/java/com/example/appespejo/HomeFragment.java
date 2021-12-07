package com.example.appespejo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    List<TareasList> elements;
    RecyclerView recyclerView;
    TareasListAdaptador adaptador;
    FirebaseAuth mAuth;
    FirebaseUser usuario;
    FirebaseFirestore db;
    TextView welcome, fecha, grados, location, cancion,artista , intensidad;
    ImageView iconWeather,album;
    Button pause, back, next, play;
    String name,mAccessToken;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=Gandia&units=metric&appid=e96051f26738be95560f9d1a8d60feb6";
    private static final String CLIENT_ID = "79c5d539ac3e46d199ef6b06f3530d5c";
    private static final String REDIRECT_URI = "SpotifyTestApp://authenticationResponse";
    private SpotifyAppRemote mSpotifyAppRemote;
    final int REQUEST_CODE = 1337;
    Button spotify;
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;


    /* @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     }*/
    public HomeFragment() {

        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        View tab1 = inflater.inflate(R.layout.tab1, container,false);

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
        intensidad.setText("Intencidad " + seekBar.getProgress() + "%");

        album = v.findViewById(R.id.homeSpotyImage);
        pause = v.findViewById(R.id.spotyPause);
        back = v.findViewById(R.id.homeSpotyPrevious);
        next = v.findViewById(R.id.spotyNext);
        play = v.findViewById(R.id.spotyPlay);
        play.setVisibility(View.GONE);
//        spotify = v.findViewById(R.id.spotify);
        cancion = v.findViewById(R.id.homeSpotyCancion);
        artista = v.findViewById(R.id.spotyHomeArtista);


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
//                    new FetchImage(url).start();
                    Picasso.get()
                            .load(urlfoto)
                            .into(iconWeather);
//                    Glide.with(getContext())
//                            .load(url)
//                            .into(iconWeather);

//                    InputStream inputStream = null;
//                    inputStream = new URL(url).openStream();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    iconWeather.setImageBitmap(bitmap);
                    
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
        elements.add(new TareasList("Hacer ejercicios"));
        elements.add(new TareasList("Acabar proyecto"));
        elements.add(new TareasList("Tarea 3"));
        elements.add(new TareasList("Tarea 4"));
        elements.add(new TareasList("Tarea 5"));
        elements.add(new TareasList("Tarea 6"));
        elements.add(new TareasList("Tarea 7"));

        Map<String, Object> tareas = new HashMap<>();

        for(int i=0; i<elements.size(); i++){
            tareas.put("Tarea" + i, elements.get(i));
        }

        db.collection("Tareas")
                .document(mAuth.getCurrentUser().getUid())
                .set(tareas);

        recyclerView = view.findViewById(R.id.recycleTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new TareasListAdaptador(getContext(), elements);

        recyclerView.setAdapter(adaptador);
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



    //        -----------------------------------------------------------------------------------
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

                        cancion.setText(track.name);
                        artista.setText(track.artist.name);
//                        if(mSpotifyAppRemote.getPlayerApi().getPlayerState().equals(mSpotifyAppRemote.getPlayerApi().pause())){
//
//                        }
                    }
                    else{
                        Log.d("Demo", "No ha pillado el track");
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//        mSpotifyAppRemote.getPlayerApi().pause();
        Log.d("Demo", "onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//        mSpotifyAppRemote.getPlayerApi().pause();
        Log.d("Demo", "onDestroy");

    }


}