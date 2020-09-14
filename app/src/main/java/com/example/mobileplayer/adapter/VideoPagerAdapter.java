package com.example.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileplayer.R;
import com.example.mobileplayer.domain.MediaItem;
import com.example.mobileplayer.pager.VideoPager;
import com.example.mobileplayer.utils.Utils;

import java.util.ArrayList;

public class VideoPagerAdapter extends BaseAdapter {

    private final ArrayList<MediaItem> mediaItems;
    private Context context;
    private Utils utils;
    private boolean isVideo;

    public VideoPagerAdapter(Context context , ArrayList<MediaItem> mediaItems ,boolean isVideo){
        this.context = context;
        this.mediaItems = mediaItems;
        this.isVideo = isVideo;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = View.inflate(context, R.layout.item_video_pager, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = view.findViewById(R.id.iv_icon);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            viewHolder.tv_size = view.findViewById(R.id.tv_size);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position得到列表中对应位置的视频数据
        MediaItem mediaItem = mediaItems.get(i);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(android.text.format.Formatter.formatFileSize(context, mediaItem.getSize()));
        viewHolder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
        if(!isVideo) viewHolder.iv_icon.setImageResource(R.drawable.music_default_bg);
        return view;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
