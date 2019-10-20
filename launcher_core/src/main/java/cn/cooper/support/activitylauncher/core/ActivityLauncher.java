package cn.cooper.support.activitylauncher.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.Method;

public class ActivityLauncher {

    public static void fill(Activity activity, Bundle savedInstanceState) {
        Class<?> activityLauncher = getActivityLauncher(activity);
        if (activityLauncher != null) {
            try {
                Method fillMethod = activityLauncher.getMethod("fill", activity.getClass(), Bundle.class);
                fillMethod.invoke(null, activity, savedInstanceState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save(Activity activity, Bundle outState) {
        Class<?> activityLauncher = getActivityLauncher(activity);
        if (activityLauncher != null) {
            try {
                Method fillMethod = activityLauncher.getMethod("save", activity.getClass(), Bundle.class);
                fillMethod.invoke(null, activity, outState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void dispatchResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Class<?> activityLauncher = getActivityLauncher(activity);
        if (activityLauncher != null) {
            try {
                Method fillMethod = activityLauncher.getMethod("dispatchResult", activity.getClass(), int.class, int.class, Intent.class);
                fillMethod.invoke(null, activity, requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Class<?> getActivityLauncher(Activity activity) {
        String launcherName = activity.getClass().getCanonicalName() + "Launcher";
        try {
            return Class.forName(launcherName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
