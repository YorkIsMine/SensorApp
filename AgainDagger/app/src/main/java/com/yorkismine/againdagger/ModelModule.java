package com.yorkismine.againdagger;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {
    @Provides
    public Model getModel(){
        return new Model();
    }
}
