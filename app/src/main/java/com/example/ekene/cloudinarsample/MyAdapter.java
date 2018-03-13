package com.example.ekene.cloudinarsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by EKENE on 3/2/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListClass> listClass;
    private Context context;

    public MyAdapter(List<ListClass> listClass, Context context) {
        this.listClass = listClass;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        ListClass listClassItems = listClass.get(position);
        Picasso.with(context).load(listClassItems.getImageUrl()).into(holder.display_imageView);
        holder.display_title.setText(listClassItems.getTitle());
        holder.display_price.setText(listClassItems.getPprice());
    }

    @Override
    public int getItemCount() {
        return listClass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView display_imageView;
        private TextView display_title;
        private TextView display_price;

        public ViewHolder(View itemView) {
            super(itemView);
            display_imageView = itemView.findViewById(R.id.display_imageView);
            display_title = itemView.findViewById(R.id.display_title);
            display_price = itemView.findViewById(R.id.display_price);

        }
    }
}
