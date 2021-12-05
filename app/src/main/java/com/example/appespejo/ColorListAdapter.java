package com.example.appespejo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorEnvelope;

import java.util.List;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    private Context context;
    private List<Object> modo;
    private LayoutInflater mInflates;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser usuario;

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button button;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            button = itemView.findViewById(R.id.button13);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            usuario = mAuth.getCurrentUser();
        }

        void bindData(final List<NuevoColor> item ){
//            db.collection("Users").document(mAuth)
            button.setText("Modo");
        }

    }

    public ColorListAdapter(Context context, List<Object> color) {
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
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isComplete()){

//                    for(int i=0; i<task.getResult().getData().size(); i++){
                        Log.d("Color", String.valueOf(task.getResult().getData().get("Prueba1")));
//                    }

//                    for(int i=0; i<task.getResult().getData().size(); i++){
//                        modo.add(task.getResult().getData().get(i));
//                    }
//
                    modo.get(position);
                    holder.button.setText("Modo" + (modo.indexOf(modo.get(position))+1));
                    holder.button.setBackgroundResource(R.drawable.bordes_redondos_botton);
                }
            }
        });

    }

    public void setItems(List<Object> items){
        modo = items;
    }

}
