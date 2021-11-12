package com.example.appespejo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.flag.FlagView;

public class CustomFlag extends FlagView {

    private TextView textView;
    private AlphaTileView alphaTileView;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference red = database.getReference("red");
    DatabaseReference green = database.getReference("green");
    DatabaseReference blue = database.getReference("blue");


    @SuppressLint("WrongViewCast")
    public CustomFlag(Context context, int layout) {
        super(context, layout);
        textView = findViewById(R.id.flag_color_code);
        alphaTileView = findViewById(R.id.flag_color_layout);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {

        int[] color = colorEnvelope.getArgb();
        //Integer i = Color.red(color);
        int i = Integer.decode(String.valueOf(colorEnvelope.getColor()));
        int colorR = Color.red(i);
        int colorG = Color.green(i);
        int colorB = Color.blue(i);

        red.setValue(colorR);
        green.setValue(colorG);
        blue.setValue(colorB);

        Log.d("Demo", "Rojo " + colorR + "Green " + colorG + "Blue " + colorB);
        // textView.setText("#" + colorEnvelope.getHexCode());
        // alphaTileView.setPaintColor(colorEnvelope.getColor());
    }

}
