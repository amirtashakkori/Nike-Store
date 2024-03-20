package com.example.nikestore.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nikestore.Model.Product;

@Database(version = 1 , exportSchema = false ,  entities = Product.class)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase;

    public static AppDataBase getAppDataBase(Context c) {
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(c.getApplicationContext() , AppDataBase.class , "db_products")
                    .allowMainThreadQueries()
                    .build();
        }
        return appDataBase;
    }

    public abstract DBDao getDataBaseDao();

}
