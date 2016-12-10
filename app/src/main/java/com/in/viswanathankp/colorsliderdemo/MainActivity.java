package com.in.viswanathankp.colorsliderdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.in.viswanathankp.colorslider.ColorSlider;
import com.example.viswanathankp.colorsliderdemo.R;

public class MainActivity extends AppCompatActivity {

    private ColorSlider colorSlider,mVerticalSlider;
    private View mColorView,mVerticalColorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorSlider = (ColorSlider) findViewById(R.id.horizontal_slider);
        mColorView = findViewById(R.id.color_view);

        mVerticalSlider = (ColorSlider) findViewById(R.id.vertical_slider);
        mVerticalColorView = findViewById(R.id.color_view_vertical);

        colorSlider.setOnColorChangedListener(new ColorSlider.OnColorChangedListener() {
            @Override
            public void OnColorChanged(int color) {
                mColorView.setBackgroundColor(color);
            }
        });

        mVerticalSlider.setOnColorChangedListener(new ColorSlider.OnColorChangedListener() {
            @Override
            public void OnColorChanged(int color) {
                mVerticalColorView.setBackgroundColor(color);
            }
        });
    }
}
