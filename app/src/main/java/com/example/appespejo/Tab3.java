package com.example.appespejo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Tab3 extends Fragment {
    /*private static final int MY_READ_PERMISSION_CODE =0 ;
    private RecyclerView recyclerView;
    public AdaptadorLugares adaptador;

    public Tab3(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3, container, false);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        recyclerView = binding.content.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);






        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_READ_PERMISSION_CODE);
        }
        return v;
    }

}*/

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;
    Context context;
    FirebaseFirestore db;


    //vars
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    private static final int MY_READ_PERMISSION_CODE=101;

    final static int RESULT_OK = 123;
    Button camara;

    public Tab3(){
        // require a empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab3, container, false);

        gallery_number = v.findViewById(R.id.gallery_number);
      recyclerView = v.findViewById(R.id.recyclerview_gallery_imagen);




        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_READ_PERMISSION_CODE);
        }else{
         loadImages();

        }
        return v;
    }


    private void loadImages(){

    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
    images= ImageGallery.lisofImages(getContext());

    galleryAdapter=new GalleryAdapter(getContext(), images, new GalleryAdapter.PhotoListener() {
        @Override
        public void onPhotoClick(String path) {
          Toast.makeText(getContext(),"La ruta es "+path,Toast.LENGTH_SHORT).show();
        }
    });

    recyclerView.setAdapter(galleryAdapter);
       // images = images.subList(0,Math.min(99, images.size()));
    gallery_number.setText("Photos("+images.size()+")");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==MY_READ_PERMISSION_CODE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this.getContext(),"Read external storage permission granted",Toast.LENGTH_SHORT).show();
            loadImages();
            }
            else {
                Toast.makeText(this.getContext(),"Read external storage denied", Toast.LENGTH_SHORT);
            }
        }
    }


    //Check from permission

}

