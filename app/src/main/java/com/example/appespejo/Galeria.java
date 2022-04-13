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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Galeria extends Fragment {

    StorageReference storageRef;
    AdaptadorImagenes adaptador;
    RecyclerView recyclerView;
    Button button;
    ImageView prueba;
    FirebaseFirestore db;
    Context context;
    List<Uri> fotos = new ArrayList<>();
    TextView auxText;
    FirebaseAuth mAuth;
    private static final int GALLERY_INTENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3, container, false);

        // Create a Cloud Storage reference from the app

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        auxText = v.findViewById(R.id.textView45);
        storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("prueba/");
        Map<String, Object> fotoMap = new HashMap<>();
        int varAux = 0;

            Log.d("Foto", "Asincronia antes" );
            pathReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    Log.d("Foto", "listResult" + listResult.toString());

                    Log.d("Foto", "Antes de sleep");
                    int aux = 0;
                    for (int i=0; i<listResult.getItems().size(); i++){
                        aux = i;
                        int finalAux = aux;
                        listResult.getItems().get(i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                fotos.add(uri);
                                Log.d("Foto", "Anyadido " + uri.toString());
//                                Glide.with(v).load(uri).into(prueba);

                                fotoMap.put("Foto" + finalAux, uri.toString());
//                                db.collection("Fotos")
//                                        .document(mAuth.getCurrentUser().getUid())
//                                        .set(fotoMap);

                                if(finalAux == listResult.getItems().size()-1){
                                    db.collection("Fotos")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .set(fotoMap);
                                }
                            }
                        });
                    }

                    Log.d("Foto", "Antes de sleep");

                    Log.d("Foto", "Despues de sleep");



                    Log.d("Foto", "Despeus de sleep de bd");

                    auxText.setText(fotos.toString());
                }
            });

        prueba = v.findViewById(R.id.imageView6);

//        Glide.with(v)
//                .load(fotos.get(0).toString())
//                .into(prueba);

//        Picasso.get().load(fotos.get(0).getPath()).into(prueba);

        Log.d("Foto", "Size list fotos " + fotos.size());


//        -------------------------------------------------------------------
//        Recycler view
//        -------------------------------------------------------------------

        aux(v);

        return v;
    }

    public void aux(View v){

    }

    @Override
    public void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_INTENT) {
            if (requestCode == 1234) {
                Log.d("foto", "onAtivityResult");

                Uri uri = data.getData();
            }
        }
    }

    @Override public void onStart() {
        super.onStart();
//        adaptador.startListening();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Map<String, Object> mapFotos = new HashMap<>();
        db.collection("Fotos").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                for(int i=0; i<task.getResult().getData().size(); i++){
                    mapFotos.put("Foto",task.getResult().get("Foto"+i));
                }
            }
        });


//        recyclerView = findViewById(R.id.recyclerModos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        adaptador = new ColorListAdapter(getContext(), prueba);
//        recyclerView.setAdapter(adaptador);
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

