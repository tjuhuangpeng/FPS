/*
package bean;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
*/
/*import com.autohome.videoplayer.utils.ContextUtils;
import com.autohome.videoplayer.utils.LogUtil;*//*


public class NetworkHelpers {
    public static final String TAG = "NetworkHelpers";
    public static final int TYPE_INVALID = -1;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_WIFI = 1;
    private static final boolean LOGD = false;
    private static final String LOG_TAG = "NetworkHelpers";
    private static final int TYPE_MOBILE_HIPRI = 5;

    private NetworkHelpers() {
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        if(context == null) {
            return null;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
            if(connectivity == null) {
                LogUtil.w("NetworkHelpers", "Couldn\'t get connectivity manager");
                return null;
            } else {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                return info;
            }
        }
    }

    public static boolean isNetworkAvailable(Context context, boolean includeMobile, boolean includeRoaming) {
        if(context == null) {
            return true;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
            if(connectivity == null) {
                LogUtil.w("NetworkHelpers", "Couldn\'t get connectivity manager");
                return false;
            } else {
                boolean result = false;
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                result = info != null && info.isConnected();
                result = result && (includeRoaming || !info.isRoaming());
                result = result && (includeMobile || !isMobile(info));
                return result;
            }
        }
    }

    public static boolean isNetworkAvailable(NetworkInfo info, boolean includeMobile, boolean includeRoaming) {
        boolean result = false;
        result = info != null && info.isConnected();
        result = result && (includeRoaming || !info.isRoaming());
        result = result && (includeMobile || !isMobile(info));
        return result;
    }

    public static int getCurrentNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
        int result = -1;
        if(cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info != null) {
                result = info.getType();
            }
        }

        return result;
    }

    private static boolean isMobile(NetworkInfo info) {
        int type = info.getType();
        return type == 0 || type > 1 && type <= 5;
    }

    public static boolean isMobile(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        boolean result = false;
        if(connectivity == null) {
            LogUtil.w("NetworkHelpers", "couldn\'t get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null) {
                result = isMobile(info);
            }
        }

        return result;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        boolean result = false;
        if(connectivity == null) {
            LogUtil.w("NetworkHelpers", "couldn\'t get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null) {
                int type = info.getType();
                result = type == 1;
            }
        }

        return result;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if(connectivity == null) {
            LogUtil.w("NetworkHelpers", "couldn\'t get connectivity manager");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService("connectivity");
        if(connectivity == null) {
            LogUtil.w("NetworkHelpers", "couldn\'t get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null && info.getType() == 0 && info.isRoaming()) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkNetState(int nettype) {
        boolean result = true;
        if(nettype <= 0) {
            result = false;
            LogUtil.d("NetworkHelpers", "The network by user config can\'t be used.");
        } else {
            NetworkInfo netInfo = getNetworkInfo(ContextUtils.getApplicationContext());
            if(!isNetworkAvailable(netInfo, nettype > 1, nettype > 2)) {
                if(!isNetworkAvailable(netInfo, true, true)) {
                    result = false;
                    LogUtil.d("NetworkHelpers", "The network is disconnect.");
                } else if(!isNetworkAvailable(netInfo, true, false)) {
                    result = false;
                    LogUtil.d("NetworkHelpers", "The network can\'t used on roaming.");
                } else {
                    result = false;
                    LogUtil.d("NetworkHelpers", "The network can\'t used on mobile.");
                }
            }
        }

        return result;
    }

    public static void verifyNetState(int nettype) throws NetworkErrorException {
        String msg = "";
        boolean result = true;
        if(nettype <= 0) {
            result = false;
            msg = "The network by user config can\'t be used.";
        } else {
            NetworkInfo netInfo = getNetworkInfo(ContextUtils.getApplicationContext());
            if(!isNetworkAvailable(netInfo, nettype > 1, nettype > 2)) {
                if(!isNetworkAvailable(netInfo, true, true)) {
                    result = false;
                    msg = "The network is disconnect.";
                } else if(!isNetworkAvailable(netInfo, true, false)) {
                    result = false;
                    msg = "The network can\'t used on roaming.";
                } else {
                    result = false;
                    msg = "The network can\'t used on mobile.";
                }
            }
        }

        if(!result) {
            throw new NetworkErrorException(msg);
        }
    }

    public interface INetErrorType {
        int NET_ERROR_TYPE_NO_AVAIL = 0;
        int NET_ERROR_TYPE_NO_WIFI = 1;
    }

    public interface INetStateDef {
        int NETTYPE_NO_NETWORK = 0;
        int NETTYPE_NO_MOBILE = 1;
        int NETTYPE_NO_ROAMING = 2;
        int NETTYPE_ALL = 3;
    }
}
*/
