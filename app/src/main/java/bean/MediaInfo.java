/*
package bean;


import android.os.Bundle;
import java.util.HashMap;

public class MediaInfo implements IVideoPlayStateDef {
    private String mUrl;
    public String mVId;
    public boolean mIsAntiChain;
    private int mNetType;
    private boolean mCacheVideo;
    private Bundle mExtra;
    private HashMap<String, String> mHttpHeader;

    public MediaInfo(String url, String vid, boolean isAntiChain, int playNetType, boolean cacheVideo, HashMap<String, String> httpHeader) {
        this.mNetType = 1;
        this.mUrl = url;
        this.mVId = vid;
        this.mIsAntiChain = isAntiChain;
        this.mNetType = playNetType;
        this.mCacheVideo = cacheVideo;
        this.mHttpHeader = httpHeader;
    }

    public MediaInfo(String url, String vid, boolean isAntiChain) {
        this(url, vid, isAntiChain, 1, false, (HashMap)null);
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public int getNetType() {
        return this.mNetType;
    }

    public void setNetType(int playNetType) {
        this.mNetType = playNetType;
    }

    public boolean isCacheVideo() {
        return this.mCacheVideo;
    }

    public Bundle getExtra() {
        return this.mExtra;
    }

    public HashMap<String, String> getHttpHeader() {
        return this.mHttpHeader;
    }

    public void setExtra(Bundle extra) {
        this.mExtra = extra;
    }

    public void addHead(String key, String value) {
        if(this.mHttpHeader == null) {
            this.mHttpHeader = new HashMap();
        }

        this.mHttpHeader.put(key, value);
    }
}
*/
