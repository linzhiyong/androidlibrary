//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.zxing.client.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.client.android.camera.AutoFocusManager;
import com.google.zxing.client.android.camera.CameraConfigurationManager;
import com.google.zxing.client.android.camera.PreviewCallback;
import com.google.zxing.client.android.camera.open.OpenCameraInterface;
import com.google.zxing.client.android.camera.open.OpenCameraManager;

import java.io.IOException;

public final class CameraManager {
    private static final String TAG = CameraManager.class.getSimpleName();
    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 700;
    private static final int MAX_FRAME_HEIGHT = 700;
    private final Context context;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private AutoFocusManager autoFocusManager;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private boolean previewing;
    private int requestedFramingRectWidth;
    private int requestedFramingRectHeight;
    private final PreviewCallback previewCallback;

    public CameraManager(Context context) {
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
        this.previewCallback = new PreviewCallback(this.configManager);
    }

    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = this.camera;
        if(theCamera == null) {
            theCamera = ((OpenCameraInterface)(new OpenCameraManager()).build()).open();
            if(theCamera == null) {
                throw new IOException();
            }

            this.camera = theCamera;
        }

        theCamera.setPreviewDisplay(holder);
        if(!this.initialized) {
            this.initialized = true;
            this.configManager.initFromCameraParameters(theCamera);
            Rect parameters = holder.getSurfaceFrame();
            this.configManager.getScreenResolution().set(parameters.width(), parameters.height());
            if(this.requestedFramingRectWidth > 0 && this.requestedFramingRectHeight > 0) {
                this.setManualFramingRect(this.requestedFramingRectWidth, this.requestedFramingRectHeight);
                this.requestedFramingRectWidth = 0;
                this.requestedFramingRectHeight = 0;
            }
        }

        Parameters parameters1 = theCamera.getParameters();
        String parametersFlattened = parameters1 == null?null:parameters1.flatten();

        try {
            this.configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException var8) {
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            if(parametersFlattened != null) {
                parameters1 = theCamera.getParameters();
                parameters1.unflatten(parametersFlattened);

                try {
                    theCamera.setParameters(parameters1);
                    this.configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException var7) {
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

    }

    public synchronized boolean isOpen() {
        return this.camera != null;
    }

    public synchronized Camera getCamera() {
        return this.camera;
    }

    public synchronized void closeDriver() {
        if(this.camera != null) {
            this.camera.release();
            this.camera = null;
            this.framingRect = null;
            this.framingRectInPreview = null;
        }

    }

    public synchronized void startPreview() {
        Camera theCamera = this.camera;
        if(theCamera != null && !this.previewing) {
            theCamera.startPreview();
            this.previewing = true;
            this.autoFocusManager = new AutoFocusManager(this.context, this.camera);
        }

    }

    public synchronized void stopPreview() {
        if(this.autoFocusManager != null) {
            this.autoFocusManager.stop();
            this.autoFocusManager = null;
        }

        if(this.camera != null && this.previewing) {
            this.camera.stopPreview();
            this.previewCallback.setHandler((Handler)null, 0);
            this.previewing = false;
        }

    }

    public synchronized void setTorch(boolean newSetting) {
        if(this.camera != null) {
            if(this.autoFocusManager != null) {
                this.autoFocusManager.stop();
            }

            this.configManager.setTorch(this.camera, newSetting);
            if(this.autoFocusManager != null) {
                this.autoFocusManager.start();
            }
        }

    }

    public synchronized void requestPreviewFrame(Handler handler, int message) {
        Camera theCamera = this.camera;
        if(theCamera != null && this.previewing) {
            this.previewCallback.setHandler(handler, message);
            theCamera.setOneShotPreviewCallback(this.previewCallback);
        }

    }

    public synchronized Rect getFramingRect() {
        if(this.framingRect == null) {
            if(this.camera == null) {
                return null;
            }

            Point screenResolution = this.configManager.getScreenResolution();
            if(screenResolution == null) {
                return null;
            }

            int width = screenResolution.x * 3 / 4;
            if(width < 240) {
                width = 240;
            } else if(width > 700) {
                width = 700;
            }

            int height = screenResolution.y * 3 / 4;
            if(height < 240) {
                height = 240;
            } else if(height > 700) {
                height = 700;
            }

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            this.framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + this.framingRect);
        }

        return this.framingRect;
    }

    public synchronized Rect getFramingRectInPreview() {
        if(this.framingRectInPreview == null) {
            Rect framingRect = this.getFramingRect();
            if(framingRect == null) {
                return null;
            }

            Rect rect = new Rect(framingRect);
            Point cameraResolution = this.configManager.getCameraResolution();
            Point screenResolution = this.configManager.getScreenResolution();
            if(cameraResolution == null || screenResolution == null) {
                return null;
            }

            if(screenResolution.x < screenResolution.y) {
                rect.left = rect.left * cameraResolution.y / screenResolution.x;
                rect.right = rect.right * cameraResolution.y / screenResolution.x;
                rect.top = rect.top * cameraResolution.x / screenResolution.y;
                rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            } else {
                rect.left = rect.left * cameraResolution.x / screenResolution.x;
                rect.right = rect.right * cameraResolution.x / screenResolution.x;
                rect.top = rect.top * cameraResolution.y / screenResolution.y;
                rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
            }

            this.framingRectInPreview = rect;
        }

        return this.framingRectInPreview;
    }

    public synchronized void setManualFramingRect(int width, int height) {
        if(this.initialized) {
            Point screenResolution = this.configManager.getScreenResolution();
            if(width > screenResolution.x) {
                width = screenResolution.x;
            }

            if(height > screenResolution.y) {
                height = screenResolution.y;
            }

            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            this.framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated manual framing rect: " + this.framingRect);
            this.framingRectInPreview = null;
        } else {
            this.requestedFramingRectWidth = width;
            this.requestedFramingRectHeight = height;
        }

    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = this.getFramingRectInPreview();
        if(rect == null) {
            return null;
        } else {
            Point screenResolution = this.configManager.getScreenResolution();
            if(screenResolution.x >= screenResolution.y) {
                return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
            } else {
                byte[] rotatedData = new byte[data.length];

                for(int y = 0; y < height; ++y) {
                    for(int x = 0; x < width; ++x) {
                        rotatedData[x * height + height - y - 1] = data[x + y * width];
                    }
                }

                return new PlanarYUVLuminanceSource(rotatedData, height, width, rect.left, rect.top, rect.width(), rect.height(), false);
            }
        }
    }
}
