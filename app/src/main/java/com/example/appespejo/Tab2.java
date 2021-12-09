package com.example.appespejo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Image;
import com.spotify.protocol.types.Track;

//import com.spotify.sdk.android.auth;


import androidx.fragment.app.Fragment;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

public class Tab2 extends Fragment {
    
    private static final String CLIENT_ID = "79c5d539ac3e46d199ef6b06f3530d5c";
    private static final String REDIRECT_URI = "SpotifyTestApp://authenticationResponse";
    private SpotifyAppRemote mSpotifyAppRemote;
    final int REQUEST_CODE = 1337;
    ImageView album, pause, back, next, play;
    Button spotify;
    TextView cancion,artista;
    String mAccessToken;
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;


    public Tab2(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab2, container, false);

        album = v.findViewById(R.id.album);
        pause = v.findViewById(R.id.pausePlay);
        back = v.findViewById(R.id.back);
        next = v.findViewById(R.id.next);
        play = v.findViewById(R.id.play);
        play.setVisibility(View.GONE);
        spotify = v.findViewById(R.id.spotify);
        cancion = v.findViewById(R.id.cancion);
        artista = v.findViewById(R.id.artista);

//    Aqui desclararemos todas las funciones onClick
        allClicks();

        // Request code will be used to verify if result comes from the login activity.
        // Can be set to any integer.

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        builder.setShowDialog(true);

        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);

        return v;
    }



//        -----------------------------------------------------------------------------------
//        Todas las funciones onClick
//        -----------------------------------------------------------------------------------
    private void allClicks() {

        Tab2 cx = this;

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().pause();
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().resume();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
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

        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String apppackage = "https://www.spotify.com/31sh5hscok32wewoudxkmivktpye4";
                String apppackage = "https://open.spotify.com/";

                try {
                    Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(apppackage);
                    Log.d("Demo", "Esta cargando");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apppackage)));
                }
                catch (Exception  e) {
                    Log.d("Demo", "Sorry, Spotify Apps Not Found");
                    Toast.makeText(getContext(), "Sorry, Spotify Apps Not Found", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void spotify(View view){
//        Intent i2 = getPackageManager().getLaunchIntentForPackage("com.spotify.android");
//        startActivity(i2);
        String apppackage = "com.spotify.android";
        Tab2 cx = this;
        try {
            Intent i = getActivity().getPackageManager().getLaunchIntentForPackage(apppackage);
            Log.d("Demo", "Esta cargando");
            cx.startActivity(i);
        } catch (Exception  e) {
            Log.d("Demo", "Sorry, Spotify Apps Not Found");
            Toast.makeText(getContext(), "Sorry, Spotify Apps Not Found", Toast.LENGTH_LONG).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
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
        mSpotifyAppRemote.getPlayerApi().pause();
        Log.d("Demo", "onStop");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        mSpotifyAppRemote.getPlayerApi().pause();
        Log.d("Demo", "onDestroy");

    }
}
