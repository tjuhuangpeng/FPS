package com.example.administrator.learning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import selsectwindow.AHAbstractExpandView;
import selsectwindow.AHEdgedExpandView;
import selsectwindow.AHSelectWindow;
import selsectwindow.ChooseEntity;
import selsectwindow.SingleMultipleListDataAdapter;

/**
 * Created by Administrator on 2017/2/22.
 */
public class testFragment1 extends ViewPagerFragment {
    View view;
    private AHSelectWindow selectWindow2;
    private AHEdgedExpandView edgedExpandView;
    private ListView listView;
    private String [] data={"apple","banana","orange", "watermelon","apple","banana","orange", "watermelon",
            "pear","grape","pineapple","strawberry", "cherry","mango","pear","grape","pineapple","strawberry", "cherry","mango"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.testfragment,container,false);

        //头部分类
        buildEdged();

        //listview
        listView = (ListView) view.findViewById(R.id.list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        ImageView header = new ImageView(getContext());
        header.setScaleType(ImageView.ScaleType.CENTER_CROP);
        header.setImageDrawable(getResources().getDrawable(R.mipmap.timg2));
        listView.addHeaderView(header);

        return view;
    }

    private void buildEdged() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View contentLayout = inflater.inflate(R.layout.select_popupwindow, null);
        final ListView listView = (ListView) contentLayout.findViewById(R.id.ah_test_select_list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        SingleMultipleListDataAdapter adapter = new SingleMultipleListDataAdapter(getActivity(), false);
        final List<ChooseEntity> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ChooseEntity chooseEntity = new ChooseEntity(i + "", "edge_item" + i);
            list.add(chooseEntity);
        }
        if (list != null && list.size() > 0) {
            ChooseEntity en = list.get(0);
            en.setChecked(true);
        }
        adapter.setList(list);
        adapter.setListView(listView);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseEntity entity = list.get(position);
                setListIndexSelector(list, entity, listView);
                entity.setChecked(true);
                ((SingleMultipleListDataAdapter) listView.getAdapter()).notifyDataSetChanged();
                edgedExpandView.update(chooseEntity2ExpandMenuItem(entity));
                selectWindow2.dismiss();
            }
        });
        final AHAbstractExpandView.ExpandMenuItem edge = chooseEntity2ExpandMenuItem(list.get(0));
        edgedExpandView = (AHEdgedExpandView) view.findViewById(R.id.ah_window_edged_expand_view);
        edgedExpandView.setStatusImageResource(R.drawable.arrow_down, R.drawable.arrow_up);
        edgedExpandView.setRightMargin(50);
        edgedExpandView.update(edge);
        edgedExpandView.setOnFoldStateChangedListener(new AHAbstractExpandView.OnFoldStateChangedListener() {
            @Override
            public void onStateChange(AHAbstractExpandView.ExpandMenuItem menuItem) {
                if (menuItem.isExpanded) {
                    selectWindow2.show();
                } else {
                    selectWindow2.dismiss();
                }
            }
        });
        selectWindow2 = new AHSelectWindow(getActivity(), contentLayout, edgedExpandView);
        //selectWindow2.setWindowBackgroundDrawable("#00ff00");
        // 获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        selectWindow2.setHostFrameHeight(dm.heightPixels);
        selectWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                edgedExpandView.getCurrentMenuItem().isExpanded = false;
                edgedExpandView.update(edgedExpandView.getCurrentMenuItem());
            }
        });
    }

    public void setListIndexSelector(List<ChooseEntity> list, ChooseEntity entity, ListView listView) {
        if (list != null) {
            for (ChooseEntity ce : list) {
                if (!TextUtils.isEmpty(ce.getName()) && ce.getName().equals(entity.getName())) {
                    ce.setChecked(true);
                } else {
                    ce.setChecked(false);
                }
            }
            ((SingleMultipleListDataAdapter) listView.getAdapter()).notifyDataSetChanged();
        }
    }

    private static AHAbstractExpandView.ExpandMenuItem chooseEntity2ExpandMenuItem(ChooseEntity entity) {
        AHAbstractExpandView.ExpandMenuItem item = new AHAbstractExpandView.ExpandMenuItem();
        item.menuId = entity.getId();
        item.menuContent = entity.getName();
        return item;
    }
}
