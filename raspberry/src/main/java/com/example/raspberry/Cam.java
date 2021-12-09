package com.example.raspberry;


import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import io.grpc.LoadBalancerRegistry;
import io.grpc.internal.PickFirstLoadBalancerProvider;

public class Cam {
    public static String PATH_TO_CREDENTIALS = "prueba1-9bb89.json";
    //public static String RUTA_ARCHIVO = "C:\\Users\\Laura\\Desktop\\ProyectoEspejo\\raspberry\\imagen.jpg";
    public static String ficherofoto = "C:\\Users\\Laura\\Desktop\\prueba2.jpg";
    public static String BUCKET = "prueba1-9bb89.appspot.com";
    public static Storage storage;

    public static void main(String[] args) {

        String nombreFichero = UUID.randomUUID().toString();

        LoadBalancerRegistry.getDefaultRegistry()
                .register(new PickFirstLoadBalancerProvider());
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            new FileInputStream(PATH_TO_CREDENTIALS)))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //tomarFoto("captura.jpeg");


        String url = "https://storage.googleapis.com/"+BUCKET+"/"+"fotos/"+nombreFichero+".jpeg";
        guardarFirestore(url, "subido desde Raspberry Pi");

        //subirFichero("./captura.jpeg", "fotos/imagen.jpeg");
        //subirFichero("captura.jpeg", "fotos/"+nombreFichero+".jpeg");

    }


    /*
    static private void subirFichero(String fichero, String referencia) {
        try {
            BlobId blobId = BlobId.of(BUCKET, referencia);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            System.out.println(blobInfo);
            System.out.println(blobId);

            storage.create(blobInfo, Files.readAllBytes(Paths.get(fichero)));
            //Da acceso al fichero a través de https. la URL es
            //https://storage.googleapis.com/BUCKET/nombre_recurso
            storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(),
                    Acl.Role.READER));
            System.out.println("Fichero subido: " + referencia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    static int tomarFoto(String fichero) {
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec("raspistill -o " + fichero);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            p.waitFor();
            p.destroy();
            return p.exitValue();
        } catch (Exception e) {
            System.out.println("Error al tomar foto: libcamera-jpeg -o ");
            return -1;
        }
    }

    static void guardarFirestore(String url, String titulo){
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("foto").document();
        Map<String, Object> data = new HashMap<>();
        data.put("titulo", titulo);
        data.put("url", url);
        data.put("tiempo", System.currentTimeMillis());
        ApiFuture<WriteResult> result = docRef.set(data); //escritura asíncrona
        try { //al añadir result.get() bloquemos hasta respuesta
            System.out.println("Tiempo subida: "+result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}