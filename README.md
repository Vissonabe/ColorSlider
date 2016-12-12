# ColorSlider
Horizontal and Vertical color sliding control for android.

![screenshot_1481460263](https://cloud.githubusercontent.com/assets/15524780/21080223/f018dcf2-bfcf-11e6-99b2-ae9623fcf504.png)

# Dependencies
## Gradle

    compile 'com.in.viswanathankp.colorslider:colorslider:1.0.0'

#Usage
Add click listener for color slider control

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
