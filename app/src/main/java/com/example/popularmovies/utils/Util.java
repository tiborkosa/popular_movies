package com.example.popularmovies.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Util {

    public static String getProperty(String key, Context ctx) throws IOException {
        Properties properties = new Properties();
        AssetManager assetManager = ctx.getAssets();
        InputStream is = assetManager.open("config.properties");
        properties.load(is);
        return properties.getProperty(key);
    }

}
