package cn.lh.candost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lh.ui.common.image.folder.SelectImageActivity;
import com.lh.ui.common.image.folder.SelectOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SelectImageActivity.show(this,
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
}
