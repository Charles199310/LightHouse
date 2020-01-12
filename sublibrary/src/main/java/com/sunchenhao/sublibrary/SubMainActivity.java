package com.sunchenhao.sublibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunchenhao.lighthouse.LightHouse;
import com.sunchenhao.lighthouse.annotation.Router;

@Router(path = "SubMain")
public class SubMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_main);
        findViewById(R.id.btn_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubMainActivity.this, LightHouse.getInstance().getActivityClass("main")));
            }
        });
    }
}
