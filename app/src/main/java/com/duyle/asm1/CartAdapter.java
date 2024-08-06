package com.duyle.asm1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private List<CarModel> cartList;

    public CartAdapter(Context context, List<CarModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder();
            holder.imgCartItem = convertView.findViewById(R.id.imgCartItem);
            holder.tvCartItemName = convertView.findViewById(R.id.tvCartItemName);
            holder.tvCartItemPrice = convertView.findViewById(R.id.tvCartItemPrice);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CarModel car = cartList.get(position);
        holder.tvCartItemName.setText(car.getTen());
        holder.tvCartItemPrice.setText(String.valueOf(car.getGia()));

        String imageUrl = car.getImageUrl();
        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_login)
                    .into(holder.imgCartItem);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView imgCartItem;
        TextView tvCartItemName;
        TextView tvCartItemPrice;
    }
}
