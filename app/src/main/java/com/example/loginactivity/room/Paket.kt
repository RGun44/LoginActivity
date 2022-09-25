package com.example.loginactivity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Paket (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val daerahAsal: String,
    val daerahTujuan: String,
    val beratPaket: Int,
    val kecepatan: String,
)