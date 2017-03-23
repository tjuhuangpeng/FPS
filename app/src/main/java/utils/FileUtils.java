package utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 文件操作工具
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    /**
     * 文件操作工具 默认构造器
     */
    public FileUtils() {
        throw new AssertionError();
    }

    /**
     * 根据指定路径创建文件
     *
     * @param myfile 文件路径
     * @return 返回创建成功的文件,若创建失败则返回null
     */
    public static File createFile(String myfile) {
        File file = null;
        try {
            file = new File(myfile);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }

        return file;
    }

    /**
     * 写入String类型内容到指定的文件上
     *
     * @param content  上下文环境
     * @param filePath 要写入的文件名字
     * @return 创建的File文件
     */
    public static File createFile(String content, String filePath) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        return FileUtils.writeFileFromInput(filePath, inputStream);
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName 要创建的目录
     * @return 创建的目录
     */
    public static boolean createDir(String dirName) {
        if(StringUtils.isEmpty(dirName)){
            return false;
        }

        boolean result = false;
        File dir = new File(dirName);
        if (!dir.exists()) {
            try {
                result = dir.mkdirs();
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
        }else{
            result = true;
        }
        return result;
    }

    /**
     * 拷贝文件
     *
     * @param oldPath 源文件路径
     * @param newPath 保存到目标文件路径
     * @throws Exception 如果拷贝失败则抛出异常
     */
    public static void copyFile(String oldPath, String newPath) throws Exception {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) { // 文件存在时
            InputStream inStream = new FileInputStream(oldPath); // 读入原文件
            FileOutputStream fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return boolean类型：true:文件存在，false:文件不存在
     */
    public static boolean isFileExist(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }

    /**
     * 判断目录是否存在
     * @param fileDirPath
     * @return
     */
    public static boolean isDirExist(String fileDirPath){
        if(TextUtils.isEmpty(fileDirPath)){
            return false;
        }

        File dir = new File(fileDirPath);
        return dir != null && dir.exists() && dir.isDirectory();
    }

    /**
     * 判断目录是否存在
     * @param fileDir
     * @return
     */
    public static boolean isDirExist(File fileDir){
        if(fileDir == null){
            return false;
        }

        return fileDir.exists() && fileDir.isDirectory();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return boolean类型：true已经删除，false删除失败
     */
    public static boolean deleteFile(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).delete();
    }

    /**
     * 删除目录
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }

        return deleteDir(new File(dirPath));
    }

    /**
     * 删除目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (null == dir) {
            return false;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            int len = 0;
            if (children != null && (len = children.length) > 0) {
                for (int i = 0; i < len; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 将一个InputStream里面的数据写入到对应文件中(针对内部存储用)
     *
     * @param fileName 要写入的文件路径
     * @param input    输入流
     * @return 写入后的文件
     */
    public static File writeFileFromInput(String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            file = createFile(fileName);

            output = new FileOutputStream(file);
            byte[] buffer = new byte[2048];
            int length = 0;
            while ((length = (input.read(buffer))) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
        } catch (Exception e) {
            if (file != null && file.exists()) {
                file.delete();
            }
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 获取指定文件的输入流
     *
     * @param fileName 文件路径
     * @return 指定文件的输入流
     */
    public static InputStream getFileInputStream(String fileName) {
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            inputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取应用私有目录下对应文件的输入流
     *
     * @param context  上下文环境
     * @param fileName 文件路径
     * @return 指定文件的输入流
     */
    public static InputStream getFileInputStreamFromPrivateDir(Context context, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 将String类型的内容写入到SD文件指定位置
     *
     * @param path     sd路径目录
     * @param fileName 文件名字
     * @param content  String类型内容
     */
    public static void writeString2SD(String path, String fileName, String content) {
        File file = null;
        FileOutputStream outputStream = null;
        try {
            createDir(path);
            file = createFile(path + fileName);
            outputStream = new FileOutputStream(file, true);
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取android_assets目录下的文件
     *
     * @param context  上下文环境
     * @param fileName assets目录下的文件名字
     * @return 文件的byte[]形式
     */
    public static byte[] readFileFromAssets(Context context, String fileName) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream input = null;
        try {
            input = context.getAssets().open(fileName);
            int tempchar;
            while ((tempchar = input.read()) != -1) {
                out.write(tempchar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    out.close();
                    input.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }

    /**
     * 判断CSS或JS文件是否存在
     *
     * @param context  上下文环境
     * @param fileName CSS或JS的文件名字
     * @return boolean类型：true文件存在，false文件不存在
     */
    public static boolean isCssOrJsFileExist(Context context, String fileName) {
        if(context == null){
            return false;
        }
        fileName = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
        return FileUtils.isFileExist(fileName);

    }

    /**
     * 删除指定的CSS或JS文件
     *
     * @param context  上下文环境
     * @param fileName CSS或JS的文件名字
     * @return boolean类型：true已经删除，false删除失败
     */
    public static boolean removeCssOrJs(Context context, String fileName) {
        if(context == null){
            return false;
        }
        fileName = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
        return FileUtils.deleteFile(fileName);
    }

    /**
     * 读取String类型内容的文件。
     *
     * @param filePath 指定要读取的文件路径
     * @return 返回文件的内容
     * @throws IOException 如果文件读取失败则抛出异常
     */
    public static String getFileContent(String filePath) throws IOException {
        InputStream input = getFileInputStream(filePath);
        return IOUtils.inputStream2String(input);
    }

    /**
     * 获取文件大小
     *
     * @param file 要获取大小的文件
     * @return long类型：文件的长度
     */
    public static long getFileSizes(File file) {//取得文件大小
        long size = 0;
        if(file != null && file.isFile()){
            size = file.length();
        }
        return size;
    }

    /**
     * 根据文件的大小，设置合适的单位
     * @param context
     * @param filesize 文件大小
     * @param shorter 是否以简短形式显示
     * @return
     */
    public static String formatter(Context context, long filesize, boolean shorter){
        String result = null;
        if(shorter){
            result = Formatter.formatShortFileSize(context, filesize);
        }else{
            result = Formatter.formatFileSize(context, filesize);
        }
        return result;
    }

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }
}
