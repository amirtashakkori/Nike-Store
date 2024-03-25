package com.example.nikestore.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nikestore.Model.Product;

@Database(version = 1 , exportSchema = false , entities = {Product.class})
public abstract class ProductDataBaseClass extends RoomDatabase {
    private static ProductDataBaseClass appDataBase;

    public static ProductDataBaseClass getAppDataBase(Context c) {
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(c.getApplicationContext() , ProductDataBaseClass.class , "product")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDataBase;
    }

    public abstract ProductDataBase getDataBaseDao();
}
