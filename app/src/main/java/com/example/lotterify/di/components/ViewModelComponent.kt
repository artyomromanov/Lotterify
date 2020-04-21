package com.example.lotterify.di.components

import com.example.lotterify.di.modules.MainViewModelModule
import com.example.lotterify.main.view.MainActivity
import dagger.Component

@Component(modules = [MainViewModelModule::class], dependencies = [AppComponent::class])
interface ViewModelComponent {

    fun injectActivity(activity: MainActivity)

}