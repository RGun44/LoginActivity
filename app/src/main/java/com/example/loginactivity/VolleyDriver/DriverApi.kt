package com.example.loginactivity.VolleyDriver

class DriverApi {
    companion object{
        val BASE_URL ="http://192.168.100.13/paketinDBLaravel/public/api/"
        val GET_ALL_URL = BASE_URL + "driver/"
        val GET_BY_ID_URL = BASE_URL + "driver/"
        val ADD_URL = BASE_URL + "driver"
        val UPDATE_URL = BASE_URL + "driver/"
        val DELETE_URL = BASE_URL + "driver/"
    }
}