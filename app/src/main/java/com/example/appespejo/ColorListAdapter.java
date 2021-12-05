package com.example.appespejo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    private Context context;
    private List<ColorArray> modo;
    private LayoutInflater mInflates;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser usuario;

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button button;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            button = itemView.findViewById(R.id.button13);
        }

        void bindData(final ColorArray item ){
//            db.collection("Users").document(mAuth)
            button.setText((CharSequence) item.getColor());
        }


    }

    public ColorListAdapter(Context context, List<ColorArray> color) {
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
    public void onBindViewHolder( @NonNull ViewHolder holder,  int position){
//        holder.tarea.setText(modo.get(position).getTarea());
        holder.button.setText("Modo");
    }

    public void setItems(List<ColorArray> items){
        modo = items;}

}
