package com.example.raspberrycam;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cam {
    public static String PATH_TO_CREDENTIALS =
            "C:\\Users\\Laura\\Desktop\\ProyectoEspejo\\raspberryCam\\prueba1-9bb89.json";

    public static String BUCKET = "prueba1-9bb89.appspot.com";
    public static Storage storage;
    public static void main(String[] args) {
        System.out.println("Hello Raspberry");
        try {
            storage = StorageOptions.newBuilder()
                    .setCredentials(ServiceAccountCredentials.fromStream(
                            new FileInputStream(PATH_TO_CREDENTIALS)))
                    .build()
                    .getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
        subirFichero(PATH_TO_CREDENTIALS, "carpeta/fichero.json");
    }
    static private void subirFichero(String fichero, String referencia) {
        try {
            BlobId blobId = BlobId.of(BUCKET, referencia);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, Files.readAllBytes(Paths.get(fichero)));
            //Da acceso al fichero a través de https. la URL es
            //https://storage.googleapis.com/BUCKET/nombre_recurso
            storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(),
                    Acl.Role.READER));
            System.out.println("Fichero subido: " + referencia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
