package util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.chengjie.myapplication.MainActivity;

import java.util.ArrayList;

/**
 * Created by chengjie on 17-8-20.
 */

public class ErrorCode {

    public static ArrayList<String> teaPicName = null;
    public static ArrayList<String> shortDescription=null;
    public static ArrayList<String> longDescription=null;
    public static ArrayList<String> name=null;
    public static Activity activity = null;
    private static ValueAnimator widthAnimation;
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String LOGIN_FAILED = "LOGIN_FAILED";
    public static final String LOGIN_TIMEOUT = "LOGIN_TIMEOUT";
    public static final String LOGIN_NETWORK_EXCEPTION = "LOGIN_NETWORK_EXCEPTION";
    public static final String LOGIN_INPUT_NONE = "LOGIN_INPUT_NONE";
    public static final String LOGIN_SERVER_EXCEPTION = "LOGIN_SERVER_EXCEPTION";


    public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
    public static final String REGISTER_TIMEOUT = "REGISTER_TIMEOUT";
    public static final String REGISTER_CONFIRM_ERROR = "REGISTER_CONFIRM_ERROR";
    public static final String REGISTER_INPUT_NONE = "REGISTER_INPUT_NONE";
    public static final String REGISTER_USERNAME_EXIST = "REGISTER_USERNAME_EXIST";
    public static final String REGISTER_PHONE_EXIST = "REGISTER_PHONE_EXIST";
    public static final String REGISTER_SERVER_EXCEPTION = "REGISTER_SERVER_EXCEPTION";
    public static final String REGISTER_ILLEGAL_NAME = "REGISTER_ILLEGAL_NAME";
    public static final String REGISTER_ILLEGAL_PHONE = "REGISTER_ILLEGAL_PHONE";

    public static void showErrorInfo(final Context context, final CircularProgressButton button, String code, final String info) {

        switch (code) {
            case LOGIN_SUCCESS:
                final Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("res", teaPicName);
                intent.putExtra("long",longDescription);
                intent.putExtra("short",shortDescription);
                intent.putExtra("name",name);
                widthAnimation = ValueAnimator.ofInt(1, 200);
                widthAnimation.setDuration(2000);
                widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        if (value <= 100)
                            button.setProgress(value);
                        if (value == 200) {
                            context.startActivity(intent);
                            activity.finish();
                        }
                    }
                });
                widthAnimation.start();
                break;
            case LOGIN_FAILED:
                widthAnimation = ValueAnimator.ofInt(1, 200);
                widthAnimation.setDuration(2000);
                widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        if (value < 99)
                            button.setProgress(value);
                        if (value == 100)
                            button.setProgress(-1);
                        if (value == 200)
                            button.setProgress(0);
                    }
                });
                widthAnimation.start();
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
                break;
            case LOGIN_TIMEOUT:
            case LOGIN_NETWORK_EXCEPTION:
            case LOGIN_INPUT_NONE:
            case LOGIN_SERVER_EXCEPTION:
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
                break;
            case REGISTER_SUCCESS:
                final Intent i = new Intent(context, MainActivity.class);
                i.putExtra("res", teaPicName);
                i.putExtra("long",longDescription);
                i.putExtra("short",shortDescription);
                i.putExtra("name",name);
                widthAnimation = ValueAnimator.ofInt(1, 200);
                widthAnimation.setDuration(2000);
                widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        if (value <= 100)
                            button.setProgress(value);
                        if (value == 200) {
                            context.startActivity(i);
                            activity.finish();
                        }
                    }
                });
                widthAnimation.start();
                break;
            case REGISTER_USERNAME_EXIST:
                widthAnimation = ValueAnimator.ofInt(1, 200);
                widthAnimation.setDuration(2000);
                widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        if (value < 99)
                            button.setProgress(value);
                        if (value == 100)
                            button.setProgress(-1);
                        if (value == 200)
                            button.setProgress(0);
                    }
                });
                widthAnimation.start();
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
                break;
            case REGISTER_PHONE_EXIST:
                widthAnimation = ValueAnimator.ofInt(1, 200);
                widthAnimation.setDuration(2000);
                widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        if (value < 99)
                            button.setProgress(value);
                        if (value == 100)
                            button.setProgress(-1);
                        if (value == 200)
                            button.setProgress(0);
                    }
                });
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
                break;
            case REGISTER_TIMEOUT:
            case REGISTER_CONFIRM_ERROR:
            case REGISTER_INPUT_NONE:
            case REGISTER_ILLEGAL_NAME:
            case REGISTER_ILLEGAL_PHONE:
            case REGISTER_SERVER_EXCEPTION:
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
