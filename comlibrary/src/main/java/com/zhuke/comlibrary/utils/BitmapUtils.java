package com.huarong.wpd.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by 15653 on 2018/6/26.
 */

public class BitmapUtils {
        private static String TAG = "BitmapUtils";
    /**
     * 采样率压缩，按照图片宽高自动计算缩放比
     *
     * @param filePath    文件路径
     * @param reqHeight   设置宽高并不是设置图片实际宽高，而是根据宽高自动计算缩放比，压缩后图片不会变形，宽高会根据计算的缩放比同时缩放，
     *                    宽高建议都设置300   设置300后图片大小为100-200KB，图片质量能接受；设置为400到500，图片大小为500-600kb，上传偏大，可自行设置
     * @param reqWidth
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqHeight, int reqWidth) {

        //读取Bitmap文件
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //计算图片的缩放值
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     *这个可以压缩到指定的宽，但是图片大小可能达不到预期，图片本身较小的可以使用，图片较大的建议使用上一个压缩方式
     * 根据自定义宽度设置图片大小，高度自适应  0不压缩
     *
     * @param path
     * @param width
     * @return
     */
    public static Bitmap createScaledBitemap(String path, int width) {
        Bitmap bit = BitmapFactory.decodeFile(path);
        int bitWidth = bit.getWidth();//得到图片宽
        float scaleWidth = ((float) width) / ((float) bitWidth);//计算宽度缩放比例
        if (width == 0) {
            return bit;
        } else {
            int height = (int) (bit.getHeight() * scaleWidth);//根据宽度缩放比例设置高度
            Bitmap bitmap = Bitmap.createScaledBitmap(bit, width, height, true);
            return bitmap;
        }
    }

