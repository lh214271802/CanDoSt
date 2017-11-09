package com.lh.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.blankj.utilcode.util.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/19
 *     desc  : 相机相关工具类
 * </pre>
 */
public class CameraUtils {
    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;

    public static String getCameraPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";// filePath:/sdcard/
    }

    public static String getSaveImageFullName() {
        return "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";// 照片命名
    }

    public static String getSaveMovieFullName() {
        return "MOVIE_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";// 照片命名
    }

    /**
     * 通过Uri获取文件路径
     * 支持图片媒体,文件等
     * <p/>
     * Author qiujuer@live.cn
     *
     * @param uri     Uri
     * @param context Context
     * @return 文件路径
     */
    @SuppressLint({"NewApi", "Recycle"})
    public static String getImagePath(Uri uri, Context context) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            String authority = uri.getAuthority();
            if ("com.android.externalstorage.documents".equals(authority)) {
                // isExternalStorageDocument
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                // isDownloadsDocument
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if ("com.android.providers.media.documents".equals(authority)) {
                // isMediaDocument
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    static Bitmap bitmap = null;

    /**
     * 2014年8月13日
     *
     * @param uri
     * @param context E-mail:mr.huangwenwei@gmail.com
     */
    public static Bitmap loadPicasaImageFromGalley(final Uri uri,
                                                   final Activity context) {

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int columIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columIndex != -1) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            bitmap = android.provider.MediaStore.Images.Media
                                    .getBitmap(context.getContentResolver(),
                                            uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
            cursor.close();
            return bitmap;
        } else
            return null;
    }

    /**
     * 获取图片缩略图 只有Android2.1以上版本支持
     *
     * @param imgName
     * @param kind    MediaStore.Images.Thumbnails.MICRO_KIND
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap loadImgThumbnail(Activity context, String imgName,
                                          int kind) {
        Bitmap bitmap = null;

        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = context.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj,
                MediaStore.Images.Media.DISPLAY_NAME + "='" + imgName + "'",
                null, null);

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ContentResolver crThumb = context.getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = MethodsCompat.getThumbnail(crThumb, cursor.getInt(0),
                    kind, options);
        }
        return bitmap;
    }

    public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
        Bitmap bitmap = ImageUtils.getBitmap(filePath);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 放大缩小图片
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                    true);
        }
        return newbmp;
    }

    /**
     * 创建缩略图
     *
     * @param context
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     */
    public static void createImageThumbnail(Context context,
                                            String largeImagePath, String thumbfilePath, int square_size,
                                            int quality) throws IOException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

        if (cur_bitmap == null)
            return;

        // 原始图片的高宽
        int[] cur_img_size = new int[]{cur_bitmap.getWidth(),
                cur_bitmap.getHeight()};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);
        // 生成缩放后的bitmap
        Bitmap thb_bitmap = zoomBitmap(cur_bitmap, new_img_size[0],
                new_img_size[1]);
        // 生成缩放后的图片文件
        saveImageToSD(null, thumbfilePath, thb_bitmap, quality);
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(Context ctx, String filePath,
                                     Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0,
                    filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(filePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }
    public static Bitmap getBitmapByPath(String filePath,
                                         BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 计算缩放图片的宽高
     *
     * @param img_size
     * @param square_size
     * @return
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size)
            return img_size;
        double ratio = square_size
                / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio),
                (int) (img_size[1] * ratio)};
    }

