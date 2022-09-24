package com.example.loginactivity.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(note: User)
    @Update
    suspend fun updateUser(note: User)
    @Delete
    suspend fun deleteUser(note: User)
    @Query("SELECT * FROM user")
    suspend fun getNotes() : List<User>
    @Query("SELECT * FROM user WHERE username =:username")
    suspend fun getNote(username: String) : List<User>
}