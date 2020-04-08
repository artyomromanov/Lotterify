package com.example.lotterify.database

import com.example.lotterify.database.User
import com.example.lotterify.database.UserDataRepository
import com.example.lotterify.database.UsersDatabase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDataRepositoryImpl(private val database : UsersDatabase) : UserDataRepository {

    override fun addUser(user: User): Completable {
        return database
            .getUsersDao()
            .addUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun removeUser(user: User): Completable {
        return database
            .getUsersDao()
            .removeUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUser(user: String): Single<User> {
        return database
            .getUsersDao()
            .getUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}