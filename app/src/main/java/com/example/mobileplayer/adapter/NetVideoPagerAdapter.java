package com.example.mobileplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileplayer.R;
import com.example.mobileplayer.domain.MediaItem;
import com.example.mobileplayer.domain.NetMediaItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NetVideoPagerAdapter extends BaseAdapter {

    private  Context context;
    private final ArrayList<NetMediaItem> mediaItems;

    public NetVideoPagerAdapter(Context context, ArrayList<NetMediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;
        if(convertView ==null){
            convertView = View.inflate(context, R.layout.item_net_video_pager,null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon =  convertView.findViewById(R.id.iv_icon);
            viewHoder.tv_name =  convertView.findViewById(R.id.tv_name);
            viewHoder.tv_desc =  convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) convertView.getTag();
        }

        //根据position得到列表中对应位置的数据
        NetMediaItem mediaItem = mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getMovieName());
        viewHoder.tv_desc.setText(mediaItem.getVideoTitle());

        //3.使用Picasso 请求图片
        Picasso.get().load(mediaItem.getCoverImg())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.video_default_icon)
                .error(R.drawable.video_default_icon)
                .into(viewHoder.iv_icon);


        return convertView;
    }


    static class ViewHoder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }

}