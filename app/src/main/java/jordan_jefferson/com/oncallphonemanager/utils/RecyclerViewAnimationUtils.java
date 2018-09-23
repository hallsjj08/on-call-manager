package jordan_jefferson.com.oncallphonemanager.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAnimationUtils {

    private List<View> viewsToAnimateVisible = new ArrayList<>();
    private List<View> viewsToAnimateInvisible = new ArrayList<>();
    private List<View> viewsToAnimate = new ArrayList<>();

    public RecyclerViewAnimationUtils(){

    }

    public void addInvisibleToVisibleViews(View... invisibleViews){
        viewsToAnimateVisible.addAll(Arrays.asList(invisibleViews));
    }

    public void addVisibleToInvisibleViews(View... visibleViews){
        viewsToAnimateInvisible.addAll(Arrays.asList(visibleViews));
    }

    public void addVisibleViews(View... visibleViews){
        viewsToAnimate.addAll(Arrays.asList(visibleViews));
    }

    public void startAnimation(){

        if(!viewsToAnimateVisible.isEmpty()){
            for(View view : viewsToAnimateVisible){
                initToVisible(view);
            }
        }

        if(!viewsToAnimateInvisible.isEmpty()){
            for(View view : viewsToAnimateInvisible){
                initToInvisible(view);
            }
        }

        if(!viewsToAnimate.isEmpty()){
            for(View view : viewsToAnimate){
                initToAnimate(view);
            }
        }
    }

    public void reverseAnimation(){
        if(!viewsToAnimateVisible.isEmpty()){
            for(View view : viewsToAnimateVisible){
                reverseInitToVisible(view);
            }
        }

        if(!viewsToAnimateInvisible.isEmpty()){
            for(View view : viewsToAnimateInvisible){
                reverseInitToInvisible(view);
            }
        }

        if(!viewsToAnimate.isEmpty()){
            for(View view : viewsToAnimate){
                reverseInitToAnimate(view);
            }
        }
    }

    private void initToVisible(View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 16f);
        slideAnimator.setDuration(500);
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);
        fadeAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.playTogether(slideAnimator, fadeAnimator);
        view.setVisibility(View.VISIBLE);
        set.start();
    }

    private void reverseInitToVisible(View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        slideAnimator.setDuration(500);
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        fadeAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.playTogether(slideAnimator, fadeAnimator);
        set.start();
    }

    private void initToInvisible(final View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 16f);
        slideAnimator.setDuration(500);
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        fadeAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.playTogether(slideAnimator, fadeAnimator);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void reverseInitToInvisible(View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        slideAnimator.setDuration(500);
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        fadeAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.playTogether(slideAnimator, fadeAnimator);
        view.setVisibility(View.VISIBLE);
        set.start();
    }

    private void initToAnimate(View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 42f);
        slideAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.play(slideAnimator);
        set.start();
    }

    private void reverseInitToAnimate(View view){
        ObjectAnimator slideAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f);
        slideAnimator.setDuration(500);
        AnimatorSet set  = new AnimatorSet();
        set.play(slideAnimator);
        set.start();
    }
}
