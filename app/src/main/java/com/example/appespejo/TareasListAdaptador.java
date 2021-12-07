package com.example.appespejo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TareasListAdaptador extends RecyclerView.Adapter<TareasListAdaptador.ViewHolder> {

    private Context context;
    private List<TareasList> tareas;
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

    public TareasListAdaptador(Context context, List<TareasList> tareas) {
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
//        usuario = FirebaseAuth.getInstance().getCurrentUser();
//        db = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder,  int position){
        holder.tarea.setText(tareas.get(position).getTarea());
    }

    public void setItems(List<TareasList> items){tareas = items;}

}
