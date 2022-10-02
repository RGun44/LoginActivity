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
    suspend fun getPakets() : List<Paket>

    @Query("SELECT * FROM paket WHERE id =:paketId")
    fun getPaket(paketId: Int): List<Paket>
}