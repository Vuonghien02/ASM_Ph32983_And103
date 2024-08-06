// CartActivity.java
package com.duyle.asm1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listViewCart;
    private CartAdapter cartAdapter;
    private List<CarModel> cartList;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartList = getIntent().getParcelableArrayListExtra("cartList");
        cartAdapter = new CartAdapter(this, cartList);
        listViewCart.setAdapter(cartAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                Intent homeIntent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            } else if (item.getItemId() == R.id.navigation_cart) {
                return true;
            } else if (item.getItemId() == R.id.navigation_completed_orders) {
                Intent completedOrdersIntent = new Intent(CartActivity.this, CompletedOrdersActivity.class);
                startActivity(completedOrdersIntent);
                return true;
            }
            return false;
        });

        btnCheckout.setOnClickListener(v -> showCheckoutDialog());
    }

    private void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thanh toán");

        String[] paymentMethods = {"Thẻ tín dụng", "Paypal", "Tiền mặt"};
        builder.setItems(paymentMethods, (dialog, which) -> {
            String selectedMethod = paymentMethods[which];
            handlePayment(selectedMethod);
        });

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            // Thực hiện thanh toán
            handlePayment("Unknown");
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handlePayment(String method) {
        Toast.makeText(this, "Đơn hàng đã được thanh toán bằng: " + method, Toast.LENGTH_SHORT).show();

        // Chuyển đến màn hình đơn hàng đã đặt
        Intent completedOrdersIntent = new Intent(CartActivity.this, CompletedOrdersActivity.class);
        startActivity(completedOrdersIntent);
    }
}
