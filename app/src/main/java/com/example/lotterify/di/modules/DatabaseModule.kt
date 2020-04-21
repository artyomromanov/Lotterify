package com.example.lotterify.di.modules

import android.content.Context
import com.example.lotterify.database.UsersDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(private val context: Context) {

    @Provides
    fun provideDatabase() : UsersDatabase{
        return UsersDatabase.getInstance(context)
    }

}
