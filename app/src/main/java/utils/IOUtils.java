package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * @author yangliqiang
 * @date 2016/9/8
 */
public class IOUtils {

    /**
     * 将输入流存储到byte数组中
     *
     * @param
     */
    public static byte[] inputStream2byte(InputStream is) throws IOException {
        byte[] bs = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = -1;
        while ((len = is.read(bs)) != -1) {
            bos.write(bs, 0, len);
        }
        bs = bos.toByteArray();
        return bs;
    }

    /**
     * 从流获取字符串
     *
     * @param is 输入流
     * @return 返回流的字符串内容
     * @throws IOException 如果从流读取失败则抛出异常
     */
    public static String inputStream2String(InputStream is) throws IOException {
        byte[] bytes = inputStream2byte(is);
        return new String(bytes);
    }

    /**
     * 从流获取字符串
     *
     * @param is 输入流
     * @param  encoding 字符编码格式
     * @return 返回流的字符串内容
     * @throws IOException 如果从流读取失败则抛出异常
     */
    public static String inputStream2String(InputStream is, String encoding) throws IOException {
        byte[] bytes = inputStream2byte(is);
        return new String(bytes, encoding);
    }


    /**
     * 字节转换成流
     *
     * @param bytes
     * @return
     */
    public static InputStream byte2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 字符串转换成输入流
     *
     * @param input
     * @return
     */
    public static InputStream String2InputStream(String input) {
        byte[] bytes = input.getBytes();
        return byte2InputStream(bytes);
    }

    /**
     * 字符串转换成输入流
     *
     * @param input
     * @param encoding
     * @return
     * @throws IOException
     */
    public static InputStream String2InputStream(String input, String encoding) throws IOException {
        byte[] bytes = encoding != null ? input.getBytes(encoding) : input.getBytes();
        return byte2InputStream(bytes);
    }

    /**
     * 从输入流读取到输出流中
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = in.read(buf)) != -1) {
            out.write(buf, 0, len);
        }
    }

    /**
     * Close closable object and wrap {@link IOException} with {@link RuntimeException}
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred when close. ", e);
            }
        }
    }

}
