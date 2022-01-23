package com.example.appespejo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

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
import java.util.Map;

public class TareasListAdaptador extends RecyclerView.Adapter<TareasListAdaptador.ViewHolder> {

    private Context context;
    private List<String> tareas;
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
            tarea.setText(item.getTarea());
        }
    }

    public TareasListAdaptador(Context context, List<String> tareas) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position){

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        List<String> tareaa = new ArrayList<>();
        if(tareas!=null){
            holder.tarea.setText((String) tareas.get(position));
        }

        for(int i=0; i<tareas.size(); i++){
            tareaa.add(i, tareas.get(i));
        }

        holder.tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> updatess = new HashMap<>();
                List<String> updates = new ArrayList<>();

                tareaa.remove(position);

                for(int i=0; i<tareaa.size(); i++){
                    updatess.put("Tarea" + i, tareaa.get(i));
                    updates.add(tareaa.get(i));
                }

                db.collection("Tareas")
                        .document(mAuth.getUid()).set(updatess);

                Log.d("Tarea", "Pinchado " + updates );
                Log.d("Tarea", "Pinchado " + updatess );

                context.startActivity(new Intent(context, context.getClass()));
                Toast.makeText(context, "Tu tarea ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setItems(List<String> items){tareas = items;}
}
