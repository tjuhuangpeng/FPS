package com.example.administrator.ijk;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.learning.R;
import com.example.administrator.learning.VideoItemData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Author  wangchenchen
 * Description video列表adapter
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoItemData> list;
    DisplayImageOptions options;
    public VideoAdapter(Context context){
        list=new ArrayList<>();


         options = new DisplayImageOptions.Builder()
/*                .showImageOnLoading(R.drawable.loading) // resource or drawable
                .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
                .showImageOnFail(R.drawable.fail) // resource or drawable*/
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    @Override
    public VideoAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        VideoAdapter.VideoViewHolder holder = new VideoAdapter.VideoViewHolder(view);
        view.setTag(holder);
        return new VideoAdapter.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void refresh(List<VideoItemData> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout videoLayout;
        private int position;
        private RelativeLayout showView;
        private TextView title,from;
        private ImageView background;


        public VideoViewHolder(View itemView) {
            super(itemView);
            videoLayout = (FrameLayout) itemView.findViewById(R.id.layout_video);
            showView= (RelativeLayout) itemView.findViewById(R.id.showview);
            //title= (TextView) itemView.findViewById(R.id.title);
            from= (TextView) itemView.findViewById(R.id.from);

            background= (ImageView) itemView.findViewById(R.id.image_bg);
            background.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }

        public void update(final int position) {
            this.position = position;
            //title.setText(list.get(position).getTitle());
            from.setText(list.get(position).getVideosource());
            ImageLoader.getInstance().displayImage(list.get(position).getCover(),background,options);
            showView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showView.setVisibility(View.GONE);
                    if (click != null)
                        click.onclick(position);
                }
            });
        }
    }

    private onClick click;

    public void setClick(onClick click){
        this.click=click;
    }

    public static interface onClick{
        void onclick(int position);
    }
}
