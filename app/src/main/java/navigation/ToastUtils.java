package navigation;

/**
 * Created by Administrator on 2016/12/29.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class ToastUtils {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static AHDownToast toast = null;
    private static Object lock = new Object();

    public ToastUtils() {
    }

    public static void showMessage(Context act, String msg) {
        showMessage(act, msg, 3000, false);
    }

    public static void showMessage(Context act, String msg, boolean isBlue) {
        showMessage(act, msg, 3000, isBlue);
    }

    public static void showMessage(Context act, int msg, boolean isBlue) {
        showMessage(act, msg, 3000, isBlue);
    }

    public static void showMessage(Context act, int msg, int len, boolean isBlue) {
        if(null != act) {
            showMessage(act, act.getString(msg), len, isBlue);
        }

    }

    public static void showMessage(final Context act, final String msg, final int len, final boolean isBlue) {
        if(act != null) {
            (new Thread(new Runnable() {
                public void run() {
                    ToastUtils.handler.post(new Runnable() {
                        public void run() {
                            synchronized(ToastUtils.lock) {
                                if(ToastUtils.toast != null) {
                                    ToastUtils.toast.setText(msg);
                                    ToastUtils.toast.setDuration(len);
                                    ToastUtils.toast.setBlue(isBlue);
                                } else {
                                    ToastUtils.toast = new AHDownToast(act, msg);
                                    ToastUtils.toast.setBlue(isBlue);
                                }

                                ToastUtils.toast.show();
                            }
                        }
                    });
                }
            })).start();
        }

    }
}
