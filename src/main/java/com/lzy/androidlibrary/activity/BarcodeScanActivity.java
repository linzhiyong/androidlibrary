package com.lzy.androidlibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.android.BarcodeScaner;
import com.google.zxing.client.android.BarcodeScaner.BarcodeScanListener;
import com.lzy.androidlibrary.R;
import com.lzy.androidlibrary.manager.LFlashLightManager;
import com.lzy.androidlibrary.util.LBitmapUtil;
import com.lzy.androidlibrary.util.LPermission;
import com.lzy.androidlibrary.util.LUtils;
import com.lzy.androidlibrary.util.annotation.OnLPermissionDenied;
import com.lzy.androidlibrary.util.annotation.OnLPermissionGranted;
import com.lzy.androidlibrary.util.annotation.OnLPermissionNeverAskAgain;

import java.io.File;

/**
 * 条码扫描界面
 * <p>
 * 需要访问摄像头/读写存储设备/播放声音权限
 *
 * @author linzhiyong
 * @time 2016年11月16日17:21:46
 * @email wflinzhiyong@163.com
 * @desc
 */
public class BarcodeScanActivity extends AppCompatActivity implements BarcodeScanListener {

    private static final int REQUEST_CODE_CAMERA = 0x01;

    public static final String TITLE = "title";
    public static final String SHOW_LIGHT = "show_light";
    public static final String CONTENT = "content";
    public static final String THUMB_PATH = "thumb_path";

    private LFlashLightManager flashLightManager;

    public BarcodeScaner getBarcodeScaner() {
        return barcodeScaner;
    }

    private BarcodeScaner barcodeScaner;

    private MenuItem menuItem;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        LPermission.with(this)
                .permissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .setRequestCode(REQUEST_CODE_CAMERA)
                .request();
    }

    private void initScanView() {
        barcodeScaner = new BarcodeScaner(this, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        barcodeScaner.setLayoutParams(params);
        barcodeScaner.setBeepResId(R.raw.beep);
        barcodeScaner.setListener(this);
        barcodeScaner.setScannerWay(BarcodeScaner.ScannerWay.VIBRATE);

        setContentView(barcodeScaner);

        flashLightManager = new LFlashLightManager(this);
        barcodeScaner.open();
        barcodeScaner.scan();
    }

    @OnLPermissionGranted(REQUEST_CODE_CAMERA)
    void success_camera() {
        initScanView();
    }

    @OnLPermissionDenied(REQUEST_CODE_CAMERA)
    @OnLPermissionNeverAskAgain(REQUEST_CODE_CAMERA)
    void error_camera() {
        Toast.makeText(this, "相机权限或读写权限授权失败！", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.scan));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScaner != null) {
            barcodeScaner.scan();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getBooleanExtra(SHOW_LIGHT, false)) {
            getMenuInflater().inflate(R.menu.scan_menu, menu);
            menuItem = menu.findItem(R.id.openLight);
            menuItem.setTitle("开灯");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.openLight) {
            if (barcodeScaner == null) {
                menuItem.setVisible(false);
                return false;
            }
            if (LUtils.isFastDoubleClick(200)) {
                return true;
            }
            if (camera == null) {
                camera = barcodeScaner.getCameraManager().getCamera();
            }
            if (camera == null) {
                Toast.makeText(this, "开启闪光灯失败！", Toast.LENGTH_LONG).show();
                return true;
            }
            if (flashLightManager.isTurnOnFlash()) {
                flashLightManager.turnLightOffCamera(camera);
                menuItem.setTitle("开灯");
            } else {
                flashLightManager.turnLightOnCamera(camera);
                menuItem.setTitle("关灯");
            }

        } else if (i == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * 识别完成回调
     *
     * @param result
     * @param bitmap
     */
    @Override
    public void scanSuccess(Result result, Bitmap bitmap) {
        String savePath = getIntent().getStringExtra(THUMB_PATH) == null ? getExternalCacheDir().getAbsolutePath() : getIntent().getStringExtra(THUMB_PATH);
        LBitmapUtil.saveBitmap(savePath, "barcode.png", bitmap);
        barcodeScaner.close();
        if (result == null || result.getText() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(CONTENT, result.getText());
        intent.putExtra(THUMB_PATH, savePath + File.separator + "barcode.png");
        setResult(0x11, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
