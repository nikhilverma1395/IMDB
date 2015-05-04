package com.nikhilvermavit.crossfade;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewAnimator;


public class MainActivity extends ActionBarActivity {

    Button btnNext, btnPrevious;
    ViewAnimator viewAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator1);
        final Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        final Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.slide_right);
        viewAnimator.setInAnimation(inAnim);
        viewAnimator.setOutAnimation(outAnim);

        viewAnimator.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewAnimator.showNext();
            }
        });

        Button btn1 = (Button) findViewById(R.id.buttonNext);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewAnimator.showPrevious();
            }
        });

        Button btn2 = (Button) findViewById(R.id.buttonPrevious);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewAnimator.showNext();
            }
        });

    }
}