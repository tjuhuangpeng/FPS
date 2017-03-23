package com.example.administrator.learning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Administrator on 2017/2/22.
 */
public class testFragment4 extends ViewPagerFragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.testfragment4,container,false);

       /* List<AHAbstractExpandView.ExpandMenuItem> expandMenuItemList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AHAbstractExpandView.ExpandMenuItem item = new AHAbstractExpandView.ExpandMenuItem();
            item.menuId = i;
            item.menuContent = "items " + i;
            item.isExpanded = false;
            expandMenuItemList.add(item);
        }
        AHCompositeExpandView compositeExpandView = (AHCompositeExpandView) view.findViewById(R.id.ah_window_composite_expand_view);
        compositeExpandView.setItemCenterMargin(10);
        compositeExpandView.setStatusImageResource(R.drawable.arrow_down, R.drawable.arrow_up);
        compositeExpandView.setMenuList(expandMenuItemList, true);
        compositeExpandView.setOnItemStateChangedClickListener(new AHCompositeExpandView.OnItemStateChangedClickListener() {
            @Override
            public void onItemStateChange(int position, AHAbstractExpandView.ExpandMenuItem menuItem) {
                ToastUtils.showMessage(getActivity(), menuItem.menuContent);
            }
        });
        compositeExpandView.notifyDataSetChanged();*/
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(getActivity());
        ImageLoader.getInstance().init(configuration);

        return view;
    }


}
