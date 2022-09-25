package com.example.loginactivity.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: UserDB)

    @Query("SELECT * FROM user")
    fun getUser() : List<User>

    @Query("SELECT * FROM user WHERE id =:register_id")
    fun getUser(register_id: Int) : User
}