package com.example.yogadarma.angkotku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button sopir, penumpang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sopir = findViewById(R.id.btnDriver);
        penumpang = findViewById(R.id.btnCustomer);

        sopir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SopirActivity.class);
                startActivity(intent);

                return;
            }
        });

        penumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PenumpangActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
