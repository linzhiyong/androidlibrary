package com.lzy.androidlibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 上拉加载ListView实现类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/31
 * @desc
 */
public class LPullUpRefreshListView extends ListView {

    private OnPullUpListener listener;

    private TextView loadView;

    public LPullUpRefreshListView(Context context) {
        super(context);
        init();
    }

    public LPullUpRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LPullUpRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        loadView = new TextView(getContext());
        loadView.setText("正在加载...");
        loadView.setGravity(Gravity.CENTER);
        loadView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        loadView.setTextColor(Color.BLACK);
        loadView.setPadding(0, 5, 0, 5);
        loadView.setVisibility(GONE);
        addFooterView(loadView);
        // 去掉底部分割线
        setFooterDividersEnabled(false);

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时 并且判断是否滚动到底部
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
                        view.getLastVisiblePosition() == view.getCount() - 1) { //  -1 底部加载条
                    //加载更多功能的代码
                    handler.removeCallbacks(closeRefreshRunable);
                    loadView.setVisibility(VISIBLE);
                    if (listener != null) {
                        listener.onRefresh();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void setOnPullUpListener(OnPullUpListener listener) {
        this.listener = listener;
    }

    /**
     * 停止刷新
     */
    public void closeRefresh() {
        handler.postDelayed(closeRefreshRunable, 200);
    }

    private Handler handler = new Handler();
    private Runnable closeRefreshRunable = new Runnable() {
        @Override
        public void run() {
            if (loadView != null) {
                loadView.setVisibility(GONE);
            }
        }
    };

    public interface OnPullUpListener {
        void onRefresh();
    }

}
