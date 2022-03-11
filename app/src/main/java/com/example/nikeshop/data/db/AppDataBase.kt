package com.example.nikeshop.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.data.repo.product.ProductLocalDataSource

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productDao(): ProductLocalDataSource


}