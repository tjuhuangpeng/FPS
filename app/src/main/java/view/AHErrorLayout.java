package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.administrator.learning.R;


/**
 * 界面公共加载控件
 * @author yangliqiang
 * @date 2016/6/23
 */
public class AHErrorLayout extends LinearLayout implements  View.OnClickListener {
    /**
     * 加载失败
     */
    public static final int NETWORK_ERROR = 1;
    /**
     * 没有网络
     */
    @Deprecated
    public static final int NETWORK_NONE = 2;
    /**
     * 加载结束，无数据
     */
    public static final int NODATA = 3;
    /**
     * 正在加载中
     */
    public static final int NETWORK_LOADING = 4;
    /**
     *隐藏组件（此常量已废弃，无需再使用）
     */
    @Deprecated
    public static final int HIDE_LAYOUT = 5;
    /**
     * 无数据但支持点击
     */
    public static final int NODATA_ENABLE_CLICK = 6;

    private Context mContext;
    /**
     * 无数据提示内容
     */
    private String mNoDataTipContent;
    /**
     * 失败提示内容
     */
    private String mErrorTipContent;

    /**
     * 没有网络的提示内容
     */
    private String mNoNetworkTipContent;

    /**
     * 加载中提示文本
     */
    private String mLoadingTipContent;

    /**
     * 功能按钮文本内容
     */
    private String mFailActionContent;

    /**
     * 当前加载状态
     */
    private int mCurrentStatus = HIDE_LAYOUT;

    /**
     * 功能按钮是否可见
     */
    private String mNoDataActionContent;

    /**
     * 功能按钮点击事件监听
     */
    private LoadActionListener mLoadActionListener;
    /**
     * 监听器，监听整个Layout的点击事件
     */
    @Deprecated
    private OnClickListener mOnLayoutClickListener;
    /**
     *停止加载内容提示控件
     */
    private TextView mLoadResultTipTv;
    /**
     * 失败状态功能按钮控件
     */
    private TextView mFailFuncBtn;
    /**
     * 无数据状态功能按钮控件
     */
    private TextView mNoDataFuncBtn;
    /**
     * 图片控件
     */
    private ImageView mIconIv;
    /**
     * 加载中进度控件
     */
    private ProgressBar mLoadingProgressBar;
    /**
     * 加载中进度控件(暗黑样式)
     */
    private ProgressBar mNightLoadingProgressBar;
    /**
     * 正在加载提示控件
     */
    private TextView mLoadingTipTv;

    /**
     * 正在加载布局
     */
    private View mLoadingLayout;
    /**
     * 停止加载布局
     */
    private View mLoadResultLayout;

    /**
     * 正在加载“三个点”
     */
    private ImageView mLoadingDot;

    private AnimationDrawable loadingDotAnimDrawable;
    /**
     * 是否为暗黑模式
     */
    private boolean isBlackMode = false;
    /**
     * 是否来自图片页面
     */
    private boolean isFromPhoto = false;

