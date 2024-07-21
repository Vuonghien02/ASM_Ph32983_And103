package com.duyle.asm1;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarAdapter extends BaseAdapter {

    private List<CarModel> carModelList;
    private Context context;

    public CarAdapter(Context context, List<CarModel> carModelList) {
        this.context = context;
        this.carModelList = carModelList;
    }

    @Override
    public int getCount() {
        return carModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return carModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_car, viewGroup, false);

        TextView tvID = rowView.findViewById(R.id.tvId);
        ImageView imgAvatar = rowView.findViewById(R.id.imgAvatatr);  // Sửa tên ID nếu cần
        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvNamSX = rowView.findViewById(R.id.tvNamSX);
        TextView tvHang = rowView.findViewById(R.id.tvHang);
        TextView tvGia = rowView.findViewById(R.id.tvGia);
        ImageView btnEdit = rowView.findViewById(R.id.btnEdit);
        ImageView btnDelete = rowView.findViewById(R.id.btnDelete);

        CarModel car = carModelList.get(position);

        tvID.setText(car.get_id());
        tvName.setText(car.getTen());
        tvNamSX.setText(String.valueOf(car.getNamSX()));
        tvHang.setText(car.getHang());
        tvGia.setText(String.valueOf(car.getGia()));

        // Tải ảnh từ URL
        Picasso.get().load(car.getImageUrl()).into(imgAvatar);

        // Sự kiện khi nhấn nút delete
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa xe")
                    .setMessage("Bạn có chắc chắn muốn xóa xe này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        deleteCarFromServer(car.get_id(), position);
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        // Sự kiện khi nhấn nút edit
        btnEdit.setOnClickListener(v -> {
            showEditCarDialog(car);
        });

        return rowView;
    }

    private void deleteCarFromServer(String carId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<Void> call = apiService.deleteCar(carId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    carModelList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa xe thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi xóa xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi xóa xe", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditCarDialog(CarModel car) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_car, null);
        builder.setView(dialogView);

        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtNamSX = dialogView.findViewById(R.id.edtNamSX);
        EditText edtHang = dialogView.findViewById(R.id.edtHang);
        EditText edtGia = dialogView.findViewById(R.id.edtGia);
        EditText edtImageUrl = dialogView.findViewById(R.id.edtImageUrl);

        edtTen.setText(car.getTen());
        edtNamSX.setText(String.valueOf(car.getNamSX()));
        edtHang.setText(car.getHang());
        edtGia.setText(String.valueOf(car.getGia()));
        edtImageUrl.setText(car.getImageUrl());

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String ten = edtTen.getText().toString();
            int namSX = Integer.parseInt(edtNamSX.getText().toString());
            String hang = edtHang.getText().toString();
            double gia = Double.parseDouble(edtGia.getText().toString());
            String imageUrl = edtImageUrl.getText().toString();

            car.setTen(ten);
            car.setNamSX(namSX);
            car.setHang(hang);
            car.setGia(gia);
            car.setImageUrl(imageUrl);

            updateCarOnServer(car);
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateCarOnServer(CarModel car) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        Call<CarModel> call = apiService.updateCar(car.get_id(), car);

        call.enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                if (response.isSuccessful()) {
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cập nhật xe thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi cập nhật xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(context, "Lỗi khi cập nhật xe", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
