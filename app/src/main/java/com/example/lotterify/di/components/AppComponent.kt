package com.example.lotterify.di.components

import com.example.lotterify.database.UsersDatabase
import com.example.lotterify.di.modules.DatabaseModule
import dagger.Component

@Component(modules = [DatabaseModule::class])
interface AppComponent {

    fun provideUsersRepository() : UsersDatabase

}