package com.example.loginactivity.Volley

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Paket (
    @PrimaryKey(autoGenerate = true)
    var id: String,
    var daerahAsal: String,
    var daerahTujuan: String,
    var beratPaket: String,
    var kecepatan: String,
)