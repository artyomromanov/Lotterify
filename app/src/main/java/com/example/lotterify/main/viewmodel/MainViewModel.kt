package com.example.lotterify.main.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotterify.INITIAL_BALANCE
import com.example.lotterify.database.User
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.database.UserDataRepositoryImpl
import com.example.lotterify.database.UsersDatabase
import com.example.lotterify.error.DrawsError
import com.example.lotterify.error.UserDatabaseError
import com.example.lotterify.main.model.*
import com.example.lotterify.network.DrawsRepository
import com.example.lotterify.network.RepositoryEuroMillionsImpl
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch


class MainViewModel(private val drawsRepository: DrawsRepository, private val userDataRepository: UserDataRepository) : ViewModel() {

    private val euroRepository: DrawsRepository = RepositoryEuroMillionsImpl()

    private val compositeDisposable = CompositeDisposable()

    private val resultData = MutableLiveData<LoadingDrawsState>()
    private val userData = MutableLiveData<UserDataState>()

    var signedInUser : String? = null

    fun getResultsData() = resultData as LiveData<LoadingDrawsState>
    fun getUserData() = userData as LiveData<UserDataState>

    fun fetchNumbers() {
        resultData.value = LoadingDrawsState.IN_PROGRESS
        viewModelScope.launch {
                try {
                    val result = euroRepository.fetchNumbers()
                    resultData.value = LoadingDrawsState.SUCCESS(result)
                } catch (error: DrawsError){
                    resultData.value = LoadingDrawsState.ERROR(error)
                } finally {
                    //logic for completed loading
                }
        }
    }

    fun findUser(email : String){
        userData.value = UserDataState.LOADING

        compositeDisposable.add(
            userDataRepository
                .getUser(email)
                .subscribe (
                    {userData.value = UserDataState.EXISTING(it)},
                    {userData.value = UserDataState.ERROR(UserDatabaseError(it.message ?: "Unknown Error", it.cause))
                })
        )
    }

    fun addUser(email : String){
        userData.value = UserDataState.LOADING
        val newUser = User(email, INITIAL_BALANCE)
        compositeDisposable.add(
            userDataRepository
                .addUser(newUser)
                .subscribe({
                    userData.value = UserDataState.NEW(newUser)
                },{
                    userData.value = UserDataState.ERROR(it)
                })
        )
    }
}