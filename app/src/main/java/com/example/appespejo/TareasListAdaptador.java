package com.example.appespejo;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TareasListAdaptador extends RecyclerView.Adapter<TareasListAdaptador.ViewHolder> {

    private Context context;
    private List<HashMap> tareas;
    private LayoutInflater mInflates;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser usuario;
    boolean isActivatedButton;


    public class ViewHolder extends RecyclerView.ViewHolder{
         RadioButton tarea;
         EditText linea;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tarea = itemView.findViewById(R.id.tareas);
//            linea = itemView.findViewById(R.id.editTextTextMultiLine);

            isActivatedButton = tarea.isChecked(); //DESACTIVADO
            tarea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//        ACTIVADO
                    if(isActivatedButton){tarea.setChecked(false);}
                    isActivatedButton = tarea.isChecked();
                }
            });
        }

        void bindData(final TareasList item ){
//            db.collection("Users").document(mAuth)
            tarea.setText(item.getTarea());
        }


    }

    public TareasListAdaptador(Context context, List<HashMap> tareas) {
        this.context = context;
        this.tareas = tareas;
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    //        -----------------------------------------------------------------------------------
//        PARA ACTIVAR Y DESACTIVAR BOTON
//        -----------------------------------------------------------------------------------


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tareas_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder,  int position){

        List<String> tareaa = new ArrayList<>();
//        holder.tarea.setText(tareas.get(position).getTarea());

        for(int i=0; i<tareas.size(); i++){
            tareaa.add(i, (String) tareas.get(i).get("tarea"));
        }
        holder.tarea.setText(tareaa.get(position).toString());



//        db.collection("Tareas")
//                .document(mAuth.getCurrentUser().getUid())
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if(task.isComplete()){
//
//                    List<String> tareaa = new ArrayList<>();
////                    List<Long> verde = new ArrayList<>();
////                    List<Long> azul = new ArrayList<>();
//
//                    for(int i=0; i<task.getResult().getData().size(); i++){
////                        Log.d("Color", String.valueOf(task.getResult().getData().get("Prueba1")));
////                        Log.d("Modo2", modo.get(i).toString());
//
//                        tareaa.add(i, (String) tareas.get(i).get("tarea"));
////                        verde.add(i, (Long) modo.get(i).get("green"));
////                        azul.add(i, (Long) modo.get(i).get("blue"));
//                    }
////                    holder.button.setText("Modo" + (modo.indexOf(modo.get(position))+1));
////                    holder.text.setText("Modo" + (modo.indexOf(modo.get(position))+1));
////                    holder.button.setBackgroundColor(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
////                    holder.button.setBackgroundResource(R.drawable.bordes_redondos_botton);
////                    holder.button.setColorFilter(getIntFromColor(Math.toIntExact(rojo.get(position)), Math.toIntExact(verde.get(position)), Math.toIntExact(azul.get(position))));
//                    holder.tarea.setText(tareaa.get(position).toString());
////                    Log.d("RedArray", rojo.toString());
//
//                }
//            }


    }

    public void setItems(List<HashMap> items){tareas = items;}

}
