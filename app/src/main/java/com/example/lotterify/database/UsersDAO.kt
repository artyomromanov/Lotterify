package com.example.lotterify.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User) : Completable

    @Query("SELECT * FROM users WHERE username = :user")
    fun getUser(user : String): Single<User>

    @Delete
    fun removeUser(user: User) : Completable

    //test purposes only
    @Query("DELETE FROM users")
    fun removeAllUsers() : Completable

}