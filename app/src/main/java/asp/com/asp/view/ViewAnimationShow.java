package asp.com.asp.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class ViewAnimationShow {

    private static final int ANIMATION_DURATION_SHOW_PROFILE_DETAILS = 500;

    private Context context;
    private View mToolbarProfile;
    private View mProfileDetails;
    private ImageView imageView;
    private View infoLinearLayout;

    private AnimatorSet mOpenProfileAnimatorSet;
    private AnimatorSet mCloseProfileAnimatorSet;


    public ViewAnimationShow(Context context,View infoLinearLayout){
        this.context = context;
        this.infoLinearLayout = infoLinearLayout;

    }

    public ViewAnimationShow(Context context,View mToolbarProfile,View img,View mProfileDetails){
        this.context = context;
        this.mToolbarProfile = mToolbarProfile;
        this.imageView = (ImageView)img;
        this.mProfileDetails = mProfileDetails;
    }


    public void animateOpenTopProfileDetails(int profileDetailsAnimationDelay) {
        getOpenProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }
    public void animateOpenProfileDetails(int profileDetailsAnimationDelay) {
        getOpenProfileDetailsAnimator(profileDetailsAnimationDelay).start();
    }

    public void animateCloseProfileDetails(int profileDetailsAnimationDelay) {
        //  mState = EuclidState.Closing;
        getCloseProfileAnimatorSet(profileDetailsAnimationDelay).start();
    }

    private AnimatorSet getOpenProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if (mOpenProfileAnimatorSet == null) {
            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(getOpenProfileToolbarAnimator());
            profileAnimators.add(getOpenProfileImageViewAnimator());

            mOpenProfileAnimatorSet = new AnimatorSet();

            mOpenProfileAnimatorSet.playTogether(profileAnimators);
            mOpenProfileAnimatorSet.setDuration(ANIMATION_DURATION_SHOW_PROFILE_DETAILS);
        }
        mOpenProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);
        mOpenProfileAnimatorSet.setInterpolator(new DecelerateInterpolator());
        return mOpenProfileAnimatorSet;
    }

    private Animator getOpenProfileToolbarAnimator() {

        Animator mOpenProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.Y,
                -mToolbarProfile.getHeight(),0);
       // Log.i("getHeight", ".." + mToolbarProfile.getHeight() + "pxpxp" + dpToPx(mToolbarProfile.getHeight()));
        mOpenProfileToolbarAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mToolbarProfile.setX(0);
                mToolbarProfile.bringToFront();
                mToolbarProfile.setVisibility(View.VISIBLE);
                imageView.setX(0);
                imageView.bringToFront();
                imageView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //  mButtonProfile.startAnimation(mProfileButtonShowAnimation);

                //     mState = EuclidState.Opened;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return mOpenProfileToolbarAnimator;
    }
    private Animator getOpenProfileImageViewAnimator() {
        Animator mOpenProfileImageViewAnimator = ObjectAnimator.ofFloat(imageView, View.Y,
                context.getResources().getDisplayMetrics().heightPixels,
                mToolbarProfile.getHeight());
        return mOpenProfileImageViewAnimator;
    }
    private Animator getOpenProfileDetailsAnimator(int profileDetailsAnimationDelay) {
        Animator mOpenProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.Y,
                context.getResources().getDisplayMetrics().heightPixels,
                imageView.getHeight()+mToolbarProfile.getHeight());
        mOpenProfileDetailsAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mProfileDetails.setX(0);
                mProfileDetails.bringToFront();
                mProfileDetails.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mOpenProfileDetailsAnimator.setDuration(ANIMATION_DURATION_SHOW_PROFILE_DETAILS);
        mOpenProfileDetailsAnimator.setStartDelay(profileDetailsAnimationDelay);
        mOpenProfileDetailsAnimator.setInterpolator(new DecelerateInterpolator());
        Log.i("getHeight","...mProfileDetails:"+""+context.getResources().getDisplayMetrics().heightPixels);
        return mOpenProfileDetailsAnimator;
    }

    private Animator getCloseProfileAnimatorSet(int profileDetailsAnimationDelay) {
        if(mCloseProfileAnimatorSet == null){
            Animator mCloseProfileToolbarAnimator = ObjectAnimator.ofFloat(mToolbarProfile, View.X,
                    0,mToolbarProfile.getWidth());
            mCloseProfileToolbarAnimator.setStartDelay(profileDetailsAnimationDelay);
            Animator mCloseProfileImageViewAnimator = ObjectAnimator.ofFloat(imageView, View.X,
                    0,imageView.getWidth());
            mCloseProfileImageViewAnimator.setStartDelay(profileDetailsAnimationDelay*2);
            Animator mCloseProfileDetailsAnimator = ObjectAnimator.ofFloat(mProfileDetails, View.X,
                    0,mProfileDetails.getWidth());
            mCloseProfileDetailsAnimator.setStartDelay(profileDetailsAnimationDelay*3);

            List<Animator> profileAnimators = new ArrayList<>();
            profileAnimators.add(mCloseProfileToolbarAnimator);
            profileAnimators.add(mCloseProfileImageViewAnimator);
            profileAnimators.add(mCloseProfileDetailsAnimator);

            mCloseProfileAnimatorSet = new AnimatorSet();
            mCloseProfileAnimatorSet.playTogether(profileAnimators);
            mCloseProfileAnimatorSet.setDuration(ANIMATION_DURATION_SHOW_PROFILE_DETAILS);

        }

        mCloseProfileAnimatorSet.setStartDelay(profileDetailsAnimationDelay);
        mCloseProfileAnimatorSet.setInterpolator(new AccelerateInterpolator());
        mCloseProfileAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mToolbarProfile.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                mProfileDetails.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return mCloseProfileAnimatorSet;
    }

    public int dpToPx(int dp) {
        return Math.round((float) dp * context.getResources().getDisplayMetrics().density);
    }


}
