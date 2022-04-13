package com.example.appespejo.galeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appespejo.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdaptadorImagenes extends RecyclerView.Adapter<AdaptadorImagenes.ViewHolder>{

    //    Difiniendo variables
    private Context context;
    private List<String> fotos;

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

    }

    public void setItems(List<String> items){fotos = items;}
}
