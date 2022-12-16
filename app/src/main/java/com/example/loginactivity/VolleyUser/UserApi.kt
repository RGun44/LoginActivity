package com.example.loginactivity.VolleyUser

class UserApi {
    companion object{
        val BASE_URL ="http://10.5.251.13/paketinDBLaravel/public/api/"
        val GET_ALL_URL = BASE_URL + "profile/"
        val GET_BY_ID_URL = BASE_URL + "profile/"
        val ADD_URL = BASE_URL + "register"
        val UPDATE_URL = BASE_URL + "profile/"

        val LOGIN = BASE_URL + "loginCheck"
    }
}