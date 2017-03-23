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
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import navigation.ToastUtils;
import selsectwindow.AHAbstractExpandView;
import selsectwindow.AHCompositeExpandView;
import selsectwindow.AHSelectWindow;
import selsectwindow.ChooseEntity;
import selsectwindow.SingleMultipleListDataAdapter;

/**
 * Created by Administrator on 2017/2/22.
 */
public class testFragment2 extends ViewPagerFragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.testfragment2,container,false);
        buildComposite();
        return view;
    }

    /**
     * 组合导航栏
     */
    private void buildComposite() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final List<List<ChooseEntity>> list = new ArrayList<>();
        List<AHAbstractExpandView.ExpandMenuItem> expandMenuItemList = new ArrayList<>();
        final List<AHSelectWindow> selectWindowList = new ArrayList<>();
        final AHCompositeExpandView compositeExpandView = (AHCompositeExpandView) view.findViewById(R.id.ah_window_composite_expand_view);
        compositeExpandView.setStatusImageResource(R.drawable.arrow_down, R.drawable.arrow_up);
        for (int i = 0; i < 3; i++) {
            View contentLayout = inflater.inflate(R.layout.select_popupwindow, null);
            final ListView listView = (ListView) contentLayout.findViewById(R.id.ah_test_select_list);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            SingleMultipleListDataAdapter adapter = new SingleMultipleListDataAdapter(getActivity(), false);
            final List<ChooseEntity> sublist = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
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
                    compositeExpandView.updateItem(finalI, chooseEntity2ExpandMenuItem(entity));
                    selectWindowList.get(finalI).dismiss();
                }
            });
            AHAbstractExpandView.ExpandMenuItem item = chooseEntity2ExpandMenuItem(sublist.get(0));
            expandMenuItemList.add(item);
            AHSelectWindow selectWindow = new AHSelectWindow(getActivity(), contentLayout, compositeExpandView);
            //selectWindow.setWindowBackgroundDrawable("#ff0000");
            // 获取屏幕信息
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            selectWindow.setHostFrameHeight(dm.heightPixels);
            selectWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    compositeExpandView.collapseAll();
                }
            });
            selectWindowList.add(selectWindow);
        }
        compositeExpandView.setItemCenterMargin(50);
        compositeExpandView.setMenuList(expandMenuItemList, true);
        compositeExpandView.setOnItemStateChangedClickListener(new AHCompositeExpandView.OnItemStateChangedClickListener() {
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
        compositeExpandView.notifyDataSetChanged();
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
