package com.example.appespejo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animacion

        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        ImageView logo = this.findViewById(R.id.imageView4);
        TextView text1 = this.findViewById(R.id.textView3);
        TextView text2 = this.findViewById(R.id.textView4);

        logo.setAnimation(animacion1);
        text1.setAnimation(animacion2);
        text2.setAnimation(animacion2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                Si el usuario ya esta logeado nos envia a Homed directamente. Si no, a Login

//                if(mAuth.getCurrentUser() == null){
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                }else{
//                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
//                }
                finish();
            }
        },4000);
    }
}