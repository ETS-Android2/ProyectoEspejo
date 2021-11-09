package com.example.appespejo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity {
   // private String[] nombres = new String[]{"Luces","Pestaña 2","Pestaña 3", "Pesataña 4", "Pesataña 5"};


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Context context = this;


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
//        usuarioNombre = (TextView) findViewById(R.id.usuarioNombre);

//        if(!usuario.isEmailVerified() && !usuario.isAnonymous()){
//            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//            Toast.makeText(HomeActivity.this, "Necesitas verificar tu correo", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signOut();
//        }


        Dialog dialogSheetDialog = new Dialog(getApplicationContext());

        View dialogSheetView = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.correo_no_verificado,null);
        dialogSheetDialog.setContentView(dialogSheetView);
        dialogSheetDialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));



//        if(usuario.isEmailVerified()){
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//            dialogSheetDialog.show();
//        } else{
//            dialogSheetDialog.cancel();
//            Log.d("Demo", "El correo es: " + Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified());
//        }

        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new Tab1()).commit();

        /*ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new MiPagerAdapter(this));
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position){
                        tab.setText(nombres[position]);
                    }
                }
        ).attach();*/
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void spotifyy(View view){
//        Intent i2 = getPackageManager().getLaunchIntentForPackage("com.spotify.android");
//        startActivity(i2);
        String apppackage = "com.spotify.android";
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage(apppackage);
            Log.d("Demo", "Esta cargando");
            startActivity(i);
        }
        catch (Exception  e) {
            Log.d("Demo", "Sorry, Spotify Apps Not Found");
            Toast.makeText(this, "Sorry, Spotify Apps Not Found", Toast.LENGTH_LONG).show();
        }
    }

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

                        case R.id.perfil:
                            fragment=new Tab4();
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
