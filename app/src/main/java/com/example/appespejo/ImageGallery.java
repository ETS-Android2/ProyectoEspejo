package com.example.appespejo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ImageGallery {

    @SuppressLint("Recycle")
    public static ArrayList<String> lisofImages (Context context){

        Uri uri;
        Cursor cursor;
        int columna_index_data, column_index_folder_name;
        ArrayList <String> listofAllImages= new ArrayList<>();
        String absolutePathofImages;

        //Ruta de la foto
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection ={MediaStore.MediaColumns.DATA,MediaStore.Images.Media.BUCKET_DISPLAY_NAME};


        String[] projection2 = new String[]{
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DATE_ADDED, MediaStore.Video.VideoColumns.DATE_ADDED
        };

        // Guiar un camino
        Cursor cur = context.getContentResolver()
                .query(uri, projection2, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");

        columna_index_data=cur.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //column_index_folder_name=cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        String orderBy=MediaStore.Video.Media.DATE_TAKEN;
        //cursor = context.getContentResolver().query(uri,projection,null,
         //       null, orderBy+ "DESC");

        //columna_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        //get foder name
        //olumn_index_folder_name=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while(cur.moveToNext()){
            absolutePathofImages=cur.getString(columna_index_data);//solo hasta data

            listofAllImages.add(absolutePathofImages);
        }
        return listofAllImages;
    }
    }

