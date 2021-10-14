package com.example.appespejo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import top.defaults.colorpicker.ColorPickerPopup;

public class Tab1 extends Fragment {
   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/
   public Tab1(){
       // require a empty public constructor
   }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);

        //ColorPickerView colorPicker = v.findViewById(R.id.colorPicker);

        //colorPicker.show(v, new )

            new ColorPickerPopup.Builder(this.getContext());
                    /*.initialColor(Color.RED) // Set initial color
                    .enableBrightness(false) // Enable brightness slider or not
                    .enableAlpha(false) // Enable alpha slider or not
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(v, new ColorPickerPopup.ColorPickerObserver() {
                        @Override
                        public void onColorPicked(int color) {
                            v.setBackgroundColor(color);
                        }

                    });*/


        return inflater.inflate(R.layout.tab1, container, false);
    }
}