package com.lzy.androidlibrary.view.imageindicator;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.lzy.androidlibrary.util.LImageLoaderUtil;
import com.lzy.androidlibrary.view.photoview.PhotoView;

import java.util.List;

/**
 * 加载网络图片指示器
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/21
 * @desc
 */
public class NetworkImageIndicatorView extends ImageIndicatorView {

    public NetworkImageIndicatorView(Context context) {
        super(context);
    }

    public NetworkImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置数据源
     * @param urlList 数据源
     * @param gesture 是否支持手势放大
     */
    @Override
    public void setupLayoutByImageUrl(List<String> urlList, boolean gesture) {
        if (urlList == null) {
            throw new NullPointerException();
        }

        for (String imgUrl : urlList) {
            ImageView pageItem = null;
            if (gesture) {
                pageItem = new PhotoView(getContext());
            } else {
                pageItem = new ImageView(getContext());
            }
            if (!TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("/")) {
                imgUrl = "file://" + imgUrl;
            }
            LImageLoaderUtil.displayImage(imgUrl, pageItem);
            addViewItem(pageItem);
        }
    }
}
