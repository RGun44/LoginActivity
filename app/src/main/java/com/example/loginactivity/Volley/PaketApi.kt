package com.example.loginactivity.Volley

class PaketApi {
    companion object{
        val BASE_URL ="http://10.5.251.13/paketinDBLaravel/public/api/"
        val GET_ALL_URL = BASE_URL + "paket/"
        val GET_BY_ID_URL = BASE_URL + "paket/"
        val ADD_URL = BASE_URL + "paket"
        val UPDATE_URL = BASE_URL + "paket/"
        val DELETE_URL = BASE_URL + "paket/"
    }
}