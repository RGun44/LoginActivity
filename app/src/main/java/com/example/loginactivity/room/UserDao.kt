package com.example.loginactivity.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addNote(note: User)
    @Update
    suspend fun updateNote(note: User)
    @Delete
    suspend fun deleteNote(note: User)
    @Query("SELECT * FROM user")
    suspend fun getNotes() : List<User>
    @Query("SELECT * FROM user WHERE username =:username")
    suspend fun getNote(username: String) : List<User>
}