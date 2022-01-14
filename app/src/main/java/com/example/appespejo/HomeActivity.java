package com.example.appespejo;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blautic.pikkuAcademyLib.PikkuAcademy;
import com.blautic.pikkuAcademyLib.ScanInfo;
import com.blautic.pikkuAcademyLib.ble.gatt.ConnectionState;
import com.blautic.pikkuAcademyLib.callback.ConnectionCallback;
import com.blautic.pikkuAcademyLib.callback.ScanCallback;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class HomeActivity extends AppCompatActivity {

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
     PikkuAcademy pikku;
     ImageView apagarEncender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context = this;

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View homeFragment = inflater.inflate(R.layout.fragment_home, null);

        View tab1 = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.tab1,null);

        seekBar = tab1.findViewById(R.id.seekBar);
        apagarEncender = tab1.findViewById(R.id.apagarEncender);
        ints = seekBar.getProgress();

        pikku = PikkuAcademy.getInstance(this);
        pikku.enableLog();

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

        conectarMqtt();
        connect();
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
    }

    private void checkBlePermissions() {
        if (!pikku.isBluetoothOn()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Pemision")
                    .setNegativeButton("Cancel",
                            (dialog, which) -> finish())
                    .setPositiveButton(android.R.string.ok,
                            (dialog, which) -> {
                                pikku.enableBluetooth(this, 1002);
                            })

                    .setCancelable(false)
                    .show();

            return;
        }

        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted(permission);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1003);
        }

    }

    public void clickApagar(View view){
        publicarMqtt("modo/apagar", "Apagar");
        Log.d("Encender",  "Holaa");
    }

    private void onPermissionGranted(String permission) {
        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pikku.checkGPSIsOpen(this)) {
                new AlertDialog.Builder(this)
                        .setMessage("GPS enable")
                        .setNegativeButton(android.R.string.cancel,
                                (dialog, which) -> finish())
                        .setPositiveButton("GPS enable",
                                (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, 1002);
                                })
                        .setCancelable(false)
                        .show();
            }
        }
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
            Log.i("MQTT", "Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e("MQTT", "Error al suscribir.", e);
        }
    }

//    @Override public void connectionLost(Throwable cause) {
//        Log.d("MQTTTab1", "Conexión perdida");
//    }
//    @Override public void deliveryComplete(IMqttDeliveryToken token) {
//        Log.d("MQTT", "Entrega completa");
//    }
//    @Override public void messageArrived(String topic, MqttMessage message)
//            throws Exception {
//        String payload = new String(message.getPayload());
//        tempaHome.setText(payload + "°C");
//        Log.d("MQTT", "Recibiendo: " + topic + "->" + payload);
//    }

    private void checkIsConnected() {
        if (pikku.isConnected()) {
//            updateUI(true);
            readValues();
        }
    }

    private void connect() {

//        if (pikku.isConnected()) {
//            pikku.disconnect();
//            return;
//        }

        checkBlePermissions();

        if (pikku.getAddressDevice().isEmpty()) {

        } else {
            pikku.connect(state -> {
                switch (state) {
                    case CONNECTED: {
                        readValues();
                        break;
                    }
                    case DISCONNECTED:
                    case FAILED: {
                        break;
                    }
                }
            });

        }
    }

    private void readValues() {
        pikku.readButtons((nButton, pressedButton, durationMilliseconds) -> {
            String durationSeconds = String.format("%.1f''", durationMilliseconds / 1000.0);
            //Button 1 or 2
            if (nButton == 1) {
                Log.d("Pikku", "Boton1");
                WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                lp.screenBrightness =0.00001f;// i needed to dim the display
                this.getWindow().setAttributes(lp);

            } else {
                Log.d("Pikku", "Boton2");
                WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                lp.screenBrightness =1;// i needed to dim the display
                this.getWindow().setAttributes(lp);
            }
        });
    }

    public void verificado(View view){
        if(usuario.isEmailVerified()){

        }
    }

    public void onClickScan(View view) {
        pikku.scan(true, new ScanCallback() {
            @Override
            public void onScan(ScanInfo scanInfo) {
                pikku.saveDevice(scanInfo);
// guardar dispositivo para futuras conexiones
                Log.d("Pikku", scanInfo.toString());
            }
        });
    }

    public void onClickConnect(View view) {
        Log.d("Pikku", "Conectando...");
        pikku.connect(new ConnectionCallback() {
            @Override
            public void onConnect(ConnectionState state) {
                if (state == ConnectionState.CONNECTED) {
                    Log.d("Pikku", "Conectado: " + pikku.getAddressDevice());
                }
            }
        });
    }

    public void onClickDesconnect(View view){
        pikku.disconnect();
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
