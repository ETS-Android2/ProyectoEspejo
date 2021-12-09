package com.example.appespejo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;


public class HomeActivity extends AppCompatActivity {

     private BottomNavigationView bottomNavigationView;
     FirebaseAuth mAuth;
     FirebaseUser usuario;
     ImageView fotoUsuario;
     FirebaseFirestore db;
     FirebaseStorage storage;
     TextView perfilNombre,perfilApellido,perfilEmail,usuarioNombre;
     Animation animacion2;
     Button spotify,verificado;
     Dialog dialog;
     SeekBar seekBar;

     int ints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context = this;

        View tab1 = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.tab1,null);

        seekBar = tab1.findViewById(R.id.seekBar);
        ints = seekBar.getProgress();

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        fotoUsuario = findViewById(R.id.fotoUsuario);
        perfilNombre = findViewById(R.id.perfilNombre);
        perfilApellido = findViewById(R.id.perfilApellido);
        perfilEmail = findViewById(R.id.perfilCorreo);
        spotify = findViewById(R.id.spotify);
        verificado = findViewById(R.id.verificado);

//        --------------------------PARA EL ANONIMO-----------------------------
//        usuarioNombre = (TextView) findViewById(R.id.usuarioNombre);

//        if(!usuario.isEmailVerified() && !usuario.isAnonymous()){
//            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//            Toast.makeText(HomeActivity.this, "Necesitas verificar tu correo", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signOut();
//        }

//        if(usuario.isEmailVerified()){
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//            dialogSheetDialog.show();
//        } else{
//            dialogSheetDialog.cancel();
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//        }
//        -----------------------------------------------------------------------
//        -----------------------------------------------------------------------

        Dialog dialogSheetDialog = new Dialog(getApplicationContext());

        View dialogSheetView = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.correo_no_verificado,null);
        dialogSheetDialog.setContentView(dialogSheetView);
        dialogSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }

    public void intencsity(View view){

        View tab1 = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.tab1,null);

        seekBar = tab1.findViewById(R.id.seekBar);

        TextView textView = view.findViewById(R.id.intensidad);
        textView.setText(seekBar.getProgress());
    }

//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        LoginManager.getInstance().logOut();
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
//    }

    public void verificado(View view){
        if(usuario.isEmailVerified()){

        }
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(this, AcercaDeActivity.class);
        startActivity(i);
    }

    public void registrarAnonimo(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }

    private NavigationBarView.OnItemSelectedListener bottomNavMethod = new
            NavigationBarView.OnItemSelectedListener(){
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                    Fragment fragment=null;

                    switch (menuItem.getItemId()){

                        case R.id.ligth:
                            fragment=new Tab1();
                        break;

                        case R.id.music:
                            fragment=new Tab2();
                        break;

                        case R.id.photo:
                            fragment=new Tab3();
                        break;

                        case R.id.menu_home:
                            fragment=new HomeFragment();
                        break;

                        case R.id.conf:
                            fragment=new Tab5();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                    return true;
                }
            };


    /*Tab1 tab1 = new Tab1();
    Tab2 tab2 = new Tab2();
    Tab3 tab3 = new Tab3();
    Tab4 tab4 = new Tab4();
    Tab5 tab5 = new Tab5();*/

    /*public class MiPagerAdapter extends FragmentStateAdapter {
        public MiPagerAdapter(FragmentActivity activity){
            super(activity);
        }
        @Override
        public int getItemCount() {
            return 5;
        }
        @Override @NonNull
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new Tab1();
                case 1: return new Tab2();
                case 2: return new Tab3();
                case 3: return new Tab4();
                case 4: return new Tab5();
            }
            return null;
        }
    }*/
    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ligth:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tab1).commit();
                return true;

            case R.id.music:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tab2).commit();
                return true;

            case R.id.photo:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tab3).commit();
                return true;
            case R.id.perfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tab4).commit();
                return true;
            case R.id.conf:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, tab5).commit();
                return true;
        }
        return false;
    }*/


}
