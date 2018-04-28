package com.lzy.androidlibrary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * fragment基类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @time 2017/7/19
 * @desc
 */
public abstract class LBaseFragment extends Fragment {

    public Context context;

    private String title;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void gotoActivity(Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    public void gotoActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
