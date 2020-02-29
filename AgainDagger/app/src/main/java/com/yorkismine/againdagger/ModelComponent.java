package com.yorkismine.againdagger;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Component;

@Component(modules = {ModelModule.class})
public interface ModelComponent {
    void inject(MainActivity activity);
}
