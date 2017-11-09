package com.lh.api.dowload;

import java.text.DecimalFormat;

/**
 * Created by liaohui on 2017/9/14.
 */

public class SizeFormatUtils {

    /**
     * @param var0 文件的字节数
     * @return 文件的大小
     */
    public static String getDataSize(long var0) {
        DecimalFormat var2 = new DecimalFormat("###.00");
        return var0 < 1024L ? var0 + "bytes" : (var0 < 1048576L ? var2.format((double) ((float) var0 / 1024.0F))
                + "KB" : (var0 < 1073741824L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F))
                + "MB" : (var0 < 0L ? var2.format((double) ((float) var0 / 1024.0F / 1024.0F / 1024.0F))
                + "GB" : "error")));
    }
}
