package com.example.clock.entity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.clock.MainActivity_pack.ClockExtraSetting;
import com.example.clock.R;

public class ClockUnitView {

    private final View view;
    private final LinearLayout clockUnit;
    private final FrameLayout fragmentContainer;
    private boolean isExpanded = false;
    private final FragmentManager fragmentManager;

    private static final String TAG = "ClockUnitView";

    private myClock myclock;

    public ClockUnitView(Context context, FragmentManager fragmentManager, ViewGroup parent) {
        this.view = LayoutInflater.from(context).inflate(R.layout.activity_clock_unit, parent, false);
        this.clockUnit = view.findViewById(R.id.clockUnit);
        this.fragmentContainer = new FrameLayout(context);

        this.fragmentContainer.setId(View.generateViewId());

        ((LinearLayout) view.findViewById(R.id.clockUnit)).addView(fragmentContainer);

        parent.addView(view);
        this.fragmentManager = fragmentManager;

        setupClickListener();
    }

    private void setupClickListener() {
        clockUnit.setOnClickListener(v -> {
            Log.d(TAG, "Clock unit clicked, isExpanded: " + isExpanded);
            Fragment fragment = fragmentManager.findFragmentById(fragmentContainer.getId());
            if (isExpanded) {
                if(fragment != null){
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
                collapse();
            } else {
                if (fragment == null){
                    expand();
                    fragmentManager.beginTransaction().replace(fragmentContainer.getId(),new ClockExtraSetting()).commit();
                }

            }
        });
    }

    private void expand() {
        Log.d(TAG, "Expanding ClockUnitView");
        isExpanded = true;
        fragmentContainer.setVisibility(View.VISIBLE);
        animateHeight(fragmentContainer, 0, (int) view.getResources().getDimension(R.dimen.time_unit_extra_content_height));
    }

    private void collapse() {
        Log.d(TAG, "Collapsing ClockUnitView");
        isExpanded = false;
        animateHeight(fragmentContainer, (int) view.getResources().getDimension(R.dimen.time_unit_extra_content_height), 0);
    }

    private void animateHeight(final FrameLayout container, int startHeight, int endHeight) {
        Log.d(TAG, "Animating height from " + startHeight + " to " + endHeight);
        if (container == null) {
            Log.e(TAG, "Container is null!");
            return;
        }

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
            if (layoutParams == null) {
                Log.e(TAG, "LayoutParams is null for container!");
                return;
            }
            layoutParams.height = animatedValue;
            container.setLayoutParams(layoutParams);
            Log.d(TAG, "Updated container height: " + animatedValue);
        });

        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isExpanded) {
                    container.setVisibility(View.GONE);
                    Log.d(TAG, "Container hidden after collapse");
                }
            }
        });
        animator.start();
    }

    public View getView() {
        return view;
    }
}
