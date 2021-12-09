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

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    private Context context;
    private List<HashMap> modo;
    private LayoutInflater mInflates;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser usuario;

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

    //        -----------------------------------------------------------------------------------
//        PARA ACTIVAR Y DESACTIVAR BOTON
//        -----------------------------------------------------------------------------------

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modos_layout,parent,false);
//        usuario = FirebaseAuth.getInstance().getCurrentUser();
//        db = FirebaseFirestore.getInstance();
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

                    for(int i=0; i<task.getResult().getData().size(); i++){
//                        Log.d("Color", String.valueOf(task.getResult().getData().get("Prueba1")));
                        Log.d("Modo2", modo.get(i).toString());

                        rojo.add(i, (Long) modo.get(i).get("red"));
                        verde.add(i, (Long) modo.get(i).get("green"));
                        azul.add(i, (Long) modo.get(i).get("blue"));
                    }
//                    holder.button.setText("Modo" + (modo.indexOf(modo.get(position))+1));
                    holder.text.setText("Modo" + (modo.indexOf(modo.get(position))+1));
                    holder.button.setBackgroundColor(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
//                    holder.button.setBackgroundResource(R.drawable.bordes_redondos_botton);
//                    holder.button.setColorFilter(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
                    Log.d("RedArray", rojo.toString());

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

}
