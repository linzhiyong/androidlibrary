package com.lzy.androidlibrary.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.lzy.androidlibrary.R;
import com.lzy.androidlibrary.util.LFileUtil;
import com.lzy.androidlibrary.util.LPermission;
import com.lzy.androidlibrary.util.LToastUtil;
import com.lzy.androidlibrary.util.LoggerUtil;
import com.lzy.androidlibrary.util.annotation.OnLPermissionDenied;
import com.lzy.androidlibrary.util.annotation.OnLPermissionGranted;
import com.lzy.androidlibrary.util.annotation.OnLPermissionNeverAskAgain;

import java.io.File;

/**
 * 相机拍照、图库选择照片，返回获取的图片地址【imgPath】
 * <p>
 * 需要访问摄像头/读写存储设备权限
 *
 * @author linzhiyong
 * @time 2015年7月31日21:22:53
 * @email wflinzhiyong@163.com
 */
public class LTakePicActivity extends AppCompatActivity {

    /** 是否显示图库选择按钮 */
    public static final String SHOW_SELECT_PHOTO = "show_select_photo";

    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_LOCAL = 2;

    public static final String TAKEPIC_RESULT = "imagePath";

    private File cameraFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_takepic);
        findViewById(R.id.takepic_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicFromCamera();
            }
        });
        findViewById(R.id.takepic_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicFromPhoto();
            }
        });
        findViewById(R.id.takepic_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        if (getIntent().getBooleanExtra(SHOW_SELECT_PHOTO, true)) {
            findViewById(R.id.takepic_photo_layout).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.takepic_photo_layout).setVisibility(View.GONE);
        }
    }

    /**
     * 相机拍照-按钮点击事件
     */
    public void takepicFromCamera() {
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        LPermission.with(this).
                setRequestCode(REQUEST_CODE_CAMERA)
                .permissions(permissions)
                .request();
    }

    /**
     * 相册选取-按钮点击事件
     */
    public void takepicFromPhoto() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        LPermission.with(this).
                setRequestCode(REQUEST_CODE_LOCAL)
                .permissions(permissions)
                .request();
    }

    /**
     * 取消-按钮点击事件
     */
    public void cancel() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnLPermissionGranted(REQUEST_CODE_CAMERA)
    void success_camera() {
        selectPicFromCamera();
    }

    @OnLPermissionDenied(REQUEST_CODE_CAMERA)
    @OnLPermissionNeverAskAgain(REQUEST_CODE_CAMERA)
    void error_camera() {
        showToast("相机或读写权限授权失败！");
    }

    @OnLPermissionGranted(REQUEST_CODE_LOCAL)
    void success() {
        selectPicFromLocal();
    }

    @OnLPermissionDenied(REQUEST_CODE_LOCAL)
    @OnLPermissionNeverAskAgain(REQUEST_CODE_LOCAL)
    void error() {
        showToast("读写权限授权失败！");
    }

    /**
     * 相机获取图片
     */
    public void selectPicFromCamera() {
        if (!LFileUtil.checkSdCard()) {
            LToastUtil.showShortToast(this, "SD卡不存在，不能拍照！");
            return;
        }
        String rootPath = null;
        try {
            rootPath = getExternalFilesDir(null).getAbsolutePath() + File.separator + "image";
        } catch (Exception e) {
            rootPath = getCacheDir().getAbsolutePath() + File.separator + "image";
        }
        cameraFile = new File(rootPath, "IMG_" + System.currentTimeMillis() + ".jpg");
        if (!cameraFile.getParentFile().exists()) {
            cameraFile.getParentFile().mkdirs();
        }
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)), REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE_LOCAL) { // 获取并发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                } else {
                    finish();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists()) {
                    sendPicToPreActivity(cameraFile.getAbsolutePath());
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } catch (Exception e) {
            showToast("获取图片出现错误：" + e.getMessage());
            LoggerUtil.error(getClass().getName(), e.getMessage(), e);
            finish();
        }
    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage
     */
    private void sendPicByUri(Uri selectedImage) throws Exception {
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
        String picturePath;
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
        } else {
            File file = new File(selectedImage.getPath());
            picturePath = file.getAbsolutePath();
        }
        sendPicToPreActivity(picturePath);
    }

    /**
     * 将照片地址返回上一个界面
     *
     * @param imgPath
     */
    private void sendPicToPreActivity(String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            Intent intent = new Intent();
            intent.putExtra(TAKEPIC_RESULT, imgPath);
            setResult(Activity.RESULT_OK, intent);
        } else {
            showToast("没有找到图片！");
        }
        finish();
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