    /**
     *这是个保存Bitmap到sd卡中的方法，可以返回保存图片的路径
     * 保存Bitmap到sd
     *
     * @param mBitmap
     * @param bitName 图片保存的名称，返回存储图片的路径
     */
    public static String saveBitmap(Bitmap mBitmap, String bitName) {
        File f;
        //判断是否有sd卡 有就保存到sd卡，没有就保存到app缓存目录
        if (isStorage()) {
            File file = new File("/data/data/name");//保存的路径
            if (!file.exists()) {//判断目录是否存在
                file.mkdir();//不存在就创建目录
            }
            f = new File(file, bitName + ".jpg");
        } else {
            //TODO:给为自己的全局context
//            File file = new File(AppContext.getContext().getCacheDir().toString());
            File file = null;
            if (!file.exists()) {//判断目录是否存在
                file.mkdir();
            }
            f = new File(file, bitName + ".jpg");
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fOut != null) {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f.toString();
    }
    /**
     * 判断是否有sd卡
     *
     * @return
     */
    public static boolean isStorage() {
        boolean isstorage = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return isstorage;
    }

    /**
     *把Bimtmap转成Base64，用于上传图片到服务器，一般是先压缩然后转成Base64，再上传
     */
    public static String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 把Base64转换成Bitmap
    public static Bitmap getBitmapFromBase64(String iconBase64) {
        byte[] bitmapArray = Base64.decode(iconBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 将控件转换成bitmap类型
     * @return Bitmap
     * @param paramView ：需要转换的控件
     */
    public static Bitmap convertViewToBitmap(View paramView) {
        paramView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        paramView.layout(0, 0, paramView.getMeasuredWidth(),
                paramView.getMeasuredHeight());
        paramView.buildDrawingCache();
        return paramView.getDrawingCache();
    }

    /**
     * 创建倒影图片
     * @return Bitmap
     * @param srcBitmap
     *            源图片的bitmap
     * @param reflectionHeight
     *            图片倒影的高度
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap,
                                               int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 0;

        //获取源图片的宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        if (0 == srcWidth || srcHeight == 0) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {

            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight - reflectionHeight, srcWidth, reflectionHeight,
                    matrix, false);

            if (null == reflectionBitmap) {
                Log.e(TAG, "Create the reflectionBitmap is failed");
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(srcWidth,
                    srcHeight + reflectionHeight, Bitmap.Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));

            canvas.save();
            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
            if (reflectionBitmap != null && !reflectionBitmap.isRecycled()) {
                reflectionBitmap.recycle();
                reflectionBitmap = null;
            }

            canvas.restore();

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Create the reflectionBitmap is failed");
        return null;
    }

    /**
     * 图片圆角处理
     * @return Bitmap
     * @param srcBitmap
     *            源图片的bitmap
     * @param ret
     *            圆角的度数
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }

    /**
     * 图片沿着Y轴旋转一定角度
     * @param srcBitmap
     *            源图片的bitmap
     * @param reflectionHeight
     *            图片倒影的高度
     * @param rotate
     *            图片旋转的角度
     */
    public static Bitmap skewImage(Bitmap srcBitmap, float rotate,
                                   int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        Bitmap reflecteBitmap = createReflectedBitmap(srcBitmap,
                reflectionHeight);

        if (null == reflecteBitmap) {
            Log.e(TAG, "failed to createReflectedBitmap");
            return null;
        }

        int wBitmap = reflecteBitmap.getWidth();
        int hBitmap = reflecteBitmap.getHeight();
        float scaleWidth = ((float) 180) / wBitmap;
        float scaleHeight = ((float) 270) / hBitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        reflecteBitmap = Bitmap.createBitmap(reflecteBitmap, 0, 0, wBitmap,
                hBitmap, matrix, true);
        Camera localCamera = new Camera();
        localCamera.save();
        Matrix localMatrix = new Matrix();
        localCamera.rotateY(rotate);
        localCamera.getMatrix(localMatrix);
        localCamera.restore();
        localMatrix.preTranslate(-reflecteBitmap.getWidth() >> 1,
                -reflecteBitmap.getHeight() >> 1);
        Bitmap localBitmap2 = Bitmap.createBitmap(reflecteBitmap, 0, 0,
                reflecteBitmap.getWidth(), reflecteBitmap.getHeight(),
                localMatrix, true);
        Bitmap localBitmap3 = Bitmap.createBitmap(localBitmap2.getWidth(),
                localBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap3);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
        if (null != reflecteBitmap && !reflecteBitmap.isRecycled()) {
            reflecteBitmap.recycle();
            reflecteBitmap = null;
        }
        if (null != localBitmap2 && !localBitmap2.isRecycled()) {
            localBitmap2.recycle();
            localBitmap2 = null;
        }
        localCanvas.save();
        localCanvas.restore();
        return localBitmap3;
    }

    /**
     *图片模糊化处理
     * @return Bitmap
     * @param bitmap
     *            源图片
     * @param radius
     *            模糊半径，范围为0~25
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context) {

        //创建与原Bitmap同等大小的Bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 创建一个渲染脚本
        RenderScript rs = RenderScript.create(context);

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
        }

        //分配大小
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设置模糊半径
        if (radius > 25) {
            radius = 25.0f;
        } else if (radius <= 0) {
            radius = 1.0f;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurScript.setRadius(radius);
        }

        // 执行渲染
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // 复制最终bitmap
        allOut.copyTo(outBitmap);

        // 回收Bitmap资源
        bitmap.recycle();
        bitmap = null;
        // 销毁渲染脚本
        rs.destroy();

        return outBitmap;

    }

    /**
     * TODO<给图片添加指定颜色的边框>
     *
     * @param srcBitmap
     *            原图片
     * @param borderWidth
     *            边框宽度
     * @param color
     *            边框的颜色值
     * @return
     */
    public static Bitmap addFrameBitmap(Bitmap srcBitmap, int borderWidth,
                                        int color) {
        if (srcBitmap == null) {
            Log.e(TAG, "the srcBitmap or borderBitmap is null");
            return null;
        }

        int newWidth = srcBitmap.getWidth() + borderWidth;
        int newHeight = srcBitmap.getHeight() + borderWidth;

        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(outBitmap);

        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        // 设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);

        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        if (srcBitmap != null && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();
            srcBitmap = null;
        }

        return outBitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 先将inJustDecodeBounds属性设置为true,解码避免内存分配
        options.inJustDecodeBounds = true;
        // 将图片传入选择器中
        BitmapFactory.decodeResource(res, resId, options);
        // 对图片进行指定比例的压缩
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 待图片处理完成后再进行内存的分配，避免内存泄露的发生
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // 计算图片的压缩比例
    public static int calculateInSampleSize(BitmapFactory.Options option,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = option.outHeight;
        final int width = option.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择长宽高较小的比例，成为压缩比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 创建指定大小Bitmap
     * @param source : 源Bitmap
     * @param width ： 宽度
     * @param height ： 高度
     */
    public static Bitmap Thumbnail(Bitmap bm, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        bmp = ThumbnailUtils.extractThumbnail(bm, reqWidth, reqHeight);
        return bmp;
    }

    /**
     * Creates a centered bitmap of the desired size.
     *
     * @param source
     *            original bitmap source
     * @param width
     *            targeted width
     * @param height
     *            targeted height
     * @param options
     *            options used during thumbnail extraction
     */
    public static Bitmap Thumbnail(Bitmap bm, int reqWidth, int reqHeight,
                                   int options) {
        Bitmap bmp = null;
        bmp = ThumbnailUtils.extractThumbnail(bm, reqWidth, reqHeight, options);
        return bmp;
    }

    /**
     * 创建文件视频的缩略图
     *
     * @param filePath ： 视频文件路径
     *
     * @param kind ： MINI_KIND 或 MICRO_KIND
     *
     */
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
        return ThumbnailUtils.createVideoThumbnail(filePath, kind);
    }

    //创建文件图片的缩略图
    /**
     * This method first examines if the thumbnail embedded in EXIF is bigger
     * than our target size. If not, then it'll create a thumbnail from original
     * image. Due to efficiency consideration, we want to let MediaThumbRequest
     * avoid calling this method twice for both kinds, so it only requests for
     * MICRO_KIND and set saveImage to true. This method always returns a
     * "square thumbnail" for MICRO_KIND thumbnail.
     *
     * @param filePath
     *            the path of image file
     * @param kind
     *            could be Images.Thumbnails.MINI_KIND or
     *            Images.Thumbnails.MINI_KIND
     * @return Bitmap, or null on failures
     * @hide This method is only used by media framework and media provider
     *       internally.
     */
    /*public static Bitmap createImageThumbnail(String filePath, int kind) {
        return ThumbnailUtils.createImageThumbnail(filePath, kind);
    }*/

    /**
     * 屏幕分辨率和指定清晰度的图片压缩方法
     *
     * @param context
     * @param image   Bitmap图片
     * @return
     */
    public static Bitmap comp(Context context, Bitmap image) {
        int maxLength = 1024 * 1024; // 预定的图片最大内存，单位byte
        // 压缩大小
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > maxLength) { // 循环判断，大于继续压缩
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//PNG 压缩options%
        }
        // 压缩尺寸
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options opts = new BitmapFactory.Options(); // 选项对象(在加载图片时使用)
        opts.inJustDecodeBounds = true; // 修改选项, 只获取大小
        BitmapFactory.decodeStream(bais, null, opts);// 加载图片(只得到图片大小)
        // 获取屏幕大小，按比例压缩
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int scaleX = opts.outWidth / manager.getDefaultDisplay().getWidth(); // X轴缩放比例(图片宽度/屏幕宽度)
        int scaleY = opts.outHeight / manager.getDefaultDisplay().getHeight(); // Y轴缩放比例
        int scale = scaleX > scaleY ? scaleX : scaleY; // 图片的缩放比例(X和Y哪个大选哪个)

        opts.inJustDecodeBounds = false; // 修改选项, 不只解码边界
        opts.inSampleSize = scale > 1 ? scale : 1; // 修改选项, 加载图片时的缩放比例
        return BitmapFactory.decodeStream(bais, null, opts); // 加载图片(得到压缩后的图片)
    }

    /**
     * 屏幕分辨率和指定清晰度的图片压缩方法
     *
     * @param context
     * @param path    图片的路径
     * @return
     */
    public static Bitmap comp(Context context, String path) {
        return compressImage(getUsableImage(context, path));
    }

    /**
     * 获取屏幕分辨率的Bitmap
     *
     * @param context
     * @param path    图片的路径
     * @return
     */
    public static Bitmap getUsableImage(Context context, String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options(); // 选项对象(在加载图片时使用)
        opts.inJustDecodeBounds = true; // 修改选项, 只获取大小
        BitmapFactory.decodeFile(path, opts); // 加载图片(只得到图片大小)
        DisplayMetrics metrics = new DisplayMetrics();
        metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int scaleX = opts.outWidth / metrics.widthPixels; // X轴缩放比例(图片宽度/屏幕宽度)
        int scaleY = opts.outHeight / metrics.heightPixels; // Y轴缩放比例
        int scale = scaleX > scaleY ? scaleX : scaleY; // 图片的缩放比例(X和Y哪个大选哪个)

        opts.inJustDecodeBounds = false; // 修改选项, 不只解码边界
        opts.inSampleSize = scale > 1 ? scale : 1; // 修改选项, 加载图片时的缩放比例
        return BitmapFactory.decodeFile(path, opts); // 加载图片(得到缩放后的图片)
    }

    /**
     * 压缩图片清晰度，到指定大小
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        int maxLength = 1024 * 1024; // (byte)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > maxLength) { // 循环判断如果压缩后图片是否大于1mb,大于继续压缩
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 指定分辨率和清晰度的图片压缩方法
     *
     * @param fromFile
     * @param toFile
     * @param reqWidth
     * @param reqHeight
     * @param quality
     */
    public static void transImage(String fromFile, String toFile, int reqWidth, int reqHeight, int quality) {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // 缩放的尺寸
        float scaleWidth = (float) reqWidth / bitmapWidth;
        float scaleHeight = (float) reqHeight / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
        // 保存到文件
        bitmap2File(toFile, quality, resizeBitmap);
        if (!bitmap.isRecycled()) {
            // 释放资源，以防止OOM
            bitmap.recycle();
        }
        if (!resizeBitmap.isRecycled()) {
            resizeBitmap.recycle();
        }
    }


    /**
     * Bitmap转换为文件
     *
     * @param toFile
     * @param quality
     * @param bitmap
     * @return
     */
    public static File bitmap2File(String toFile, int quality, Bitmap bitmap) {
        File captureFile = new File(toFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(captureFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return captureFile;
    }

    /**
     * Drawable转换为Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap、Drawable、InputStream、byte[] 之间转换
    // Bitmap 转 InputStream
    public static InputStream bitmap2Input(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static InputStream bitmap2Input(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    //  Bitmap 转 byte[]
    public static byte[] bitmap2ByteArray(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    public static byte[] bitmap2ByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //  Drawable 转 byte[]
    public static byte[] drawable2ByteArray(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        return out.toByteArray();
    }

    //  byte[] 转 Bitmap
    public static Bitmap byteArray2Bitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}