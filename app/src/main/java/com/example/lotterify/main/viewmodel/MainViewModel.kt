package com.example.lotterify.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotterify.database.User
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.error.DrawsError
import com.example.lotterify.error.UserDatabaseError
import com.example.lotterify.main.model.LoadingDrawsState
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.network.DrawsRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewModel(private val drawsRepository: DrawsRepository, private val userDataRepository: UserDataRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val resultData = MutableLiveData<LoadingDrawsState>()
    private val userData = MutableLiveData<UserDataState>()
    private val UIState = MutableLiveData<UIState>()

    //Currently signed in user in Accounts section.
    var signedInOAuthUser: String? = null
    var signedInLotterifyUser: User? = null

    fun getResultsData() = resultData as LiveData<LoadingDrawsState>
    fun getUserData() = userData as LiveData<UserDataState>
    fun getUIStateData() = UIState as LiveData<UIState>


    //Gets the Draws online
    fun fetchNumbers() {
        resultData.value = LoadingDrawsState.IN_PROGRESS
        viewModelScope.launch {
            try {
                val result = drawsRepository.fetchNumbers()
                resultData.value = LoadingDrawsState.SUCCESS(result)
            } catch (error: DrawsError) {
                resultData.value = LoadingDrawsState.ERROR(error)
            } finally {
                //logic for completed loading
            }
        }
    }

    fun findUser(email: String) {
        userData.value = UserDataState.Loading
        viewModelScope.launch {
            delay(1000)
            compositeDisposable.add(
                userDataRepository
                    .findUser(email)
                    .subscribe(
                        { userData.value = UserDataState.Existing(it) }, //Success
                        {
                            if (it.message!!.contains("empty result")) { //Empty query - user not found
                                userData.value = UserDataState.NotFound
                            } else {
                                userData.value = UserDataState.Error(UserDatabaseError(it.message ?: "Unknown Error", it.cause)) //Error of some other kind
                            }
                        })
            )
        }
    }

    fun addUser(user: User) {
        userData.value = UserDataState.Loading
        viewModelScope.launch {
            delay(1000)
            compositeDisposable.add(
                userDataRepository
                    .addUser(user)
                    .subscribe({
                        userData.value = UserDataState.New(user)
                    },{
                        userData.value = UserDataState.Error(it)
                    })
            )
        }
    }
    fun removeAllUsersTESTONLY() {
        compositeDisposable.add(
            userDataRepository
                .removeAllUsers()
                .subscribe({ userData.value = UserDataState.Deleted("Deleted all!") }, { error -> userData.value = UserDataState.Error(error) })
        )
    }

    fun setUIState(state: UIState) {
        UIState.value = state
    }
}