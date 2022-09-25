package com.example.loginactivity.room

import androidx.room.*

@Dao
interface PaketDao {
    @Insert
    fun addPaket(paket: Paket)

    @Update
    fun updatePaket(paket: Paket)

    @Delete
    fun deletePaket(paket: Paket)

    @Query("SELECT * FROM paket")
    fun getPaket(): List<Paket>
}