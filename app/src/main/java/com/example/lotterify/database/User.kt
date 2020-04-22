package com.example.lotterify.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    @ColumnInfo(name = "username")
    val username : String,
    val balance : Double,
    val currency : String

)
