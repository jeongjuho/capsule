package com.example.my.recapsule;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FollowAdapter extends BaseAdapter{

    public List<FollowInfoClass> falist;
    private LayoutInflater mInflater;

    public FollowAdapter(Context context,ArrayList<FollowInfoClass> itemList) {
        this.mInflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.mInflater = mInflater;
        this.falist = itemList;
    }


    public FollowAdapter() {

    }


    @Override
    public int getCount() {
        return falist.size();
    }


    @Override
    public Object getItem(int position) {
        return falist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.follow_item, parent, false);
            holder = new ViewHolder();
            holder.id = convertView.findViewById(R.id.flid);
            holder.title = convertView.findViewById(R.id.fltitle);
            holder.text =  convertView.findViewById(R.id.fltext);
            holder.mainid = convertView.findViewById(R.id.mainimg);
            holder.profile = convertView.findViewById(R.id.froimg);
            //holder.addr=(TextView)convertView.findViewById(R.id.addr);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.profile.setBackground(new ShapeDrawable(new OvalShape()));
        holder.profile.setClipToOutline(true);
        holder.id.setText(falist.get(position).getId());
        holder.title.setText(falist.get(position).getTitle());
        holder.text.setText(falist.get(position).getText());
        Glide.with(context).load(falist.get(position).getMainimg()).into(holder.mainid);
        Glide.with(context).load(falist.get(position).getFroimg()).into(holder.profile);
        return convertView;
    }

    private class ViewHolder {

        TextView id;
        TextView title;
        TextView text;
        ImageView mainid;
        ImageView profile;
    }
}
