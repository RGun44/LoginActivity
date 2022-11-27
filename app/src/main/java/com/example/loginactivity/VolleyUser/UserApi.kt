package com.example.loginactivity.VolleyUser

class UserApi {
    companion object{
        val BASE_URL ="http://192.168.100.36/paketinDBLaravel/public/api/"
        val GET_ALL_URL = BASE_URL + "profile/"
        val GET_BY_ID_URL = BASE_URL + "profile/"
        val ADD_URL = BASE_URL + "profile"
        val UPDATE_URL = BASE_URL + "profile/"
        val DELETE_URL = BASE_URL + "profile/"
    }
}