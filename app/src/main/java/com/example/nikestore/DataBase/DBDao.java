package com.example.nikestore.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nikestore.Model.Product;

import java.util.List;

@Dao
public interface DBDao {
    @Insert
    long addProduct(Product product);

    @Query("select * from tbl_products")
    List<Product> getFavoriteProductList();

}
