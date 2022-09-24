package com.example.loginactivity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Paket (
    @PrimaryKey
    val username: String,
    val password: String,
    val email: String,
    val phonenumber: String,
    val birthdate: String
)