    public AHErrorLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public AHErrorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        getValueFromAttrs(attrs);
        init();
    }

    public AHErrorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        getValueFromAttrs(attrs);
        init();
    }

    private void init(){
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.error, null);
        mLoadingLayout = view.findViewById(R.id.linear_loading_layout);
        mLoadResultLayout = view.findViewById(R.id.linear_noloading_layout);
        mLoadResultTipTv = (TextView) view.findViewById(R.id.tv_load_result_tip);
        mIconIv = (ImageView) view.findViewById(R.id.iv_noloading_icon);
        mLoadingDot = (ImageView) view.findViewById(R.id.tm_loading_dot);
        mLoadingProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_loading);
        mNightLoadingProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_loading_for_night);
        mLoadingTipTv = (TextView) view.findViewById(R.id.tv_loading_tip);
        mFailFuncBtn = (TextView) view.findViewById(R.id.btn_fail_func);
        mNoDataFuncBtn = (TextView) view.findViewById(R.id.btn_nodata_func);

        if(TextUtils.isEmpty(mNoDataTipContent)){
            mNoDataTipContent = "暂无数据";
        }
        if(TextUtils.isEmpty(mErrorTipContent)){
            mErrorTipContent = "加载失败";
        }
        if(TextUtils.isEmpty(mLoadingTipContent)){
            mLoadingTipContent = "加载中";
        }
        if(TextUtils.isEmpty(mNoNetworkTipContent)){
            mNoNetworkTipContent = "当前网络不给力";
        }
        if(TextUtils.isEmpty(mFailActionContent)){
            mFailActionContent = "刷新重试";
        }

        mFailFuncBtn.setOnClickListener(this);
        mNoDataFuncBtn.setOnClickListener(this);
        this.addView(view);

        //设置默认为白色背景
        setBackgroundColor(getResources().getColor(R.color.ahlib_color09));
        setClickable(true);

        mLoadingDot.setImageResource(R.drawable.ahlib_loading_dot);
        loadingDotAnimDrawable = (AnimationDrawable) mLoadingDot.getDrawable();
    }

    private void getValueFromAttrs(AttributeSet attrs){
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.AHErrorLayout);
        mNoDataTipContent = a.getString(R.styleable.AHErrorLayout_noDataTipContent);
        mErrorTipContent = a.getString(R.styleable.AHErrorLayout_failTipContent);
        mLoadingTipContent = a.getString(R.styleable.AHErrorLayout_loadingTipContent);
        mNoNetworkTipContent = a.getString(R.styleable.AHErrorLayout_noNetworkTipContent);

        mFailActionContent = a.getString(R.styleable.AHErrorLayout_failActionName);
        mNoDataActionContent = a.getString(R.styleable.AHErrorLayout_noDataActionName);

        a.recycle();
    }

    /**
     * 设置提示图片
     * @param iconResId
     */
    public void setIcon(int iconResId){
        mIconIv.setImageResource(iconResId);
    }

    /**
     * 设置提示图片
     * @param drwable
     */
    public void setIcon(Drawable drwable){
        mIconIv.setImageDrawable(drwable);
    }

    /**
     * 设置失败的提示内容
     * @param errorContent
     */
    public void setErrorMessage(String errorContent){
        this.mErrorTipContent = errorContent;
        changeUI(mCurrentStatus);
    }

    /**
     * 设置无数据的提示内容
     * @param nodataContent
     */
    public void setNoDataContent(String nodataContent){
        this.mNoDataTipContent = nodataContent;
        changeUI(mCurrentStatus);
    }

    /**
     * 设置正在加载时的提示内容
     * @param loadingTip
     */
    public void setLoadingTipContent(String loadingTip){
        this.mLoadingTipContent = loadingTip;
        changeUI(mCurrentStatus);
    }

    /**
     * 设置无网络时的提示内容
     * @param networkTip
     */
    public void setNoNetworkTipContent(String networkTip){
        this.mNoNetworkTipContent = networkTip;
        changeUI(mCurrentStatus);
    }

    /**
     * 设置失败时的操作按钮名称
     * @param actionName
     */
    public void setFailActionContent(String actionName){
        this.mFailActionContent = actionName;
        changeUI(mCurrentStatus);
    }

    /**
     * 设置无数据时的操作按钮名称
     * @param actionName
     */
    public void setNoDataActionContent(String actionName){
        this.mNoDataActionContent = actionName;
        changeUI(mCurrentStatus);
    }

    /**
     * 获取当前控件加载状态
     * @return
     */
    public int getErrorState(){
        return this.mCurrentStatus;
    }

    /**
     * 设置加载失败和无数据时的操作的回调
     * @param listener
     */
    public void setActionListener(LoadActionListener listener){
        this.mLoadActionListener = listener;
    }

    /**
     * 设置加载的状态
     * @param stateType 可设定值参考：{@link #NETWORK_ERROR}，{@link #NETWORK_NONE}，{@link #NODATA}
     *                  ,{@link #NODATA_ENABLE_CLICK}，{@link #NETWORK_LOADING}
     */
    public void setErrorType(int stateType){
        this.mCurrentStatus = stateType;
        changeUI(stateType);
    }

    /**
     * 切换状态
     * @param stateType
     */
    private void changeUI(int stateType){
        switch (stateType){
            case NETWORK_ERROR:
                mIconIv.setImageResource(!isBlackMode ? R.drawable.ahlib_load_fail : R.drawable.ahlib_load_fail_night);
                mLoadingLayout.setVisibility(GONE);
                mLoadResultLayout.setVisibility(VISIBLE);
                if(!TextUtils.isEmpty(mErrorTipContent)){
                    mLoadResultTipTv.setVisibility(VISIBLE);
                    mLoadResultTipTv.setText(mErrorTipContent);
                }else{
                    mLoadResultTipTv.setVisibility(GONE);
                }

                mNoDataFuncBtn.setVisibility(GONE);
                mFailFuncBtn.setVisibility(!TextUtils.isEmpty(mFailActionContent) ? VISIBLE : GONE);
                mFailFuncBtn.setText(mFailActionContent);
                changeDotLoadingAnim(loadingDotAnimDrawable, false);
                break;
            case NETWORK_NONE:
                mIconIv.setImageResource(R.drawable.ahlib_load_nonetwork);
                mLoadingLayout.setVisibility(GONE);
                mLoadResultLayout.setVisibility(VISIBLE);
                if(!TextUtils.isEmpty(mNoNetworkTipContent)){
                    mLoadResultTipTv.setVisibility(VISIBLE);
                    mLoadResultTipTv.setText(mNoNetworkTipContent);
                }else{
                    mLoadResultTipTv.setVisibility(GONE);
                }

                mNoDataFuncBtn.setVisibility(GONE);
                mFailFuncBtn.setVisibility(!TextUtils.isEmpty(mFailActionContent) ? VISIBLE : GONE);
                mFailFuncBtn.setText(mFailActionContent);
                changeDotLoadingAnim(loadingDotAnimDrawable, false);
                break;
            case NODATA:
                mIconIv.setImageResource(R.drawable.ahlib_load_empty);
                mLoadingLayout.setVisibility(GONE);
                mLoadResultLayout.setVisibility(VISIBLE);
                if(!TextUtils.isEmpty(mNoDataTipContent)){
                    mLoadResultTipTv.setVisibility(VISIBLE);
                    mLoadResultTipTv.setText(mNoDataTipContent);
                }else{
                    mLoadResultTipTv.setVisibility(GONE);
                }

                mFailFuncBtn.setVisibility(GONE);
                mNoDataFuncBtn.setVisibility(GONE);
                changeDotLoadingAnim(loadingDotAnimDrawable, false);
                break;
            case NODATA_ENABLE_CLICK:
                mIconIv.setImageResource(R.drawable.ahlib_load_empty);
                mLoadingLayout.setVisibility(GONE);
                mLoadResultLayout.setVisibility(VISIBLE);
                if(!TextUtils.isEmpty(mNoDataTipContent)){
                    mLoadResultTipTv.setVisibility(VISIBLE);
                    mLoadResultTipTv.setText(mNoDataTipContent);
                }else{
                    mLoadResultTipTv.setVisibility(GONE);
                }

                mFailFuncBtn.setVisibility(GONE);
                mNoDataFuncBtn.setVisibility(!TextUtils.isEmpty(mNoDataActionContent) ? VISIBLE : GONE);
                mNoDataFuncBtn.setText(mNoDataActionContent);
                changeDotLoadingAnim(loadingDotAnimDrawable, false);
                break;
            case NETWORK_LOADING:
                mLoadingLayout.setVisibility(VISIBLE);
                mLoadResultLayout.setVisibility(INVISIBLE);
                if(!TextUtils.isEmpty(mLoadingTipContent)){
                    mLoadingTipTv.setText(mLoadingTipContent);
                }
                changeDotLoadingAnim(loadingDotAnimDrawable, true);
                break;
            default:
                break;
        }
    }

    /**
     * 设置暗黑样式
     * @param isBlack true代表设置为暗黑样式，false为正常样式
     */
    public void setBlackMode(boolean isBlack){
        this.isBlackMode = isBlack;
        if(isBlack){
            setBackgroundColor(getResources().getColor(R.color.ahlib_color16));
            mLoadingProgressBar.setVisibility(GONE);
            mLoadingTipTv.setVisibility(GONE);
            mLoadingDot.setVisibility(GONE);
            mNightLoadingProgressBar.setVisibility(VISIBLE);
            changeDotLoadingAnim(loadingDotAnimDrawable, false);
            mIconIv.setImageResource(R.drawable.ahlib_load_fail_night);
            mFailFuncBtn.setBackgroundResource(R.drawable.ahlib_load_btn_night_bg);
            mFailFuncBtn.setTextColor(getResources().getColor(R.color.ahlib_color0555));
            mNoDataFuncBtn.setBackgroundResource(R.drawable.ahlib_load_btn_night_bg);
            mNoDataFuncBtn.setTextColor(getResources().getColor(R.color.ahlib_color0666));

        }
    }

    /**
     * 控制正在加载动画，"三个点"
     * @param loading
     */
    private void changeDotLoadingAnim(AnimationDrawable animDrawable, boolean loading){
        if(animDrawable == null){
            return;
        }

        if(!loading){
            if(animDrawable.isRunning()){
                animDrawable.stop();
            }
        }else {
            animDrawable.start();
        }

    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        changeDotLoadingAnim(loadingDotAnimDrawable, true);

        /*AHBaseApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();*/
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //AHBaseApplication.getInstance().getAtSkinObserable().unregistered(this);
        changeDotLoadingAnim(loadingDotAnimDrawable, false);
        loadingDotAnimDrawable = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_fail_func){
            setErrorType(NETWORK_LOADING);
            if(mLoadActionListener != null){
                mLoadActionListener.onFailStatusAction(view);
            }
            if (mOnLayoutClickListener != null) {
                mOnLayoutClickListener.onClick(this);
            }
        }else if(view.getId() == R.id.btn_nodata_func){
            if(mLoadActionListener != null){
                mLoadActionListener.onNoDataStatusAction(view);
            }
            if (mOnLayoutClickListener != null) {
                mOnLayoutClickListener.onClick(this);
            }
        }
    }

    /**
     * 此方法已废弃，无需再使用
     * @param context
     */
    @Deprecated
    public void changeErrorLayoutBgMode(Context context) {

    }

    /**
     * 此方法已废弃，无需再使用
     * @param isCanNightMode
     */
    @Deprecated
    public void setIsCanNightMode(boolean isCanNightMode){

    }

    /**
     * 此方法已废弃，无需再使用
     * @param type
     */
    @Deprecated
    public void setErrorTypeForBackgroud(int type) {

    }

    /**
     * 此方法已废弃，无需再使用
     * @param isNight
     */
    @Deprecated
    public void setDayNight(boolean isNight) {

    }

    /**
     * 此方法已废弃，无需再使用
     * @param listener
     */
    @Deprecated
    public void setOnLayoutClickListener(OnClickListener listener){
        mOnLayoutClickListener = listener;
    }

    /**
     * 此方法已废弃，无需再使用
     * @param isFromPhoto
     */
    @Deprecated
    public void setPhoto(boolean isFromPhoto) {
        this.isFromPhoto = isFromPhoto;
    }

    /**
     * 隐藏组件
     */
    public void dismiss() {
        this.setVisibility(View.GONE);
    }

    /**
     * 显示组件
     */
    public void show(){
        this.setVisibility(VISIBLE);
    }

    /**
     * 是否处于加载出错状态
     * @return
     */
    public boolean isLoadError() {
        if (mCurrentStatus == NETWORK_ERROR) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否正在加载
     * @return
     */
    public boolean isLoading() {
        if (mCurrentStatus == NETWORK_LOADING) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 此方法已废弃，无需再使用
     * @param noLoadingAnim
     */
    @Deprecated
    public void setNoLoadingAnim(boolean noLoadingAnim) {

    }

    /**
     * 此方法已废弃，无需再使用
     * @param use
     */
    @Deprecated
    public void setBackGroundTransparent(boolean use) {

    }

    /**
     * 加载失败和加载无数据状态时的操作监听
     */
    public interface LoadActionListener{
        /**
         * 加载失败或无网络状态时，点击操作按钮的回调
         * @param view
         */
        void onFailStatusAction(View view);

        /**
         * 加载无数据状态时，点击操作按钮的回调
         * @param view
         */
        void onNoDataStatusAction(View view);
    }
}
