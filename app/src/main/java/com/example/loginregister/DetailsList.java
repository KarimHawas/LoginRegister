package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsList extends AppCompatActivity {
    private static final String PRODUCT_SHOP_URL="http://192.168.0.194/LoginRegister/product_shop.php";
    private static final String SHOP_URL="http://192.168.0.194/LoginRegister/shop.php";
    private int product_id;
    private String name;
    private String description;
    private String image_url;
    Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    TextView productName, productDescription;
    Button sortPriceBtn, sortDistanceBtn;
    ImageView productImage;

    List<Shop> shopList;
    RecyclerView recyclerView;

    int shop_id;
    String shopName, specialOffers;
    double price,latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        Intent intent=getIntent();
        product_id=intent.getExtras().getInt("product_id");
        name=intent.getExtras().getString("productName");
        description=intent.getExtras().getString("productDescription");
        image_url=intent.getExtras().getString("image_url");

        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        sortPriceBtn=findViewById(R.id.sortPriceBtn);
        sortDistanceBtn=findViewById(R.id.sortDistanceBtn);
        productImage=findViewById(R.id.productImage);

        productName.setText(name);
        productDescription.setText(description);
        Glide.with(getApplicationContext()).load(image_url).into(productImage);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopList = new ArrayList<>();
        currentLocation=new Location("");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        RequestPermission();
        loadShops();


    }
    private void loadShops(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, PRODUCT_SHOP_URL+"?param1="+product_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray productShops = new JSONArray(response);

                    for(int i=0;i<productShops.length();i++){
                        JSONObject productShop=productShops.getJSONObject(i);
                        shop_id=productShop.getInt("shop_id");
                        price=productShop.getDouble("price");
                        specialOffers=productShop.getString("specialOffers");
                        Shop shopFinal=new Shop(shop_id,null,price,specialOffers,0,0);
                        shopList.add(shopFinal);
                    }
                    fetchLastLocation();
                    ShopAdapter adapter = new ShopAdapter(DetailsList.this, shopList,currentLocation);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsList.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void fetchLastLocation() {

        if (ActivityCompat.checkSelfPermission(DetailsList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(DetailsList.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLocation.setLatitude(location.getLatitude());
                    currentLocation.setLongitude(location.getLongitude());
                }
            }
        });

    }
    private void RequestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

}