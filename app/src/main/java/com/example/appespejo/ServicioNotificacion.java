package com.example.appespejo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Provider;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServicioNotificacion extends BroadcastReceiver {

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;
    private final String url = "https://api.openweathermap.org/data/2.5/weather?q=Gandia&units=metric&appid=e96051f26738be95560f9d1a8d60feb6";

    @Override
    public void onReceive(Context context, Intent intent) {

//        if (!intent.hasExtra("activate")) {
//            new IndexerMasterViewsTask().execute();
//            new IndexerWorkViewsTask().execute();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    int tempa = jsonObjectMain.getInt("temp");
                    String city = jsonResponse.getString("name");
                    String icon = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

                    String urlfoto  = "http://openweathermap.org/img/wn/" + icon + ".png";
                    Log.d("Demo" , urlfoto);

                    Bitmap foto = getBitmapFromURL(urlfoto);
                    notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { NotificationChannel notificationChannel = new NotificationChannel(
                            CANAL_ID, "Mis Notificaciones", NotificationManager.IMPORTANCE_DEFAULT);

                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(context, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_logo)
                                    .setLargeIcon(foto)
                                    .setContentTitle("Hoy en "+city +" se hace " + String.valueOf(tempa) + "°C")
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setContentText("con la minima de " + String.valueOf(tempa-4) + "°C\n y la maxima de " +  String.valueOf(tempa+3) + "°C");
                    notificationManager.notify(NOTIFICACION_ID, notificacion.build());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}
