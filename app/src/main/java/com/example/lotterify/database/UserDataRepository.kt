package com.example.lotterify.database

import io.reactivex.Completable
import io.reactivex.Single

interface UserDataRepository {

    fun addUser(user : User) : Completable

    fun removeUser(user: User) : Completable

    fun removeAllUsers() : Completable

    fun findUser(user: String) : Single<User>

}