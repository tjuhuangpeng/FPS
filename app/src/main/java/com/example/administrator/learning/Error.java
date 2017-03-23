package com.example.administrator.learning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import view.AHErrorLayout;


/**
 * 加载失败demo
 * @author yangliqiang
 * @date 2016/6/23
 */
public class Error extends Activity implements View.OnClickListener{
    private AHErrorLayout mLoadLayout;
    private Button mLoadErrorBtn;
    private Button mLoadNodataBtn;
    private Button mLoadStartBtn;
    private Button mNoNetworkBtn;
    private Button mNoDataClickableBtn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorlayout);
        initView();
        setData();
    }

    private void initView(){
        mLoadLayout = (AHErrorLayout) findViewById(R.id.load_layout);
        mLoadErrorBtn = (Button) findViewById(R.id.load_error);
        mLoadNodataBtn = (Button) findViewById(R.id.load_nodata);
        mLoadStartBtn = (Button) findViewById(R.id.load_start);
        mNoNetworkBtn = (Button) findViewById(R.id.load_nonetwork);
        mNoDataClickableBtn = (Button) findViewById(R.id.load_nodata_clickable);
        mLoadErrorBtn.setOnClickListener(this);
        mLoadNodataBtn.setOnClickListener(this);
        mLoadStartBtn.setOnClickListener(this);
        mNoNetworkBtn.setOnClickListener(this);
        mNoDataClickableBtn.setOnClickListener(this);

        mLoadLayout.setPhoto(true);
        mLoadLayout.setActionListener(new AHErrorLayout.LoadActionListener() {
            @Override
            public void onFailStatusAction(View view) {
                Toast.makeText(Error.this, "重新加载", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoDataStatusAction(View view) {
                Toast.makeText(Error.this, "没有数据，接下干嘛？", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setData(){
//        mLoadLayout.setNoDataTipContent("暂无数据2");
//        mLoadLayout.setErrorTipContent("加载失败2");
//        mLoadLayout.setLoadingTipContent("加载中...");
//        mLoadLayout.setNoNetworkTipContent("网络真愁人");
//        mLoadLayout.setFailActionContent("我试试");
//        mLoadLayout.setNoDataActionContent("去添加一个");
//        mLoadLayout.setNoDataTipContent("小君走丢了");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.load_error:
                mLoadLayout.setErrorType(AHErrorLayout.NETWORK_ERROR);
                break;
            case R.id.load_nodata:
                mLoadLayout.setErrorType(AHErrorLayout.NODATA);
                break;
            case R.id.load_start:
                mLoadLayout.setErrorType(AHErrorLayout.NETWORK_LOADING);
                break;
            case R.id.load_nonetwork:
                mLoadLayout.setErrorType(AHErrorLayout.NETWORK_NONE);
                break;
            case R.id.load_nodata_clickable:
                mLoadLayout.setNoDataActionContent("去添加口碑");
                mLoadLayout.setErrorType(AHErrorLayout.NODATA_ENABLE_CLICK);
                break;
            default:
                break;
        }
    }
}
