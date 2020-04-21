package com.example.lotterify.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lotterify.INITIAL_BALANCE
import com.example.lotterify.database.User
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.error.DrawsError
import com.example.lotterify.error.UserDatabaseError
import com.example.lotterify.main.model.LoadingDrawsState
import com.example.lotterify.main.model.UserDataState
import com.example.lotterify.network.DrawsRepository
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch


class MainViewModel(private val drawsRepository: DrawsRepository, private val userDataRepository: UserDataRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val resultData = MutableLiveData<LoadingDrawsState>()
    private val userData = MutableLiveData<UserDataState>()
    private val UIState = MutableLiveData<UIState>()

    //Currently signed in user in Accounts section.
    var signedInUser: String? = null

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
        userData.value = UserDataState.LOADING

        compositeDisposable.add(
            userDataRepository
                .findUser(email)
                .subscribe(
                    { userData.value = UserDataState.EXISTING(it) }, //Success
                    {
                        if (it.message!!.contains("empty result")) { //Empty query - user not found
                            userData.value = UserDataState.NOTFOUND
                        } else {
                            userData.value = UserDataState.ERROR(UserDatabaseError(it.message ?: "Unknown Error", it.cause)) //Error of some other kind
                        }
                    })
        )
    }

    fun addUser(email: String) {
        userData.value = UserDataState.LOADING
        val newUser = User(email, INITIAL_BALANCE)
        compositeDisposable.add(
            userDataRepository
                .addUser(newUser)
                .subscribe({
                    userData.value = UserDataState.NEW(newUser)
                }, {
                    userData.value = UserDataState.ERROR(it)
                })
        )
    }

    fun removeAllUsersTESTONLY() {
        compositeDisposable.add(
            userDataRepository
                .removeAllUsers()
                .subscribe({ userData.value = UserDataState.DELETED("Deleted all!") }, { error -> userData.value = UserDataState.ERROR(error) })
        )
    }

    fun setUIState(state: UIState) {
        UIState.value = state
    }
}