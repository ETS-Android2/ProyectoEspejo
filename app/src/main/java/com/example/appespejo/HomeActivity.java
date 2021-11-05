package com.example.appespejo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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


     ImageButton logout;
     private BottomNavigationView bottomNavigationView;
     FirebaseAuth mAuth;
     TextView usuarioNombre;
     FirebaseUser usuario;
     ImageView fotoUsuario;
     FirebaseFirestore db;
     FirebaseStorage storage;
     TextView perfilNombre;
     TextView perfilApellido;
     TextView perfilEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usuario = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        logout = (ImageButton) findViewById(R.id.logOut);
        fotoUsuario = findViewById(R.id.fotoUsuario);
        perfilNombre = findViewById(R.id.perfilNombre);
        perfilApellido = findViewById(R.id.perfilApellido);
        perfilEmail = findViewById(R.id.perfilCorreo);

        usuarioNombre = (TextView) findViewById(R.id.usuarioNombre);

        if(!usuario.isEmailVerified()){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            Toast.makeText(HomeActivity.this, "Necesitas verificar tu correo", Toast.LENGTH_SHORT).show();
        }

        if(usuario != null){

//-----------------------------------------------------------------------------------------------
//-------------Lo que queremos mostrar rollo datos del usuario se sacan apartir de aqui----------
//-----------------------------------------------------------------------------------------------

//            Glide.with(this)
//                    .load(usuario.getPhotoUrl())
//                    .into(fotoUsuario);

            Glide.with(this).clear(fotoUsuario);


            db.collection("Users")
                    .document(Objects.requireNonNull(usuario.getEmail()))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String name = task.getResult().getString("Nombre");
                                String correo = task.getResult().getString("Email");
                                String apellido = task.getResult().getString("Apellido");
                                usuarioNombre.setText("Hola, "+name);


//                                Glide.with(HomeActivity.this).load(usuario.getPhotoUrl()).into(fotoUsuario);
                            } else {
                                Log.e("Firestore", "Error al leer", task.getException());
                            }
                        }
                    });
        }

        else{
            usuarioNombre.setText("Usuario no logeado");
        }

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
        startActivity(new Intent(this, LoginActivity.class));
        finish();
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
