package com.example.lotterify.util

import android.app.Application
import com.example.lotterify.di.components.AppComponent
import com.example.lotterify.di.components.DaggerAppComponent
import com.example.lotterify.di.modules.DatabaseModule

class LotterifyApplication : Application(){

    val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(applicationContext))
            .build()
    }
}