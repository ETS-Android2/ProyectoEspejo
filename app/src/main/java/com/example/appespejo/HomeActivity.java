package com.example.appespejo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;
import static com.example.escripn.mqtt.Mqtt.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class HomeActivity extends AppCompatActivity implements MqttCallback {

     private BottomNavigationView bottomNavigationView;
     FirebaseAuth mAuth;
     FirebaseUser usuario;
     ImageView fotoUsuario;
     FirebaseFirestore db;
     FirebaseStorage storage;
     TextView perfilNombre,perfilApellido,perfilEmail,usuarioNombre,tempaHome;
     Animation animacion2;
     Button spotify,verificado;
     Dialog dialog;
     SeekBar seekBar;
     private static MqttClient client;
     int ints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context = this;

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View homeFragment = inflater.inflate(R.layout.fragment_home, null);

        View tab1 = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.tab1,null);

//        View homeFragment = LayoutInflater.from(HomeActivity.this)
//                .inflate(R.layout.fragment_home,null);

        seekBar = tab1.findViewById(R.id.seekBar);
        ints = seekBar.getProgress();

//        tempaHome = (TextView) this().homeFragment.findViewById(R.id.tempaHome);
        Log.d("Text", tempaHome.getText().toString());
        tempaHome.setText("Hola");
        Log.d("Text", tempaHome.getText().toString());

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        fotoUsuario = findViewById(R.id.fotoUsuario);
        perfilNombre = findViewById(R.id.perfilNombre);
        perfilApellido = findViewById(R.id.perfilApellido);
        perfilEmail = findViewById(R.id.perfilCorreo);
        spotify = findViewById(R.id.spotify);
        verificado = findViewById(R.id.verificado);

//        --------------------------PARA EL ANONIMO-----------------------------
//        usuarioNombre = (TextView) findViewById(R.id.usuarioNombre);

//        if(!usuario.isEmailVerified() && !usuario.isAnonymous()){
//            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//            Toast.makeText(HomeActivity.this, "Necesitas verificar tu correo", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signOut();
//        }

//        if(usuario.isEmailVerified()){
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//            dialogSheetDialog.show();
//        } else{
//            dialogSheetDialog.cancel();
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//        }
//        -----------------------------------------------------------------------
//        -----------------------------------------------------------------------

        Dialog dialogSheetDialog = new Dialog(getApplicationContext());

        View dialogSheetView = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.correo_no_verificado,null);
        dialogSheetDialog.setContentView(dialogSheetView);
        dialogSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        conectarMqtt();
        suscribirMqtt("#", this);
    }

    public static void conectarMqtt() {
        try {
            Log.i("MQTT", "Conectando al broker " + broker);
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

    public static void suscribirMqtt(String topic, MqttCallback listener) {
        try {
            Log.i("MQTT", "Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al suscribir.", e);
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


    public void verificado(View view){
        if(usuario.isEmailVerified()){

        }
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void registrarAnonimo(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }

    private NavigationBarView.OnItemSelectedListener bottomNavMethod = new
            NavigationBarView.OnItemSelectedListener(){
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                    Fragment fragment=null;

                    switch (menuItem.getItemId()){

                        case R.id.ligth:
                            fragment=new Tab1();
                        break;

                        case R.id.music:
                            fragment=new Tab2();
                        break;

                        case R.id.photo:
                            fragment=new Tab3();
                        break;

                        case R.id.menu_home:
                            fragment=new HomeFragment();
                        break;

                        case R.id.conf:
                            fragment=new Tab5();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                    return true;
                }
            };
}
