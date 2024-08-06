package com.duyle.asm1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView lvMain;
    private List<CarModel> listCarModel;
    private List<CarModel> cartList = new ArrayList<>();
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnAddCar = findViewById(R.id.btnAddCar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        lvMain = findViewById(R.id.listviewMain);

        btnAddCar.setOnClickListener(v -> showAddCarDialog());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Kiểm tra ID của item được chọn
            if (item.getItemId() == R.id.navigation_home) {
                // Khởi động lại MainActivity khi chọn "Home"
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true; // Đã xử lý item này

            } else if (item.getItemId() == R.id.navigation_cart) {
                // Khởi động CartActivity khi chọn "Cart"
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                cartIntent.putParcelableArrayListExtra("cartList", new ArrayList<>(cartList));
                startActivity(cartIntent);
                return true; // Đã xử lý item này
            }

            // Trả về false nếu item không được xử lý
            return false;
        });



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<List<CarModel>> call = apiService.getCars();
        call.enqueue(new Callback<List<CarModel>>() {
            @Override
            public void onResponse(Call<List<CarModel>> call, Response<List<CarModel>> response) {
                if (response.isSuccessful()) {
                    listCarModel = response.body();
                    carAdapter = new CarAdapter(MainActivity.this, listCarModel);
                    lvMain.setAdapter(carAdapter);

                    lvMain.setOnItemClickListener((parent, view, position, id) -> {
                        CarModel car = listCarModel.get(position);
                        showCarDetailDialog(car);
                    });
                } else {
                    Log.e("MainActivity", "Response Error: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<CarModel>> call, Throwable t) {
                Log.e("MainActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    private void showAddCarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_car, null);
        builder.setView(dialogView);

        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtNamSX = dialogView.findViewById(R.id.edtNamSX);
        EditText edtHang = dialogView.findViewById(R.id.edtHang);
        EditText edtGia = dialogView.findViewById(R.id.edtGia);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String ten = edtTen.getText().toString();
            int namSX = Integer.parseInt(edtNamSX.getText().toString());
            String hang = edtHang.getText().toString();
            double gia = Double.parseDouble(edtGia.getText().toString());
            String imageUrl = edtImageUrl.getText().toString();

            CarModel newCar = new CarModel(ten, namSX, hang, gia, imageUrl);
            addCarToServer(newCar);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addCarToServer(CarModel car) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<CarModel> call = apiService.addCar(car);
        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful()) {
                    listCarModel.add(response.body());
                    carAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Thêm xe thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Lỗi khi thêm xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi khi thêm xe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCarDetailDialog(CarModel car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_car_details, null);
        builder.setView(dialogView);

        ImageView imgCarDetail = dialogView.findViewById(R.id.imgCarDetail);
        EditText edtTenDetail = dialogView.findViewById(R.id.edtTenDetail);
        EditText edtNamSXDetail = dialogView.findViewById(R.id.edtNamSXDetail);
        EditText edtHangDetail = dialogView.findViewById(R.id.edtHangDetail);
        EditText edtGiaDetail = dialogView.findViewById(R.id.edtGiaDetail);
        TextView tvPrice = dialogView.findViewById(R.id.tvPrice);
        TextView tvQuantity = dialogView.findViewById(R.id.tvQuantity);
        Button btnDecrease = dialogView.findViewById(R.id.btnDecrease);
        Button btnIncrease = dialogView.findViewById(R.id.btnIncrease);
        Button btnAddToCart = dialogView.findViewById(R.id.btnAddToCart);

        edtTenDetail.setText(car.getTen());
        edtNamSXDetail.setText(String.valueOf(car.getNamSX()));
        edtHangDetail.setText(car.getHang());
        edtGiaDetail.setText(String.valueOf(car.getGia()));

        // Load image from URL using Glide
        String imageUrl = car.getImageUrl();
        if (!imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_login)
                    .into(imgCarDetail);
        }

        tvPrice.setVisibility(View.VISIBLE);
        double carPrice = car.getGia();
        tvPrice.setText("Giá: " + carPrice);

        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                tvPrice.setText("Giá: " + (carPrice * quantity));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            tvPrice.setText("Giá: " + (carPrice * quantity));
        });

        btnAddToCart.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            for (int i = 0; i < quantity; i++) {
                cartList.add(car);
            }
            Toast.makeText(MainActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        builder.setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
