package com.example.lotterify.main.model

import com.example.lotterify.database.User

sealed class UserDataState {
    data class EXISTING(val user : User) : UserDataState()
    data class NEW(val user: User) : UserDataState()
    data class ERROR(val error : Throwable) : UserDataState()
    object LOADING : UserDataState()
}
