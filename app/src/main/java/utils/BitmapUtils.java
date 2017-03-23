package utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ThumbnailUtils;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 图片处理工具
 */
public class BitmapUtils {
    /**
     * 图片处理工具 默认构造器
     */
    public BitmapUtils() {
        throw new UnsupportedOperationException("BitmapUtils can't instantiate");
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return {@code true}: 是<br>{@code false}: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * 将Bitmap转换为byte[]
     *
     * @param bmp         图片
     * @param needRecycle 是否需要释放图片资源
     * @return 返回byte[]
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        if (bmp == null)
            return null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            bmp.compress(CompressFormat.PNG, 100, output);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (output == null) {
            return null;
        }
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将byte[]转换成Bitmap
     *
     * @param b
     * @return
     */
    public static Bitmap byteArrayToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(Drawable drawable) {
        return drawable == null ? null : bmpToByteArray(drawable2Bitmap(drawable), true);
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return res == null ? null : bitmap2Drawable(res, byteArrayToBitmap(bytes));
    }

    /**
     * 将Base64编码的图片字符串转化为图片
     *
     * @param string 要转化的字符串内容
     * @return 返回转化结果Bitmap
     */
    public static Bitmap stringtoBitmap(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = null;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // 得到新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, null, true);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 计算采样大小
     *
     * @param options   选项
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return 采样大小
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        if (maxWidth == 0 || maxHeight == 0) return 1;
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((height >>= 1) >= maxHeight && (width >>= 1) >= maxWidth) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    /**
     * 获取bitmap
     *
     * @param file 文件
     * @return bitmap
     */
    public static Bitmap getBitmap(File file) {
        if (file == null || !FileUtils.isFileExist(file.getAbsolutePath())) {
            return null;
        }
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.close(is);
        }
    }

    /**
     * 获取bitmap（会根据设置的maxWidth和maxHeight计算出合适的缩放比例）
     *
     * @param file      文件
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
        if (file == null || !FileUtils.isFileExist(file.getAbsolutePath())) {
            return null;
        }

        return getBitmap(file.getAbsolutePath(), maxWidth, maxHeight);
    }

    /**
     * 获取bitmap
     *
     * @param filePath 文件路径
     * @return bitmap
     */
    public static Bitmap getBitmap(String filePath) {
        if (StringUtils.isEmptyOrBlankSpace(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * 获取bitmap（会根据设置的maxWidth和maxHeight计算出合适的缩放比例）
     *
     * @param filePath  文件路径
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(String filePath, int maxWidth, int maxHeight) {
        if (StringUtils.isEmptyOrBlankSpace(filePath)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 获取bitmap
     *
     * @param res 资源对象
     * @param id  资源id
     * @return bitmap
     */
    public static Bitmap getBitmap(Resources res, int id) {
        if (res == null) return null;
        return BitmapFactory.decodeResource(res, id);
    }

    /**
     * 获取bitmap（会根据设置的maxWidth和maxHeight计算出合适的缩放比例）
     *
     * @param res       资源对象
     * @param id        资源id
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(Resources res, int id, int maxWidth, int maxHeight) {
        if (res == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, id, options);
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context 上下文环境
     * @param resId   资源id
     * @return 转化结果Bitmap
     */
    public static Bitmap getBitmapFromRes(Context context, int resId) {
        InputStream is = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取bitmap
     *
     * @param fd 文件描述
     * @return bitmap
     */
    public static Bitmap getBitmap(FileDescriptor fd) {
        if (fd == null) return null;
        return BitmapFactory.decodeFileDescriptor(fd);
    }

    /**
     * 获取bitmap（会根据设置的maxWidth和maxHeight计算出合适的缩放比例）
     *
     * @param fd        文件描述
     * @param maxWidth  最大宽度
     * @param maxHeight 最大高度
     * @return bitmap
     */
    public static Bitmap getBitmap(FileDescriptor fd, int maxWidth, int maxHeight) {
        if (fd == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 缩放图片
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @return 缩放后的图片
     */
    public static Bitmap scale(Bitmap src, int newWidth, int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    /**
     * 缩放图片
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   是否回收
     * @return 缩放后的图片
     */
    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 缩放图片
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @return 缩放后的图片
     */
    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    /**
     * 缩放图片
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @param recycle     是否回收
     * @return 缩放后的图片
     */
    public static Bitmap scale(Bitmap src, float scaleWidth, float scaleHeight, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Matrix matrix = new Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 裁剪图片
     *
     * @param src    源图片
     * @param x      开始坐标x
     * @param y      开始坐标y
     * @param width  裁剪宽度
     * @param height 裁剪高度
     * @return 裁剪后的图片
     */
    public static Bitmap clip(Bitmap src, int x, int y, int width, int height) {
        return clip(src, x, y, width, height, false);
    }

    /**
     * 裁剪图片
     *
     * @param src     源图片
     * @param x       开始坐标x
     * @param y       开始坐标y
     * @param width   裁剪宽度
     * @param height  裁剪高度
     * @param recycle 是否回收
     * @return 裁剪后的图片
     */
    public static Bitmap clip(Bitmap src, int x, int y, int width, int height, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }

        if (width <= 0 || height <= 0) {
            return null;
        }

        int bmpWidth = src.getWidth();
        int bmpHeight = src.getHeight();

        x = x < 0 ? 0 : (x > bmpWidth ? bmpWidth : x);
        y = y < 0 ? 0 : (y > bmpHeight ? bmpHeight : y);

        int startPosX = x;
        int startPosY = y;
        int clipWidth = width;
        int clipHeight = height;

        if (startPosX + clipWidth > bmpWidth) {
            clipWidth = bmpWidth - startPosX;
        }

        if (startPosY + clipHeight > bmpHeight) {
            clipHeight = bmpHeight - startPosY;
        }

        if (clipWidth <= 0 || clipHeight <= 0) {
            return null;
        }

        Bitmap ret = Bitmap.createBitmap(src, startPosX, startPosY, clipWidth, clipHeight);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 倾斜图片
     *
     * @param src 源图片
     * @param kx  倾斜因子x
     * @param ky  倾斜因子y
     * @return 倾斜后的图片
     */
    public static Bitmap skew(Bitmap src, float kx, float ky) {
        return skew(src, kx, ky, 0, 0, false);
    }

    /**
     * 倾斜图片
     *
     * @param src     源图片
     * @param kx      倾斜因子x
     * @param ky      倾斜因子y
     * @param recycle 是否回收
     * @return 倾斜后的图片
     */
    public static Bitmap skew(Bitmap src, float kx, float ky, boolean recycle) {
        return skew(src, kx, ky, 0, 0, recycle);
    }

    /**
     * 倾斜图片
     *
     * @param src 源图片
     * @param kx  倾斜因子x
     * @param ky  倾斜因子y
     * @param px  平移因子x
     * @param py  平移因子y
     * @return 倾斜后的图片
     */
    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py) {
        return skew(src, kx, ky, px, py, false);
    }

    /**
     * 倾斜图片
     *
     * @param src     源图片
     * @param kx      倾斜因子x
     * @param ky      倾斜因子y
     * @param px      平移因子x
     * @param py      平移因子y
     * @param recycle 是否回收
     * @return 倾斜后的图片
     */
    public static Bitmap skew(Bitmap src, float kx, float ky, float px, float py, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Matrix matrix = new Matrix();
        matrix.setSkew(kx, ky, px, py);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 旋转图片
     *
     * @param src     源图片
     * @param degrees 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotate(Bitmap src, float degrees) {
        return rotate(src, degrees, false);
    }

    /**
     * 旋转图片
     *
     * @param src     源图片
     * @param degrees 旋转角度
     * @param recycle 是否回收
     * @return 旋转后的图片
     */
    public static Bitmap rotate(Bitmap src, float degrees, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        if (degrees == 0) return src;
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 转变图片为圆形图片
     *
     * @param bitmap 要转变的图片
     * @return 生成的圆形图片
     */
    public static Bitmap circleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int bitmapWith = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int minSize = Math.min(bitmapHeight, bitmapWith);

        Bitmap output = Bitmap
                .createBitmap(minSize, minSize, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        final int color = 0xff000000;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        int left = (bitmapWith - minSize) / 2;
        int top = (bitmapHeight - minSize) / 2;
        int right = left + minSize;
        int bottom = top + minSize;

        final Rect srcRect = new Rect(left, top, right, bottom);
        final Rect desRect = new Rect(0, 0, minSize, minSize);
        final int width = bitmap.getWidth();
        canvas.drawCircle(minSize / 2, minSize / 2, minSize / 2 - 1, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, desRect, paint);//将图片绘制成白色图片

        return output;
    }

    /**
     * 转为圆角图片
     *
     * @param src    源图片
     * @param radius 圆角的度数
     * @return 圆角图片
     */
    public static Bitmap toRoundBitmap(Bitmap src, float radius) {
        return toRoundBitmap(src, radius, false);
    }

    /**
     * 转为圆角图片
     *
     * @param src     源图片
     * @param radius  圆角的度数
     * @param recycle 是否回收
     * @return 圆角图片
     */
    public static Bitmap toRoundBitmap(Bitmap src, float radius, boolean recycle) {
        if (null == src) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        Paint paint = new Paint();
        Canvas canvas = new Canvas(ret);
        Rect rect = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(new RectF(rect), radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 添加倒影
     *
     * @param src              源图片的
     * @param reflectionHeight 倒影高度
     * @return 带倒影图片
     */
    public static Bitmap addReflection(Bitmap src, int reflectionHeight) {
        return addReflection(src, reflectionHeight, false);
    }

    /**
     * 添加倒影
     *
     * @param src              源图片的
     * @param reflectionHeight 倒影高度
     * @param recycle          是否回收
     * @return 带倒影图片
     */
    public static Bitmap addReflection(Bitmap src, int reflectionHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }

        if(reflectionHeight <= 0){
            return src;
        }

        // 原图与倒影之间的间距
        final int REFLECTION_GAP = 0;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();

        if(reflectionHeight > srcHeight){
            reflectionHeight = srcHeight;
        }

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionBitmap = Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight,
                srcWidth, reflectionHeight, matrix, false);
        Bitmap ret = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        LinearGradient shader = new LinearGradient(0, srcHeight,
                0, ret.getHeight() + REFLECTION_GAP,
                0x70FFFFFF, 0x00FFFFFF, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, srcHeight + REFLECTION_GAP,
                srcWidth, ret.getHeight(), paint);
        if (!reflectionBitmap.isRecycled()) reflectionBitmap.recycle();
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }

    /**
     * 图片反转
     *
     * @param bmp
     * @param flag 0为水平反转，1为垂直反转
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }

        return null;
    }


    /**
     * 转为灰度图片
     *
     * @param src 源图片
     * @return 灰度图
     */
    public static Bitmap toGray(Bitmap src) {
        return toGray(src, false);
    }

    /**
     * 转为灰度图片
     *
     * @param src     源图片
     * @param recycle 是否回收
     * @return 灰度图
     */
    public static Bitmap toGray(Bitmap src, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap grayBitmap = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(src, 0, 0, paint);
        if (recycle && !src.isRecycled()) src.recycle();
        return grayBitmap;
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format) {
        return save(src, FileUtils.createFile(filePath), format, false);
    }

    /**
     * 保存图片
     *
     * @param src    源图片
     * @param file   要保存到的文件
     * @param format 格式
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * 保存图片
     *
     * @param src      源图片
     * @param filePath 要保存到的文件路径
     * @param format   格式
     * @param recycle  是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, String filePath, CompressFormat format, boolean recycle) {
        return save(src, FileUtils.createFile(filePath), format, recycle);
    }

    /**
     * 保存图片
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean save(Bitmap src, File file, CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src) || file == null) {
            return false;
        }

        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(os);
        }
        return ret;
    }

    /**
     * 将指定的质量压缩图片后，存储到目标路径
     *
     * @param srcFilePath 源图片路径
     * @param desFilePath 压缩后图片保存路径
     * @param format      格式
     * @param quality     取值0...100
     */
    public static void compressByQualityAndSave(String srcFilePath, String desFilePath, CompressFormat format, int quality) throws IOException {
        Bitmap bitmap = getBitmap(srcFilePath);
        if (isEmptyBitmap(bitmap)) {
            return;
        }

        File desFile = new File(desFilePath);
        FileOutputStream fos = new FileOutputStream(desFile);
        bitmap.compress(format, quality, fos);

        fos.flush();
        fos.close();

        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }

    }

    /**
     * 将指定的大小压缩图片后，存储到目标路径
     *
     * @param srcFilePath
     * @param desFilePath
     * @param format      格式
     * @param maxByteSize
     */
    public static void compressBySizeAndSave(String srcFilePath, String desFilePath, CompressFormat format, long maxByteSize) {
        Bitmap bitmap = getBitmap(srcFilePath);
        Bitmap compressBmp = compressBySize(bitmap, format, maxByteSize, true);
        save(compressBmp, desFilePath, format, true);
    }

    /**
     * 按照指定的宽高压缩图片后，存储到目标路径
     *
     * @param srcFilePath
     * @param desFilePath
     * @param width
     * @param height
     */
    public static void compressByDimensionAndSave(String srcFilePath, String desFilePath, int width, int height) {
        Bitmap bitmap = getBitmap(srcFilePath);
        Bitmap thumbnailBmp = getImageThumbnail(bitmap, width, height, true);
        save(thumbnailBmp, desFilePath, CompressFormat.JPEG, true);
    }

    /**
     * 按质量压缩
     * <br>
     * 注意：压缩质量并不改变图片的像素，图片在内存中占用大小也不变
     * @param src     源图片
     * @param format  格式
     * @param quality 质量（取值0...100）
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, CompressFormat format, int quality) {
        return compressByQuality(src, format, quality, false);
    }

    /**
     * 按质量压缩
     * <br>
     * 注意：压缩质量并不改变图片的像素，图片在内存中占用大小也不变
     *
     * @param src     源图片
     * @param format  格式
     * @param quality 质量 (取值0...100)
     * @param recycle 是否回收
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, CompressFormat format, int quality, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(format, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 按大小压缩
     *
     * @param src         源图片
     * @param format      格式
     * @param maxByteSize 允许最大值字节数
     * @return 质量压缩压缩过的图片
     */
    public static Bitmap compressBySize(Bitmap src, CompressFormat format, long maxByteSize) {
        return compressBySize(src, format, maxByteSize, false);
    }

    /**
     * 按大小压缩
     *
     * @param src         源图片
     * @param format      格式
     * @param maxByteSize 允许最大值字节数
     * @param recycle     是否回收
     * @return 质量压缩压缩过的图片
     */
    public static Bitmap compressBySize(Bitmap src, CompressFormat format, long maxByteSize, boolean recycle) {
        if (isEmptyBitmap(src) || maxByteSize <= 0) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(format, 100, baos);
        Bitmap scaleBmp = null;
        if(baos.toByteArray().length > maxByteSize){
            //先进行一次大范围压缩
            float zoom = (float)Math.sqrt(maxByteSize / (float)baos.toByteArray().length); //获取缩放比例
            scaleBmp = BitmapUtils.scale(src, zoom, zoom);
            baos.reset();
            scaleBmp.compress(format, 100, baos);

            int lastLength = -1;
            int length;
            while ((length = baos.toByteArray().length) > maxByteSize) {
                if(length == lastLength){//通过scale无法继续压缩了,通过采样率继续压缩
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    int sampleSize = 1;
                    byte[] bytes = baos.toByteArray();
                    while (bytes.length > maxByteSize){
                        sampleSize <<= 1;
                        options.inSampleSize = sampleSize;
                        scaleBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                        if(scaleBmp.getWidth() == 1 || scaleBmp.getHeight() == 1){
                            break;
                        }
                        baos.reset();
                        scaleBmp.compress(format, 100, baos);
                        bytes = baos.toByteArray();
                    }
                    break;
                }

                lastLength = length;
                scaleBmp = BitmapUtils.scale(scaleBmp, 0.8f, 0.8f);
                baos.reset();
                scaleBmp.compress(format, 100, baos);
            }

        }

        if (recycle && !src.isRecycled()) {
            src.recycle();
        }

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaleBmp;
    }

    /**
     * 转换为JPG格式
     * @param srcPngFile
     * @param desJpgFile
     */
    public static void convertToJpg(String srcPngFile, String desJpgFile) {
        Bitmap pngBitmap = getBitmap(srcPngFile);
        BufferedOutputStream bos = null;
        try {
            if (pngBitmap != null) {
                bos = new BufferedOutputStream(new FileOutputStream(desJpgFile));
                boolean ret = pngBitmap.compress(CompressFormat.JPEG, 100, bos);
                if (ret) {
                    bos.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转换为PNG格式
     * @param srcJpgFile
     * @param desPngFile
     */
    public static void convertToPng(String srcJpgFile, String desPngFile) {
        Bitmap pngBitmap = getBitmap(srcJpgFile);
        BufferedOutputStream bos = null;
        try {
            if (pngBitmap != null) {
                bos = new BufferedOutputStream(new FileOutputStream(desPngFile));
                boolean ret = pngBitmap.compress(CompressFormat.PNG, 100, bos);
                if (ret) {
                    bos.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 此方法已废弃, 请使用{@link #getBitmap(File, int, int)}
     * <p>
     * 缩放图片大小
     *
     * @param context   上下环境
     * @param filePath  图像的路径
     * @param outWidth  指定输出图像的宽度
     * @param outHeight 指定输出图像的高度
     * @return 生成缩放后Bitmap
     */
    @Deprecated
    public static Bitmap getAutoSizedBitmap(Context context, String filePath, int outWidth,
                                            int outHeight) {
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        try {
            if (!FileUtils.isFileExist(filePath)) {
                return null;
            }
            fs = new FileInputStream(filePath);
            bs = new BufferedInputStream(fs);
            BitmapFactory.Options options = setBitmapOption(context, filePath, outWidth, outHeight);
            return BitmapFactory.decodeStream(bs, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                if (bs != null) {
                    bs.close();
                }
                if (fs != null) {
                    fs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取图片缩略图（取图片中间部分）
     * 此方法有两点好处：<br>
     * 1.使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。<br>
     * 2.缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使用这个工具生成的图像不会被拉伸。
     *
     * @param imagePath 图像的路径
     * @param width     指定输出图像的宽度
     * @param height    指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        Bitmap bitmap1 = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap1, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);//ThumbnailUtils.OPTIONS_RECYCLE_INPUT
//        if (bitmap1 != null && !bitmap1.isRecycled()) {
//            bitmap1.recycle();
//            bitmap1 = null;
//        }
        return bitmap;
    }

    /**
     * 获取图片缩略图
     *
     * @param src
     * @param width
     * @param height
     * @param recycle 是否回收原始图片
     * @return
     */
    public static Bitmap getImageThumbnail(Bitmap src, int width, int height, boolean recycle) {
        if (src == null) {
            return null;
        }

        Bitmap thumbnail = null;
        if (recycle) {
            thumbnail = ThumbnailUtils.extractThumbnail(src, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } else {
            thumbnail = ThumbnailUtils.extractThumbnail(src, width, height);
        }

        return thumbnail;
    }

    /**
     * 获取图片的BitmapFactory.Options信息
     *
     * @param context 上下文环境
     * @param file    图像的路径
     * @param width   指定输出图像的宽度
     * @param height  指定输出图像的高度
     * @return BitmapFactory.Options
     */
    public static BitmapFactory.Options setBitmapOption(Context context, String file, int width,
                                                        int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inTargetDensity = context.getResources().getDisplayMetrics().densityDpi;
        opt.inScaled = true;
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, opt);

        int outWidth = opt.outWidth;
        int outHeight = opt.outHeight;
        opt.inDither = false;
        opt.inPreferredConfig = Config.RGB_565;
        opt.inSampleSize = 1;

        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            opt.inSampleSize = sampleSize;
        }

        opt.inJustDecodeBounds = false;
        return opt;
    }

    /**
     * 获取Drawble的高度
     *
     * @param res Resources
     * @param id  drawble的id信息
     * @return int类型Drawble的高度
     */
    public static int getDrawbleHeight(Resources res, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        return options.outHeight;
    }

    /**
     * 获取Drawble的宽度
     *
     * @param res Resources
     * @param id  drawble的id信息
     * @return int类型Drawble的宽度
     */
    public static int getDrawblWidth(Resources res, int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        return options.outWidth;
    }

    /**
     * 图片旋转参数:正常
     */
    public static final int ROTATE_TYPE_NONE = 0;
    /**
     * 图片旋转参数:逆时针旋转90度
     */
    public static final int ROTATE_TYPE_LEFT = 1;
    /**
     * 图片旋转参数:顺时针旋转90度
     */
    public static final int ROTATE_TYPE_RIGHT = 2;
    /**
     * 图片旋转参数:旋转180度
     */
    public static final int ROTATE_TYPE_180_DEGREES = 3;

    /**
     * 此方法已废弃
     * <p>
     * 按指定的“图片旋转参数”，旋转Bitmap。
     * Notice：执行此方法后，仍需要释放源Bitmap的内存。
     *
     * @param original   源Bitmap
     * @param rotateType 图片旋转参数
     * @return 返回生成旋转图片
     */
    @Deprecated
    public static Bitmap rotateBitmap(Bitmap original, int rotateType) {
        if (original == null || original.isRecycled()) {
            return null;
        }

        float degrees = 0f;
        switch (rotateType) {
            case ROTATE_TYPE_LEFT:
                degrees = -90f;
                break;

            case ROTATE_TYPE_RIGHT:
                degrees = 90f;
                break;

            case ROTATE_TYPE_180_DEGREES:
                degrees = 180f;
                break;

            default:
                // error, should not go here
                break;
        }

        return rotate(original, degrees);

    }

    /**
     * 回收不用的bitmap
     *
     * @param b 要释放的bitmap
     */
    public static void recycleBitmap(Bitmap b) {
        if (b != null && !b.isRecycled()) {
            b.recycle();
            b = null;
        }
    }

    /**
     * 此方法已废弃，请使用{@link #save(Bitmap, File, CompressFormat)}
     * <p>
     * 将图片保存到本地sd卡
     *
     * @param bitmap 要保存的图片
     * @param path   图片保存的路径
     * @param format CompressFormat位图可以压缩成已知的格式
     * @return 返回是否保存成功。true保存成功，false保存失败
     */
    @Deprecated
    public static boolean saveBitmapToFile(Bitmap bitmap, String path, CompressFormat format) {
        boolean ret = false;
        File file = new File(path);
        if (!bitmap.isRecycled()) {
            try {
                ret = bitmap.compress(format, 80, new FileOutputStream(file, false));
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            if (!ret) {
                file.delete();
            }
        }
        return ret;
    }

    /**
     * 此方法已废弃，请通过缩放和压缩方法进行替换
     * <p>
     * 图片按指定宽度、指定大小的压缩方法（根据路径获取图片并压缩）。
     *
     * @param srcPath 图片的地址
     * @param width   图片指定宽度。如果为负值，表示不压缩
     * @param maxsize 指定的图片大小 ，单位K bytes
     * @return Bitmap对象
     */
    @Deprecated
    public static Bitmap getCompressBitmap(final String srcPath, final float width,
                                           final float maxsize) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww = 0f;//这里是设置的固定宽度
        if (width > 0) {
            ww = width;
        } else {
            ww = w;
        }

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > ww) {
            be = (int) (newOpts.outWidth / ww);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressBitmap(bitmap, maxsize);//压缩好比例大小后再进行质量压缩
    }

    /**
     * <p>
     * 压缩图片，使其大小在maxsize k 以下。如果压缩失败，则返回原图。
     * Notice：执行成功，原Bitmap会被释放。
     *
     * @param image
     * @return 返回压缩后的图片
     */
    @Deprecated
    private static Bitmap compressBitmap(Bitmap image, final float maxsize) {
        if (image == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (maxsize > 0 && (baos.toByteArray().length / 1024 > maxsize)) { //循环判断如果压缩后图片是否大于maxsize,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

        if (bitmap != null) {
            recycleBitmap(image);
            return bitmap;
        }
        return image;
    }

    /**
     * 对bitmap进行高斯模糊，产生一个新的bitmap
     *
     * @param context
     * @param bitmap
     * @param radius  模糊半径(0 < radius <= 25)
     * @return
     */
    public static Bitmap gaussianBlur(Context context, Bitmap bitmap, float radius) {
        if (context == null || bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap overlay = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bitmap, 0, 0, null);

        try {
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, overlay,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(overlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return overlay;
    }

    /**
     * stack模糊图片
     *
     * @param srcBitmap 原始bitmap
     * @param radius    模糊半径
     * @param recycle   是否回收原始图片
     * @return 经过模糊处理后的bitmap
     */
    public static Bitmap stackBlur(Bitmap srcBitmap, int radius, boolean recycle) {
        if (srcBitmap == null) {
            return null;
        }

        Bitmap ret = srcBitmap.copy(srcBitmap.getConfig(), true);
        if (ret == null) {
            return srcBitmap;
        }

        if (radius < 1) {
            return null;
        }
        int w = ret.getWidth();
        int h = ret.getHeight();

        int[] pix = new int[w * h];
        ret.getPixels(pix, 0, w, 0, 0, w, h);
        computePixels(pix, w, h, radius);
        ret.setPixels(pix, 0, w, 0, 0, w, h);

        if (recycle && !srcBitmap.isRecycled()) {
            srcBitmap.recycle();
        }
        return ret;
    }

    private static void computePixels(int[] pix, int w, int h, int radius) {
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
    }

}
