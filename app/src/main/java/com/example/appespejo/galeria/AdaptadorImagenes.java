package com.example.appespejo.galeria;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appespejo.HomeFragment;
import com.example.appespejo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.ViewHolder> {

    //    Difiniendo variables
    private Context context;
    private List<String> fotos;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference pathReference = storageRef.child(mAuth.getCurrentUser().getUid());


    public class ViewHolder extends RecyclerView.ViewHolder{
        //        Difinimos los objetos que tenemos en el layout
        ImageView imageFotoBd;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            imageFotoBd = itemView.findViewById(R.id.imageView6);
        }

        void bindData(final Imagenes item ){
//            Aqui sacamos la foto y color de etiqueta
            Glide.with(context)
                .load(item.getFotoUrl())
                .into(imageFotoBd);
        }
    }

    public AdaptadorImagenes(Context context, List<String> fotos) {
        this.context = context;
        this.fotos = fotos;
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull ViewHolder holder,  int position){
//        Hacemos la consulta a bd para sacar el array con cada prenda
        for(int i=0; i<getItemCount(); i++){

            Glide.with(context)
                    .load(fotos.get(position))
                    .into(holder.imageFotoBd);
        }

        holder.imageFotoBd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(v.getContext());
                View bottomSheetView = LayoutInflater.from(v.getContext())
                        .inflate(R.layout.foto_delete,null);
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
                bottomSheetDialog.show();

                Button eliminar, cancel;

                eliminar = bottomSheetView.findViewById(R.id.foto_eliminar);
                cancel = bottomSheetView.findViewById(R.id.foto_cancel);

                db.collection("Aux").document("aux").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().getString("Foto") != null && task.getResult().getString("Foto").equals(fotos.get(position))){
                                    cancel.setText("Quitar");
                                }
                            }
                        });


                Map<String, Object> aux = new HashMap<>();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(cancel.getText().toString().equals("Quitar")){
                            aux.put("Foto", "");
                            db.collection("Aux").document("aux")
                                    .update(aux);
                            bottomSheetDialog.cancel();
                            Toast.makeText(v.getContext(), "Tu foto ha sido quitada", Toast.LENGTH_SHORT).show();
                        } else if(cancel.getText().toString().equals("Fijar")){
                            aux.put("Foto", fotos.get(position));
                            db.collection("Aux").document("aux")
                                    .update(aux);
                            bottomSheetDialog.cancel();
                            Toast.makeText(v.getContext(), "Tu foto esta en la pantalla", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storageRef.child(mAuth.getCurrentUser().getUid() + "/img" + position).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        bottomSheetDialog.cancel();
                                        Toast.makeText(context.getApplicationContext(), "La foto ha sido eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });

    }

    public void setItems(List<String> items){fotos = items;}
}
