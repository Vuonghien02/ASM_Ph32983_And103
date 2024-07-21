package com.duyle.asm1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView lvMain;
    private List<CarModel> listCarModel;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnAddCar = findViewById(R.id.btnAddCar);

        btnAddCar.setOnClickListener(v -> showAddCarDialog());

        lvMain = findViewById(R.id.listviewMain);

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
}
