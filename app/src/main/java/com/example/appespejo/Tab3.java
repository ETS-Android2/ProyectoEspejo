package com.example.appespejo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Tab3 extends Fragment {

    private StorageReference storageRef;
    public AdaptadorImagenes adaptador;

    Button button;



    public void subirFichero(Uri fichero, String referencia) {
        StorageReference ficheroRef = storageRef.child(referencia);
        ficheroRef.putFile(fichero)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Almacenamiento", "Fichero subido");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Almacenamiento", "ERROR: subiendo fichero");
                    }
                });
    }

   /*private void bajarFichero() {
        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String path = localFile.getAbsolutePath();
        Log.d("Almacenamiento", "creando fichero: " + path);
        StorageReference ficheroRef = storageRef.child("imagenes/imagen");
        ficheroRef.getFile(localFile)
                .addOnSuccessListener(new
                                              OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
                                                  @Override
                                                  public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot){
                                                      Log.d("Almacenamiento", "Fichero bajado");
                                                      ImageView imageView = findViewById(R.id.imageView);
                                                      imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                                                  }
                                              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("Almacenamiento", "ERROR: bajando fichero");
            }
        });
    }*/




    public Tab3(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        storageRef = FirebaseStorage.getInstance().getReference();

        View v = inflater.inflate(R.layout.tab3, container, false);
        button= v.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 1234);

            }
        });


        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        Query query = FirebaseFirestore.getInstance()
                .collection("foto");
        FirestoreRecyclerOptions<Imagen> opciones = new FirestoreRecyclerOptions
                .Builder<Imagen>().setQuery(query, Imagen.class).build();
        adaptador = new AdaptadorImagenes(getContext(), opciones);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // bajarFichero();



        return v;
    }

    @Override
    public void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                subirFichero(data.getData(), "imagenes/imagen");
            }
        }
    }




@Override public void onStart() {
        super.onStart();
        adaptador.startListening();
    }
    @Override public void onStop() {
        super.onStop();
        adaptador.stopListening();
    }











}

