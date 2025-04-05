package com.example.sqldemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,

    val firstName : String?,
    val lastName : String?
)


