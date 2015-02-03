package com.example.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Created by Barry Allen on 1/24/2015.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button alpha, translate, rotate, scale, set;
    CheckBox cb;
    ObjectAnimator alphaAnim, trans_Anim, trans_Anim1, trans_Anim2;
    AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alpha = (Button) findViewById(R.id.alpha_test);
        translate = (Button) findViewById(R.id.translate_test);
        rotate = (Button) findViewById(R.id.rotate_test);
        scale = (Button) findViewById(R.id.scale_test);
        set = (Button) findViewById(R.id.set_test);
        cb = (CheckBox) findViewById(R.id.load_from_res_test);
        alpha.setOnClickListener(this);
        translate.setOnClickListener(this);
        rotate.setOnClickListener(this);
        scale.setOnClickListener(this);
        cb.setChecked(false);
        set.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alpha_test:
                alphaAnim = ObjectAnimator.ofFloat(alpha, View.ALPHA, 0);
                alphaAnim.setRepeatCount(1);
                alphaAnim.setRepeatMode(ValueAnimator.REVERSE);
                alphaAnim.start();
                break;
            case R.id.translate_test:
                trans_Anim = ObjectAnimator.ofFloat(translate, View.TRANSLATION_X, 800);
                trans_Anim.setRepeatCount(1);
                trans_Anim.setRepeatMode(ValueAnimator.REVERSE);
                trans_Anim.start();
                break;
            case R.id.rotate_test:
                trans_Anim1 = ObjectAnimator.ofFloat(rotate, View.ROTATION, 360);
                trans_Anim1.setRepeatCount(1);
                trans_Anim1.setRepeatMode(ValueAnimator.REVERSE);
                trans_Anim1.start();
                break;
            case R.id.scale_test:
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2);
                trans_Anim2 = ObjectAnimator.ofPropertyValuesHolder(scale, pvhX, pvhY);
                trans_Anim2.setRepeatCount(1);
                trans_Anim2.setRepeatMode(ValueAnimator.REVERSE);
                trans_Anim2.start();
                break;
            case R.id.set_test:
                if (!cb.isChecked()) {
                    animatorSet = new AnimatorSet();
                    animatorSet.play(trans_Anim).after(alphaAnim).before(trans_Anim1);
                    animatorSet.play(trans_Anim1).before(trans_Anim2);
                    animatorSet.start();
                } else {
                    setupAnimation(alpha, R.animator.fade);
                    setupAnimation(translate, R.animator.move);
                    setupAnimation(rotate, R.animator.spin);
                    setupAnimation(scale, R.animator.scale);
                    setupAnimation(set, R.animator.combo);
                }
                break;
        }
    }

    private void setupAnimation(Button view, int resource) {
        Animator anim = AnimatorInflater.loadAnimator(this, resource);
        anim.setTarget(view);
        anim.start();
        return;

    }
}
