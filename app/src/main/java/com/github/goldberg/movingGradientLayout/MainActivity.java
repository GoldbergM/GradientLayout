package com.github.goldberg.movingGradientLayout;

import android.annotation.TargetApi;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovingGradientLayout movingGradientLayout = (MovingGradientLayout) findViewById(R.id.gradientLayout);
        movingGradientLayout.getGradientDrawable().setOrientation(GradientDrawable.Orientation.TL_BR);

    }
}
