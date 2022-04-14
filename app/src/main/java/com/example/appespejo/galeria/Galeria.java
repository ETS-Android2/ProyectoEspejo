package com.example.appespejo.galeria;

import static com.example.escripn.mqtt.Mqtt.TAG;
import static com.example.escripn.mqtt.Mqtt.broker;
import static com.example.escripn.mqtt.Mqtt.clientId;
import static com.example.escripn.mqtt.Mqtt.qos;
import static com.example.escripn.mqtt.Mqtt.topicRoot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appespejo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Galeria extends Fragment implements MqttCallback  {

    StorageReference storageRef;
    AdaptadorImagenes adaptador;
    RecyclerView recyclerView;
    Button make_foto;
    ImageView prueba;
    FirebaseFirestore db;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Context context;
    List<Uri> fotos = new ArrayList<>();
    private static MqttClient client;
    private static final int GALLERY_INTENT = 1;
    String idUserMqtt = mAuth.getCurrentUser().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3, container, false);

        conectarMqtt();
        suscribirMqtt("rfid", this);

        make_foto = v.findViewById(R.id.make_foto);
        make_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                View bottomSheetView = LayoutInflater.from(getContext())
                        .inflate(R.layout.new_foto,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialog.show();

                Button si, cambiar_usu;
                TextView usu_text;

                si = bottomSheetView.findViewById(R.id.usu_si);
                cambiar_usu = bottomSheetView.findViewById(R.id.usu_change);
                usu_text = bottomSheetView.findViewById(R.id.usu_text);

                db.collection("Users").document(idUserMqtt).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        usu_text.setText("Hacer la foto para " + task.getResult().getString("Nombre") + " " + task.getResult().getString("Apellido") + "?");
                    }
                });


                si.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Ultimo mqtt
//                        Hacer la funcion de mqtt para los dos e ir llamandola
                        Log.d("SiUser", idUserMqtt);
//
                    }
                });

                cambiar_usu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bottomSheetDialog.cancel();
                        Dialog dialog = new Dialog(getContext());
                        View dialogView = LayoutInflater.from(getContext())
                                .inflate(R.layout.rfid_touch,null);
                        dialog.setContentView(dialogView);

                        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.show();

                        ImageView close;
                        close = dialog.findViewById(R.id.close_dialog);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

//                        Al recibir mqtt mostrar algun mensaje con el nmbredel usuario
//                        db....document(mqttMensaje).get -> nombre

                    }
                });
            }
        });

        // Create a Cloud Storage reference from the app

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("prueba/");
        Map<String, Object> fotoMap = new HashMap<>();
        List<String> fotosbd = new ArrayList<>();
        int varAux = 0;

            Log.d("Foto", "Asincronia antes" );
            pathReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    Log.d("Foto", "listResult" + listResult.toString());

                    Log.d("Foto", "Antes de sleep");
                    for (int i=0; i<listResult.getItems().size(); i++){
                        int finalAux = i;
                        listResult.getItems().get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                fotoMap.put("Foto" + finalAux, uri.toString());
                                fotosbd.add(uri.toString());

                                if(fotosbd.size() == listResult.getItems().size() && fotoMap.size() == listResult.getItems().size()){

                                    Log.d("Foto", "Ha entrado en un if");
                                    Log.d("Foto", "la fotomap " + fotoMap.toString() );

                                    db.collection("Fotos")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .set(fotoMap);

                                    if(fotosbd.size() != 0){
                                        recyclerView = v.findViewById(R.id.recyclerFotos);
//        La captura de como rellenarlo completo esta en es escritorio.
                                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                                        recyclerView.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
                                        adaptador = new AdaptadorImagenes(getContext(), fotosbd);
                                        recyclerView.setAdapter(adaptador);
                                    } else{
                                        TextView galeryNull = v.findViewById(R.id.galeryNull);
                                        galeryNull.setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        });
                    }
                }
            });

        Log.d("Foto", "Size list fotos " + fotos.size());

        aux(v);

        return v;
    }

    public void aux(View v){

    }

    @Override
    public void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_INTENT) {
            if (requestCode == 1234) {
                Log.d("foto", "onAtivityResult");

                Uri uri = data.getData();
            }
        }
    }


    @Override public void onStart() {
        super.onStart();
//        adaptador.startListening();
    }
    @Override public void onStop() {
        super.onStop();
//        adaptador.stopListening();
    }

    public static void conectarMqtt() {
        try {
            Log.i("MQTTAdapterGaleria", "Conectando al broker " + broker);
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
            Log.i("MQTTGaleria", "Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e("MQTTHome", "Error al suscribir.", e);
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

    @Override public void connectionLost(Throwable cause) {
        Log.d("MQTT", "Conexión perdida");
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d("MQTT", "Entrega completa");
    }

    @Override public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String payload = new String(message.getPayload());
        Log.d("MQTTGaleria", "Recibiendo: " + topic + "->" + payload);
        idUserMqtt = payload;
    }
}

