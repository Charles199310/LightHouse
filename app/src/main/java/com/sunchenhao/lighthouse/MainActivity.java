package com.sunchenhao.lighthouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunchenhao.lighthouse.annotation.Router;

@Router(path = RouterPath.MAIN)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App_Router.inject();
        Sub_Router.inject();
        findViewById(R.id.btn_sub_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LightHouse.getInstance().getActivityClass("SubMain")));
            }
        });
    }
}
