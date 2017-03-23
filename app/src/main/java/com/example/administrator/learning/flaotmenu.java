package com.example.administrator.learning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import view.AHFloatingButton;
import view.AHFloatingMenu;


public class flaotmenu extends Activity {
    private AHFloatingMenu mRootMenu;
    private AHFloatingButton mMenuItem1;
    private AHFloatingButton mMenuItem2;
    private AHFloatingButton mMenuItem3;
    private AHFloatingButton mMenuItem4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_menu);

        mRootMenu = (AHFloatingMenu) findViewById(R.id.root_menu);
        mRootMenu.setLabelsStyle(R.style.menu_labels_style);
        mRootMenu.setOnFloatingActionsMenuUpdateListener(new AHFloatingMenu.OnFloatingMenuListener() {
            @Override
            public void onMenuExpanded() {
            }
            @Override
            public void onMenuCollapsed() {
            }
        });

        mMenuItem1 = (AHFloatingButton) findViewById(R.id.menu_item_1);
        mMenuItem2 = (AHFloatingButton) findViewById(R.id.menu_item_2);
        mMenuItem3 = (AHFloatingButton) findViewById(R.id.menu_item_3);
        mMenuItem4 = (AHFloatingButton) findViewById(R.id.menu_item_4);

        mMenuItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(flaotmenu.this, "菜单1被点击", Toast.LENGTH_SHORT).show();
                mRootMenu.collapse();
            }
        });
        mMenuItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(flaotmenu.this, "菜单2被点击", Toast.LENGTH_SHORT).show();
                mRootMenu.collapse();
            }
        });
        mMenuItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(flaotmenu.this, "菜单3被点击", Toast.LENGTH_SHORT).show();
                mRootMenu.collapse();
            }
        });
        mMenuItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(flaotmenu.this, "菜单4被点击", Toast.LENGTH_SHORT).show();
                mRootMenu.collapse();
            }
        });
    }
}

