package com.lzy.androidlibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.lzy.androidlibrary.util.annotation.OnLPermissionDenied;
import com.lzy.androidlibrary.util.annotation.OnLPermissionGranted;
import com.lzy.androidlibrary.util.annotation.OnLPermissionNeverAskAgain;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/6/30
 * @desc
 */
public class LPermission {

    private Object context;
    private int requestCode;
    private String[] permissions;

    private LPermission(Object context) {
        this.context = context;
    }

    /**
     * 设置当前上下文
     * @param context
     * @return
     */
    public static LPermission with(Activity context) {
        return new LPermission(context);
    }

    public static LPermission with(Fragment fragment) {
        return new LPermission(fragment);
    }


    /**
     * 设置请求code
     * @param requestCode
     * @return
     */
    public LPermission setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * 设置请求权限集合
     * @param permissions
     * @return
     */
    public LPermission permissions(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    /**
     * 开始请求
     */
    public void request() {
        if (context == null) {
            throw new IllegalArgumentException("请使用withActivity设置参数！");
        }

        if (permissions == null || permissions.length == 0) {
            doExecuteSuccess(context, requestCode);
            return;
        }

        doRequestPermissions(context, requestCode, permissions);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    private static void doRequestPermissions(Object object, int requestCode, String[] permissions) {
        if (!isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return;
        }

        List<String> deniedPermissions = findDeniedPermissions(getActivity(object), permissions);
        if (deniedPermissions != null && deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            }
            else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            }
            else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }
        } else {
            doExecuteSuccess(object, requestCode);
        }
    }

    /**
     * Activity的onRequestPermissionsResult回调此方法
     *
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        dispatchResult(activity, requestCode, permissions, grantResults);
    }

    /**
     * Fragment的onRequestPermissionsResult回调此方法
     *
     * @param fragment
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        dispatchResult(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 处理权限授权结果
     *
     * @param object
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    private static void dispatchResult(Object object, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            if (hasNeverAskAgainPermission(getActivity(object), deniedPermissions)) { // 全部拒绝
                doExecuteFailAsNeverAskAgain(object, requestCode);
            } else {
                doExecuteFail(object, requestCode); // 部分拒绝
            }
        } else {
            doExecuteSuccess(object, requestCode);
        }
    }

    /**
     * 校验权限有没有二次拒绝
     *
     * @param activity
     * @param permission
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    static boolean hasNeverAskAgainPermission(Activity activity, List<String> permission) {
        if (!isOverMarshmallow()) {
            return false;
        }

        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED &&
                    !activity.shouldShowRequestPermissionRationale(value)) {
                return true;
            }
        }

        return false;
    }

    static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        }
        else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }

    /**
     * 系统是否 >=23 6.0
     *
     * @return
     */
    static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 检查权限是否被拒绝
     *
     * @param activity
     * @param permissions
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    static List<String> findDeniedPermissions(Activity activity, String... permissions) {
        if (!isOverMarshmallow()) {
            return null;
        }

        List<String> denyPermissions = new ArrayList<>();
        for (String value : permissions) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * ********************* reflect execute result *********************
     */

    /**
     * ********************* reflect execute result *********************
     */

    private static void doExecuteSuccess(Object activity, int requestCode) {
        executeMethod(activity, findMethodWithRequestCode(activity.getClass(), OnLPermissionGranted.class, requestCode));
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        executeMethod(activity, findMethodWithRequestCode(activity.getClass(), OnLPermissionDenied.class, requestCode));
    }

    private static void doExecuteFailAsNeverAskAgain(Object activity, int requestCode) {
        executeMethod(activity, findMethodWithRequestCode(activity.getClass(), OnLPermissionNeverAskAgain.class, requestCode));
    }

    private static <A extends Annotation> Method findMethodWithRequestCode(Class clazz, Class<A> annotation, int
            requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(annotation) != null &&
                    isEqualRequestCodeFromAnnotation(method, annotation, requestCode)) {
                return method;
            }
        }
        return null;
    }

    private static boolean isEqualRequestCodeFromAnnotation(Method m, Class clazz, int requestCode) {
        if (clazz.equals(OnLPermissionDenied.class)) {
            return requestCode == m.getAnnotation(OnLPermissionDenied.class).value();
        } else if (clazz.equals(OnLPermissionGranted.class)) {
            return requestCode == m.getAnnotation(OnLPermissionGranted.class).value();
        } else if (clazz.equals(OnLPermissionNeverAskAgain.class)) {
            return requestCode == m.getAnnotation(OnLPermissionNeverAskAgain.class).value();
        } else {
            return false;
        }
    }

    /**
     * ********************* reflect execute method *********************
     */

    private static void executeMethod(Object activity, Method executeMethod) {
        executeMethodWithParam(activity, executeMethod, new Object[]{});
    }

    private static void executeMethodWithParam(Object activity, Method executeMethod, Object... args) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) {
                    executeMethod.setAccessible(true);
                }
                executeMethod.invoke(activity, args);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
