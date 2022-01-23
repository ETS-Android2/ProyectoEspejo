package com.example.appespejo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
//import java.awt.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import static com.example.escripn.mqtt.Mqtt.*;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> modo;
    private LayoutInflater mInflates;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser usuario;
    private static MqttClient client;
//    private static RecyclerViewClickListener itemListener;

    public class ViewHolder extends RecyclerView.ViewHolder{
//        Button button;
        ImageView button;
        TextView text;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
//            button = itemView.findViewById(R.id.button13);
            button = itemView.findViewById(R.id.imageView9);
            text = itemView.findViewById(R.id.textView5);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            usuario = mAuth.getCurrentUser();
        }

        void bindData(final List<NuevoColor> item ){
//            db.collection("Users").document(mAuth)
//            button.setText("Modo");
        }

    }

    public ColorListAdapter(Context context, List<HashMap> color) {
        this.context = context;
        this.modo = color;
    }

    @Override
    public int getItemCount() {
        return modo.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modos_layout,parent,false);
//        usuario = FirebaseAuth.getInstance().getCurrentUser();
//        db = FirebaseFirestore.getInstance();
//        conectarMqtt();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position){
//        holder.tarea.setText(modo.get(position).getTarea());

        db.collection("Luces")
                .document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){

                    List<Long> rojo = new ArrayList<>();
                    List<Long> verde = new ArrayList<>();
                    List<Long> azul = new ArrayList<>();
                    List<Object> intesidad = new ArrayList<>();

                    for(int i=0; i<task.getResult().getData().size(); i++){
//                        Log.d("Color", String.valueOf(task.getResult().getData().get("Prueba1")));

                        rojo.add(i, (Long) modo.get(i).get("red"));
                        verde.add(i, (Long) modo.get(i).get("green"));
                        azul.add(i, (Long) modo.get(i).get("blue"));
                        intesidad.add(i, modo.get(i).get("intensidad"));

                    }
//                    holder.button.setText("Modo" + (modo.indexOf(modo.get(position))+1));
                    holder.text.setText("Modo" + (modo.indexOf(modo.get(position))+1));
//                    holder.button.setColorFilter(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));

//                    holder.button.setOutlineAmbientShadowColor(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
                    holder.button.setBackgroundColor(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
//                    holder.button.setBackgroundResource(R.drawable.bordes_redondos_botton);
//                    holder.button.setColorFilter(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));

                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            holder.text.getText();
                            rojo.add(position, (Long) modo.get(position).get("red"));
                            verde.add(position, (Long) modo.get(position).get("green"));
                            azul.add(position, (Long) modo.get(position).get("blue"));
                            intesidad.add(position, modo.get(position).get("intensidad"));

//                             Se puede enviar en 4 topicos diferentes como: red,verde, azul y intensidad
//                            publicarMqtt("modo","Rojo " + rojo.get(position).toString() + " verde " + verde.get(position).toString()+ " azul " + azul.get(position).toString() + " intensidad " + intesidad.get(position).toString());
                            publicarMqtt("modo/rojo",rojo.get(position).toString());
                            publicarMqtt("modo/verde",verde.get(position).toString());
                            publicarMqtt("modo/azul",azul.get(position).toString());
                            publicarMqtt("modo/intensidad",intesidad.get(position).toString());
                            Log.d("ModosText", "Rojo" + rojo.get(position).toString() + " verde" + verde.get(position).toString()+ " azul" + azul.get(position).toString() + " intensidad " + intesidad.get(position).toString());
                        }
                    });
                }
            }
        });
    }

    public void setItems(List<HashMap> items){
        modo = items;
    }

    public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
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

//    @Override public void connectionLost(Throwable cause) {
//        Log.d("MQTT", "Conexión perdida");
//    }
//    @Override public void deliveryComplete(IMqttDeliveryToken token) {
//        Log.d("MQTT", "Entrega completa");
//    }
//    @Override public void messageArrived(String topic, MqttMessage message)
//            throws Exception {
//        String payload = new String(message.getPayload());
//        Log.d("MQTT", "Recibiendo: " + topic + "->" + payload);
//    }

}
