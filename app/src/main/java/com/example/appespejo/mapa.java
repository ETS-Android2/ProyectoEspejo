package com.example.appespejo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapa extends FragmentActivity implements OnMapReadyCallback {
    private static final int SOLICITUD_PERMISO_ACCESS_FINE_LOCATION = 0;
    private GoogleMap mapa;
    private final LatLng UPV = new LatLng(38.995668, -0.165782);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));
        mapa.addMarker(new MarkerOptions().position(UPV).title("UPV").snippet("Universidad Politécnica de Valencia").icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass)).anchor(0.5f, 0.5f));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);

        }else{
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION, "No está activado"+
                            " el permiso de localización",
                    SOLICITUD_PERMISO_ACCESS_FINE_LOCATION, this);

        }
    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }}).show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == SOLICITUD_PERMISO_ACCESS_FINE_LOCATION) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mapa);
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void irUPV(View view) {
        mapa.animateCamera(CameraUpdateFactory.newLatLng(UPV));
    }



}

