package com.example.loginactivity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey
    val username: String,
    val password: String,
    val email: String,
    val phonenumber: String,
    val birthdate: String
)