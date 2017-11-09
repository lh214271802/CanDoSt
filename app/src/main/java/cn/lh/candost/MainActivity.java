package cn.lh.candost;

import android.view.View;

import com.lh.base.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected View getToolbarLayout() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        showProgress(true);
    }

    @Override
    protected void initDatas() {
//        SelectImageActivity.show(this,
//                new SelectOptions.Builder()
//                        .setHasCam(true)
//                        .setSelectCount(4)
//                        .setCallback(new SelectOptions.Callback() {
//                            @Override
//                            public void doSelected(List<String> videos) {
//
//                            }
//                        })
//                        .build());
    }
}
