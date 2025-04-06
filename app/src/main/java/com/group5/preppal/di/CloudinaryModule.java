package com.group5.preppal.di;

import com.group5.preppal.BuildConfig;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class CloudinaryModule {
    @Provides
    @Singleton
    public Cloudinary provideCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", BuildConfig.CLOUDINARY_NAME);
        config.put("api_key", BuildConfig.CLOUDINARY_API_KEY);
        config.put("api_secret", BuildConfig.CLOUDINARY_API_SECRET);

        return new Cloudinary(config);
    }
}
