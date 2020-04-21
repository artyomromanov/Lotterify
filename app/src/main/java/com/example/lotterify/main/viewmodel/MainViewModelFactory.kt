package com.example.lotterify.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.network.DrawsRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val drawsRepository : DrawsRepository, private val userDataRepository: UserDataRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(drawsRepository, userDataRepository) as T
    }
}