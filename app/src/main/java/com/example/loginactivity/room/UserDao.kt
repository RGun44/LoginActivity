package com.example.loginactivity.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user")
    fun getUser(): List<User>

    @Query("SELECT * FROM user WHERE id =:user_id")
    fun getUser(user_id: Int): User

    @Query("SELECT * FROM user WHERE username = :user_name and password = :user_password")
    fun getUser(user_name: String, user_password: String): User
}