package com.example.appespejo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import bolts.Task;

public class Galeria extends Fragment {

    StorageReference storageRef;
    AdaptadorImagenes adaptador;
    RecyclerView recyclerView;
    Button button;
    ImageView prueba;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3, container, false);

        // Create a Cloud Storage reference from the app

        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("prueba.jpeg");

        Log.d("Foto", pathReference.getDownloadUrl().toString());
        prueba = v.findViewById(R.id.imageView6);

        pathReference.getMetadata()
                .addOnSuccessListener( new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Metadatos obtenidos con éxito.
                        String nombre = storageMetadata.getName();
                        String evento = storageMetadata.getCustomMetadata("evento");

                        Log.d("almacen" , nombre);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("almacen", "Error");
                        // Ha ocurrido un error al descargar los metadatos
                    }
                });




//        -------------------------------------------------------------------
//        Recycler view
//        -------------------------------------------------------------------


        return v;
    }
    

    @Override
    public void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                //subirFichero(data.getData(), "imagenes/imagen");
            }
        }
    }

    @Override public void onStart() {
        super.onStart();
//        adaptador.startListening();
    }
    @Override public void onStop() {
        super.onStop();
//        adaptador.stopListening();
    }

    private void bajarFichero() {

    }



/*private void subirFichero(Uri fichero, String referencia) {
        final StorageReference ref = storageRef.child(referencia);
        UploadTask uploadTask = ref.putFile(fichero);
        Task<Uri> urlTask = uploadTask.continueWithTask(new
                                                                Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                    @Override public Task<Uri> then(@NonNull
                                                                                                            Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                        if (!task.isSuccessful()) throw task.getException();
                                                                        return ref.getDownloadUrl();
                                                                    }
                                                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.d("Almacenamiento", "URL: " + downloadUri.toString());
                    registrarImagen("Subida por Móvil", downloadUri.toString());
                } else {
                    Log.e("Almacenamiento", "ERROR: subiendo fichero");
                }
            }
        });
    }*/



}

