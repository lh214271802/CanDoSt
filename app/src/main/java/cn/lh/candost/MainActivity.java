package cn.lh.candost;

import android.view.View;

import com.lh.base.activity.BaseActivity;
import com.lh.ui.common.image.folder.SelectImageActivity;
import com.lh.ui.common.image.folder.SelectOptions;

import java.util.List;

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
        findViewById(R.id.hello_world).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImageActivity.show(mContext,
                        new SelectOptions.Builder()
                                .setHasCam(true)
                                .setSelectCount(4)
                                .setCallback(new SelectOptions.Callback() {
                                    @Override
                                    public void doSelected(List<String> videos) {

                                    }
                                })
                                .build());
            }
        });
    }

    @Override
    protected void initDatas() {
    }
}
