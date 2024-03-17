package com.example.nikestore.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.airbnb.lottie.L;
import com.example.nikestore.Model.Product;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface ProductDataBase {

    @Insert
    Long addToList(Product product);

    @Query("select * from products")
    List<Product> getProducts();

    @Delete
    int delete(Product product);

    @Query("DELETE FROM products")
    void deleteAll();

}
