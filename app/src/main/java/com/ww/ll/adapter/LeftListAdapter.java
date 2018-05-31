package com.ww.ll.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ww.ll.R;
import com.ww.ll.db.LeftList;

import java.util.List;

/**
 *
 * @author Ww
 * @date 2018/5/14
 */
public class LeftListAdapter extends ArrayAdapter<LeftList>{

    private int resourceId;

    public LeftListAdapter(@NonNull Context context, int resource, @NonNull List<LeftList> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LeftList list = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.left_list_image);
            viewHolder.textView = view.findViewById(R.id.left_list_name);
            viewHolder.imageView.setColorFilter(Color.parseColor("#000000"));
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        assert list != null;
        viewHolder.imageView.setImageResource(list.getImageId());
        viewHolder.textView.setText(list.getName());
        return view;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
