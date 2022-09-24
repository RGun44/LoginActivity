package com.example.loginactivity.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(note: User)

    @Update
    fun updateUser(note: User)

    @Delete
    fun deleteUser(note: User)

    @Query("SELECT * FROM user")
    fun getUser() : List<User>

    @Query("SELECT * FROM user WHERE id =:register_id")
    fun getUser(register_id: Int) : User

    @Query("SELECT * FROM user WHERE username = :register_name and password = :register_password")
    fun getUser(register_name: String, register_password: String) : User
}