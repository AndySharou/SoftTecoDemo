package com.example.andrew.softtecodemo;

/**
 * Created by Andrew on 06.01.2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;


    public class ViewHolder {

        public TextView textPostId, textTitlePost, textBody;
    }

    private GridItems[] items;
    private LayoutInflater mInflater;

    public GridAdapter(Context context, GridItems[] locations) {

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        items = locations;

    }

    public GridItems[] getItems() {
        return items;
    }

    public void setItems(GridItems[] items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.length;
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            return items[position];
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            return items[position].id;
        }
        return 0;
    }

    public void setItemsList(GridItems[] locations) {
        this.items = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {

            view = mInflater.inflate(R.layout.custom_grid_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textPostId = (TextView) view
                    .findViewById(R.id.post_id);

            viewHolder.textTitlePost = (TextView) view
                    .findViewById(R.id.title);
            viewHolder.textBody = (TextView) view
                    .findViewById(R.id.body);


            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GridItems gridItems = items[position];

        setText(viewHolder, gridItems.postId, gridItems.title, gridItems.body);
        return view;
    }


    private void setText(ViewHolder viewHolder, String postId, String title, String body ) {

        viewHolder.textPostId.setText(postId);

        viewHolder.textTitlePost.setText(title);
        viewHolder.textBody.setText(body);
    }
}