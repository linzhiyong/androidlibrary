package com.google.zxing.client.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import java.io.IOException;
import java.util.Collection;

/**
 * 类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/5/2
 * @desc
 */
public class BarcodeScaner extends FrameLayout implements SurfaceHolder.Callback {
    private static final String TAG = BarcodeScaner.class.getSimpleName();
    private ScannerWay way;
    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private boolean hasSurface;
    private CameraManager cameraManager;
    private BarcodeScanerHandler handler;
    private Result savedResultToShow;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private int beepResId;
    private BarcodeScanListener listener;
    private MediaPlayer mediaPlayer;

    public BarcodeScaner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarcodeScaner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        this.hasSurface = false;
        this.surfaceView = new SurfaceView(this.getContext());
        LayoutParams params = new LayoutParams(-1, -1);
        this.addView(this.surfaceView, params);
        this.viewfinderView = new ViewfinderView(this.getContext(), attrs);
        params = new LayoutParams(-1, -1);
        this.addView(this.viewfinderView, params);
        this.initMediaPlayer();
    }

    public BarcodeScanListener getListener() {
        return this.listener;
    }

    public void setListener(BarcodeScanListener listener) {
        this.listener = listener;
    }

    public void setScannerWay(ScannerWay way) {
        this.way = way;
    }

    public Collection<BarcodeFormat> getDecodeFormats() {
        return this.decodeFormats;
    }

    public void setDecodeFormats(Collection<BarcodeFormat> decodeFormats) {
        this.decodeFormats = decodeFormats;
    }

    public String getCharacterSet() {
        return this.characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public void open() {
        if(!this.hasSurface) {
            this.cameraManager = new CameraManager(this.getContext());
            this.viewfinderView.setCameraManager(this.cameraManager);
            SurfaceHolder surfaceHolder = this.surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(3);
            this.handler = null;
        }
    }

    public void close() {
        this.hasSurface = false;
        if(this.cameraManager != null) {
            this.cameraManager.closeDriver();
            this.cameraManager = null;
        }

        if(this.surfaceView != null) {
            this.surfaceView.getHolder().removeCallback(this);
        }

    }

    public void scan() {
        if(this.hasSurface) {
            this.sendReplyMessage(com.haiyisoft.mobile.android.R.id.restart_preview, (Object)null, 1500L);
        }
    }

    public CameraManager getCameraManager() {
        return this.cameraManager;
    }

    public BarcodeScanerHandler getHandler() {
        return this.handler;
    }

    public ViewfinderView getViewfinderView() {
        return this.viewfinderView;
    }

    private void drawResultPoints(Bitmap barcode, Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if(points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(Color.argb(192, 153, 204, 0));
            if(points.length == 2) {
                paint.setStrokeWidth(4.0F);
                drawLine(canvas, paint, points[0], points[1]);
            } else if(points.length == 4 && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1]);
                drawLine(canvas, paint, points[2], points[3]);
            } else {
                paint.setStrokeWidth(10.0F);
                ResultPoint[] var9 = points;
                int var8 = points.length;

                for(int var7 = 0; var7 < var8; ++var7) {
                    ResultPoint point = var9[var7];
                    canvas.drawPoint(point.getX(), point.getY(), paint);
                }
            }
        }

    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b) {
        canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
    }

    private void sendReplyMessage(int id, Object arg, long delayMS) {
        Message message = Message.obtain(this.handler, id, arg);
        if(delayMS > 0L) {
            this.handler.sendMessageDelayed(message, delayMS);
        } else {
            this.handler.sendMessage(message);
        }

    }

    private void initMediaPlayer() {
        if(this.mediaPlayer == null && this.getBeepResId() > 0) {
            this.mediaPlayer = MediaPlayer.create(this.getContext(), this.getBeepResId());
            if (this.mediaPlayer == null) {
                return;
            }
            this.mediaPlayer.setVolume(0.1F, 0.1F);
        }

    }

    private void freeSources() {
        if(this.mediaPlayer != null) {
            this.mediaPlayer.release();
        }

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator)this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200L);
    }

    /**
     * 播放声音，true表示第一次尝试
     * @param flag
     */
    private void playSound(boolean flag) {
        if (!flag) {
            return;
        }
        if(this.mediaPlayer != null) {
            this.mediaPlayer.start();
        } else {
            this.initMediaPlayer();
            this.playSound(false);
//            this.freeSources();
        }

    }

    protected void handleDecode(Result rawResult, Bitmap barcode) {
        boolean fromLiveScan = barcode != null;
        if(fromLiveScan) {
            this.drawResultPoints(barcode, rawResult);
        }

        playNotification();

        if(this.listener != null) {
            this.listener.scanSuccess(rawResult, barcode);
        }

        Log.d(TAG, this.surfaceView.getHolder().getSurfaceFrame().toString());
    }

    private void playNotification() {
        if(this.way != null) {
            switch(this.way.ordinal()) {
                case 0:
                case 1:
                    this.vibrate();
                    break;
                case 2:
                    this.playSound(true);
                    break;
                case 3:
                    this.playSound(true);
                    this.vibrate();
            }
        }
    }

    protected void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        if(this.handler == null) {
            this.savedResultToShow = result;
        } else {
            if(result != null) {
                this.savedResultToShow = result;
            }

            if(this.savedResultToShow != null) {
                Message message = Message.obtain(this.handler, com.haiyisoft.mobile.android.R.id.decode_succeeded, this.savedResultToShow);
                this.handler.sendMessage(message);
            }

            this.savedResultToShow = null;
        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if(surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        } else if(this.cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
        } else {
            try {
                this.cameraManager.openDriver(surfaceHolder);
                if(this.handler == null) {
                    this.handler = new BarcodeScanerHandler(this, this.decodeFormats, this.characterSet, this.cameraManager);
                }

                this.decodeOrStoreSavedBitmap((Bitmap)null, (Result)null);
            } catch (IOException var3) {
                Log.w(TAG, var3);
            } catch (RuntimeException var4) {
                Log.w(TAG, "Unexpected error initializing camera", var4);
            }

        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.scan();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if(holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }

        if(!this.hasSurface) {
            this.hasSurface = true;
            this.initCamera(holder);
        }

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hasSurface = false;
        this.cameraManager.closeDriver();
        holder.removeCallback(this);
    }

    public int getBeepResId() {
        return this.beepResId;
    }

    public void setBeepResId(int beepResId) {
        this.beepResId = beepResId;
    }

    public interface BarcodeScanListener {
        void scanSuccess(Result var1, Bitmap var2);
    }

    public static enum ScannerWay {
        DEFAULT,
        VIBRATE,
        SOUND,
        VS;

        private ScannerWay() {
        }
    }
}

