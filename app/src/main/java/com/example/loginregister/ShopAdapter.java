package com.example.loginregister;


import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder>{
    private Context mCtx;
    private List<Shop> shopList;
    private Location current;

    public ShopAdapter(Context mCtx, List<Shop> shopList,Location currentLocation){
        this.mCtx = mCtx;
        this.shopList = shopList;
        this.current=new Location("");
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.details_layout, null);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop=shopList.get(position);
        holder.shopName.setText(shop.getName());
        holder.price_distance.setText("Price: "+shop.getPrice());
        holder.specialOffers.setText(shop.getSpecialOffers());

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
    public double getDistance(Location current,double shopLat,double shopLong){
        double distance = 0;
        Location shopLocation=new Location("");
        // shopLocation.setLatitude(shopLat);
        // distance=current.distanceTo(shopLocation);
        return distance;

    }

    class ShopViewHolder extends RecyclerView.ViewHolder{
        TextView shopName, price_distance, specialOffers;
        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName=itemView.findViewById(R.id.shopName);
            price_distance=itemView.findViewById(R.id.price_distance);
            specialOffers=itemView.findViewById(R.id.specialOffers);
        }
    }
}
