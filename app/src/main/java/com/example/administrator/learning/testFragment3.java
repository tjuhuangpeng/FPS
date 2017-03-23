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

import navigation.ToastUtils;
import selsectwindow.AHAbstractExpandView;
import selsectwindow.AHSelectWindow;
import selsectwindow.AHSlidingFilterView;
import selsectwindow.ChooseEntity;
import selsectwindow.SingleMultipleListDataAdapter;

/**
 * Created by Administrator on 2017/2/22.
 */
public class testFragment3 extends ViewPagerFragment {
    View view;
    private ListView listView;
    private String [] data={"apple","banana","orange", "watermelon","apple","banana","orange", "watermelon",
            "pear","grape","pineapple","strawberry", "cherry","mango","pear","grape","pineapple","strawberry", "cherry","mango"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.testfragment3,container,false);
        buildSliding();

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

    /**
     * 可滑动组合导航栏
     */
    private void buildSliding() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final List<List<ChooseEntity>> list = new ArrayList<>();
        List<AHAbstractExpandView.ExpandMenuItem> expandMenuItemList = new ArrayList<>();
        final List<AHSelectWindow> selectWindowList = new ArrayList<>();
        // final AHCompositeExpandView compositeExpandView = (AHCompositeExpandView) findViewById(R.id.ah_window_composite_expand_view);
        // compositeExpandView.setStatusImageResource(R.drawable.arrow_down, R.drawable.arrow_up);
        final AHSlidingFilterView filterExpandView = (AHSlidingFilterView) view.findViewById(R.id.ah_filter_expand_view);
        filterExpandView.setStatusImageResource(R.drawable.arrow_down, R.drawable.arrow_up);
        for (int i = 0; i < 7; i++) {
            View contentLayout = inflater.inflate(R.layout.select_popupwindow, null);
            final ListView listView = (ListView) contentLayout.findViewById(R.id.ah_test_select_list);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            SingleMultipleListDataAdapter adapter = new SingleMultipleListDataAdapter(getActivity(), false);
            final List<ChooseEntity> sublist = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                ChooseEntity chooseEntity = new ChooseEntity(j + "", "item" + j);
                // ChooseEntity chooseEntity = new ChooseEntity("", "");
                if (j == 0) {
                    chooseEntity.setChecked(true);
                }
                sublist.add(chooseEntity);
            }
            list.add(sublist);
            adapter.setList(sublist);
            adapter.setListView(listView);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            final int finalI = i;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ChooseEntity entity = sublist.get(position);
                    setListIndexSelector(sublist, entity, listView);
                    entity.setChecked(true);
                    filterExpandView.updateItem(finalI, chooseEntity2ExpandMenuItem(entity));
                    selectWindowList.get(finalI).dismiss();
                }
            });
            AHAbstractExpandView.ExpandMenuItem item = chooseEntity2ExpandMenuItem(sublist.get(0));
            expandMenuItemList.add(item);
            AHSelectWindow selectWindow = new AHSelectWindow(getActivity(), contentLayout, filterExpandView);
            //selectWindow.setWindowBackgroundDrawable("#ff0000");
            // 获取屏幕信息
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            selectWindow.setHostFrameHeight(dm.heightPixels);
            selectWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    filterExpandView.collapseAll();
                }
            });
            selectWindowList.add(selectWindow);
        }
        filterExpandView.setItemCenterMargin(50);
        filterExpandView.setMenuList(expandMenuItemList, true);
        filterExpandView.setOnItemStateChangedClickListener(new AHSlidingFilterView.OnItemStateChangedClickListener() {
            @Override
            public void onItemStateChange(int position, AHAbstractExpandView.ExpandMenuItem menuItem) {
                if (!TextUtils.isEmpty(menuItem.menuContent)) {
                    ToastUtils.showMessage(getActivity(), menuItem.menuContent);
                }
                if (menuItem.isExpanded) {
                    selectWindowList.get(position).show();
                } else {
                    selectWindowList.get(position).dismiss();
                }
            }
        });
        filterExpandView.notifyDataSetChanged();
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