//    private CameraUtils() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
//    }
//
//    /**
//     * 获取打开照程序界面的Intent
//     */
//    public static Intent getOpenCameraIntent() {
//        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//    }
//
//    /**
//     * 获取跳转至相册选择界面的Intent
//     */
//    public static Intent getImagePickerIntent() {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        return intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "file/*");
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
//     */
//    public static Intent getImagePickerIntent(int outputX, int outputY, Uri fromFileURI,
//                                              Uri saveFileURI) {
//        return getImagePickerIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
//     */
//    public static Intent getImagePickerIntent(int aspectX, int aspectY, int outputX, int outputY, Uri fromFileURI,
//                                              Uri saveFileURI) {
//        return getImagePickerIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，可以指定是否缩放裁剪区域]的Intent
//     *
//     * @param aspectX     裁剪框尺寸比例X
//     * @param aspectY     裁剪框尺寸比例Y
//     * @param outputX     输出尺寸宽度
//     * @param outputY     输出尺寸高度
//     * @param canScale    是否可缩放
//     * @param fromFileURI 文件来源路径URI
//     * @param saveFileURI 输出文件路径URI
//     */
//    public static Intent getImagePickerIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
//                                              Uri fromFileURI, Uri saveFileURI) {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setDataAndType(fromFileURI, "file/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
//        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", canScale);
//        // 图片剪裁不足黑边解决
//        intent.putExtra("scaleUpIfNeeded", true);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        // 去除人脸识别
//        return intent.putExtra("noFaceDetection", true);
//    }
//
//    /**
//     * 获取[跳转至相册选择界面,并跳转至裁剪界面，默认可缩放裁剪区域]的Intent
//     */
//    public static Intent getCameraIntent(Uri saveFileURI) {
//        Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        return mIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
//    }
//
//    /**
//     * 获取[跳转至裁剪界面,默认可缩放]的Intent
//     */
//    public static Intent getCropImageIntent(int outputX, int outputY, Uri fromFileURI,
//                                            Uri saveFileURI) {
//        return getCropImageIntent(1, 1, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
//    /**
//     * 获取[跳转至裁剪界面,默认可缩放]的Intent
//     */
//    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, Uri fromFileURI,
//                                            Uri saveFileURI) {
//        return getCropImageIntent(aspectX, aspectY, outputX, outputY, true, fromFileURI, saveFileURI);
//    }
//
//
//    /**
//     * 获取[跳转至裁剪界面]的Intent
//     */
//    public static Intent getCropImageIntent(int aspectX, int aspectY, int outputX, int outputY, boolean canScale,
//                                            Uri fromFileURI, Uri saveFileURI) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(fromFileURI, "file/*");
//        intent.putExtra("crop", "true");
//        // X方向上的比例
//        intent.putExtra("aspectX", aspectX <= 0 ? 1 : aspectX);
//        // Y方向上的比例
//        intent.putExtra("aspectY", aspectY <= 0 ? 1 : aspectY);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("scale", canScale);
//        // 图片剪裁不足黑边解决
//        intent.putExtra("scaleUpIfNeeded", true);
//        intent.putExtra("return-data", false);
//        // 需要将读取的文件路径和裁剪写入的路径区分，否则会造成文件0byte
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveFileURI);
//        // true-->返回数据类型可以设置为Bitmap，但是不能传输太大，截大图用URI，小图用Bitmap或者全部使用URI
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        // 取消人脸识别功能
//        intent.putExtra("noFaceDetection", true);
//        return intent;
//    }
//
//    /**
//     * 获得选中相册的图片
//     *
//     * @param context 上下文
//     * @param data    onActivityResult返回的Intent
//     * @return bitmap
//     */
//    public static Bitmap getChoosedImage(Activity context, Intent data) {
//        if (data == null) return null;
//        Bitmap bm = null;
//        ContentResolver cr = context.getContentResolver();
//        Uri originalUri = data.getData();
//        try {
//            bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return bm;
//    }
//
//    /**
//     * 获得选中相册的图片路径
//     *
//     * @param context 上下文
//     * @param data    onActivityResult返回的Intent
//     * @return
//     */
//    public static String getChoosedImagePath(Activity context, Intent data) {
//        if (data == null) return null;
//        String path = "";
//        ContentResolver resolver = context.getContentResolver();
//        Uri originalUri = data.getData();
//        if (null == originalUri) return null;
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = resolver.query(originalUri, projection, null, null, null);
//        if (null != cursor) {
//            try {
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                path = cursor.getString(column_index);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (!cursor.isClosed()) {
//                        cursor.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return StringUtils.isEmpty(path) ? originalUri.getPath() : null;
//    }
//
//    /**
//     * 获取拍照之后的照片文件（JPG格式）
//     *
//     * @param data     onActivityResult回调返回的数据
//     * @param filePath 文件路径
//     * @return 文件
//     */
//    public static File getTakePictureFile(Intent data, String filePath) {
//        if (data == null) return null;
//        Bundle extras = data.getExtras();
//        if (extras == null) return null;
//        Bitmap photo = extras.getParcelable("data");
//        File file = new File(filePath);
//        if (ImageUtils.save(photo, file, Bitmap.CompressFormat.JPEG)) return file;
//        return null;
//    }
}
