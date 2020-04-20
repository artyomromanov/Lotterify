package com.example.lotterify.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun getUsersDao() : UsersDAO

    companion object {

        private const val DATABASE_NAME = "db.users"

        private var instance: UsersDatabase? = null

        private fun create(context: Context): UsersDatabase =
            Room
                .databaseBuilder(context, UsersDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context : Context) : UsersDatabase = (instance ?: create(context)).also { instance = it }

    }
}