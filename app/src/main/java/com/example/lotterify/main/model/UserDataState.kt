package com.example.lotterify.main.model

import com.example.lotterify.database.User

sealed class UserDataState {
    object Loading : UserDataState()
    object NotFound : UserDataState()
    data class Existing(val user : User) : UserDataState()
    data class New(val user: User) : UserDataState()
    data class Deleted(val message : String) : UserDataState()
    data class Error(val error : Throwable) : UserDataState()
}
