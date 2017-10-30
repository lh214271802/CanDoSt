package com.weilaimoshu.ui.common.video;

/**
 * 图片选择器建立契约关系，将权限操作放在Activity，具体数据放在Fragment
 * Created by huanghaibin_dev
 * on 2016/7/15.
 */
public interface SelectVideoContract {
    interface Operator {
        void requestCamera();

        void requestExternalStorage();

        void onBack();

        void setDataView(View view);
    }

    interface View {

        void onOpenCameraSuccess();

        void onCameraPermissionDenied();
    }
}
