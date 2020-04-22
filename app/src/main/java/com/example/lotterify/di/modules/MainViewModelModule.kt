package com.example.lotterify.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.database.UserDataRepositoryImpl
import com.example.lotterify.database.UsersDatabase
import com.example.lotterify.MainActivity
import com.example.lotterify.main.viewmodel.MainViewModel
import com.example.lotterify.main.viewmodel.MainViewModelFactory
import com.example.lotterify.network.DrawsRepository
import com.example.lotterify.network.RepositoryEuroMillionsImpl
import dagger.Module
import dagger.Provides

@Module
class MainViewModelModule(private val activity: MainActivity) {

    @Provides
    fun provideDrawsRepository(): DrawsRepository {
        return RepositoryEuroMillionsImpl()
    }

    @Provides
    fun provideUserDataRepository(database: UsersDatabase): UserDataRepository {
        return UserDataRepositoryImpl(database)
    }

    @Provides
    fun provideMainViewModelFactory(drawsRepository: DrawsRepository, userDataRepository: UserDataRepository): MainViewModelFactory {
        return MainViewModelFactory(drawsRepository, userDataRepository)
    }

    @Provides
    fun provideMainViewModel(factory: MainViewModelFactory): MainViewModel {
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }

}
