package com.e.hungamatest.model.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.e.hungamatest.app.Hungama
import com.e.hungamatest.model.db.pojo.Movie
import hungamatest.model.db.MovieDao


private const val DB_NAME = "movies"

@Database(entities = [Movie::class],version = 1)
abstract class AppDatabase: RoomDatabase()
{
    abstract fun getMovieDao(): MovieDao
}

object DBProvider{
    val db by lazy {
        Room.databaseBuilder(Hungama.appContext, AppDatabase::class.java, DB_NAME).build()
    }
}