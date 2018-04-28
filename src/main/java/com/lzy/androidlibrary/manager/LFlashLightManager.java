package com.lzy.androidlibrary.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.widget.Toast;

import com.lzy.androidlibrary.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 闪光灯管理工具类
 *
 * http://www.360doc.com/content/14/0308/15/3700464_358779548.shtml
 *
 * @author linzhiyong
 * @time 2016年11月17日07:47:03
 * @email wflinzhiyong@163.com
 *
 * @desc * 如果配合拍照使用, 则无需调用init()方法, 直接使用turnLightOnCamera(Camera c) turnLightOffCamera(Camera c)
 *       * 如果只作为手电筒使用, 则需要初始化init()方法, 使用turnOn() turnOff()
 */
public class LFlashLightManager {

    private static final String TAG = LFlashLightManager.class.getName();

    /** 上下文对象 */
    private Context context;

    /** 是否已经开启闪光灯 */
    private boolean isOpenFlash = false;

    /** Camera相机硬件操作类 */
    private Camera camera = null;

    /** Camera2相机硬件操作类 */
    private CameraManager manager = null;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession = null;
    private CaptureRequest request = null;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private String cameraId = null;
    private boolean isSupportFlashCamera2 = false;

    private LFlashLightManager() {
    }

    public LFlashLightManager(Context context) {
        this.context = context;
    }

    /**
     * 初始化相机
     */
    public void init() {
        this.manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (isLOLLIPOP()) {
            initCamera2();
        } else {
            camera = Camera.open();
        }
    }

    /**
     * 开启闪光灯
     */
    public void turnOn() {
        if (!isSupportFlash()) {
            showToast("设备不支持闪光灯！");
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showToast("应用未开启访问相机权限！");
            return;
        }
        if (isOpenFlash) {
            return;
        }

        if (isLOLLIPOP()) {
            turnLightOnCamera2();
        } else {
            turnLightOnCamera(camera);
        }
    }

    /**
     * 关闭闪光灯
     */
    public void turnOff() {
        if (!isSupportFlash()) {
            showToast("设备不支持闪光灯！");
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showToast("应用未开启访问相机权限！");
            return;
        }
        if (!isOpenFlash) {
            return;
        }
        if (isLOLLIPOP()) {
            turnLightOffCamera2();
        } else {
            turnLightOffCamera(camera);
        }
        isOpenFlash = false;
    }

    /**
     * 开启Camera2闪光灯
     */
    private void turnLightOnCamera2() {
        new Object() {
            private void _turnLightOnCamera2() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        showToast("应用未开启访问相机权限！");
                        return;
                    }
                    try {
                        manager.openCamera(cameraId, new CameraDevice.StateCallback() {

                            @Override
                            public void onOpened(CameraDevice camera) {
                                cameraDevice = camera;
                                createCaptureSession();
                            }

                            @Override
                            public void onError(CameraDevice camera, int error) {
                            }

                            @Override
                            public void onDisconnected(CameraDevice camera) {
                            }
                        }, null);
                    } catch (Exception e) {
                        LoggerUtil.error(TAG, e.getMessage(), e);
                        showToast("开启失败：" + e.getMessage());
                    }
                }
            }
        }._turnLightOnCamera2();
    }

    /**
     * 关闭Camera2闪光灯
     */
    private void turnLightOffCamera2() {
        new Object() {
            private void _turnLightOffCamera2() {
                if (cameraDevice != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cameraDevice.close();
                }
            }
        }._turnLightOffCamera2();
    }

    /**
     * 判断设备是否支持闪光灯
     *
     * @return boolean
     */
    public boolean isSupportFlash() {
        if (isLOLLIPOP()) { // 判断当前Android系统版本是否 >= 21, 分别处理
            return isSupportFlashCamera2;
        } else {
            PackageManager pm = context.getPackageManager();
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            for (FeatureInfo f : features) {
                // 判断设备是否支持闪光灯
                if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                    return true;
                }
            }
            // 判断是否支持闪光灯,方式二
            // Camera.Parameters parameters = camera.getParameters();
            // if (parameters == null) {
            // return false;
            // }
            // List<String> flashModes = parameters.getSupportedFlashModes();
            // if (flashModes == null) {
            // return false;
            // }
        }
        return false;
    }

    /**
     * 是否已经开启闪光灯
     *
     * @return
     */
    public boolean isTurnOnFlash() {
        return isOpenFlash;
    }

    /**
     * 判断Android系统版本是否 >= LOLLIPOP(API21)
     *
     * @return boolean
     */
    private boolean isLOLLIPOP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过设置Camera打开闪光灯
     *
     * @param mCamera
     */
    public void turnLightOnCamera(Camera mCamera) {
//        mCamera.startPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // 开启闪光灯
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            }
        }
        isOpenFlash = true;
    }

    /**
     * 通过设置Camera关闭闪光灯
     *
     * @param mCamera
     */
    public void turnLightOffCamera(Camera mCamera) {
//        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // 关闭闪光灯
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            }
        }
        isOpenFlash = false;
    }

    /**
     * 初始化Camera2
     */
    private void initCamera2() {
        new Object() {
            private void _initCamera2() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        for (String _cameraId : manager.getCameraIdList()) {
                            CameraCharacteristics characteristics = manager.getCameraCharacteristics(_cameraId);
                            // 过滤掉前置摄像头
                            Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                                continue;
                            }
                            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                            if (map == null) {
                                continue;
                            }
                            cameraId = _cameraId;
                            // 判断设备是否支持闪光灯
                            isSupportFlashCamera2 = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        }
                    } catch (Exception e) {
                        LoggerUtil.error(TAG, e.getMessage(), e);
                        showToast("初始化失败：" + e.getMessage());
                    }
                }
            }
        }._initCamera2();
    }

    /**
     * createCaptureSession
     */
    private void createCaptureSession() {
        new Object() {
            private void _createCaptureSession() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {

                        public void onConfigured(CameraCaptureSession arg0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                captureSession = arg0;
                                CaptureRequest.Builder builder;
                                try {
                                    builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                    builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
                                    builder.addTarget(surface);
                                    request = builder.build();
                                    captureSession.capture(request, null, null);
                                    isOpenFlash = true;
                                } catch (Exception e) {
                                    LoggerUtil.error(TAG, e.getMessage(), e);
                                    showToast("开启失败：" + e.getMessage());
                                }
                            }
                        }

                        public void onConfigureFailed(CameraCaptureSession arg0) {
                        }
                    };

                    surfaceTexture = new SurfaceTexture(0, false);
                    surfaceTexture.setDefaultBufferSize(1280, 720);
                    surface = new Surface(surfaceTexture);
                    ArrayList localArrayList = new ArrayList(1);
                    localArrayList.add(surface);
                    try {
                        cameraDevice.createCaptureSession(localArrayList, stateCallback, null);
                    } catch (Exception e) {
                        LoggerUtil.error(TAG, e.getMessage(), e);
                        showToast("开启失败：" + e.getMessage());
                    }
                }
            }
        }._createCaptureSession();
    }

    private void showToast(String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

}
