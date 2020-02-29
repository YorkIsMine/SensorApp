package com.yorkismine.againdagger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerModelComponent.create().inject(this);

        TextView tv = findViewById(R.id.text_view);
        tv.setText(model.sayName());
    }
}
