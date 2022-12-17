package com.example.loginactivity.VolleyDonasi

class DonasiApi {
    companion object{
        val BASE_URL ="http://192.168.100.13/paketinDBLaravel/public/api/"
        val GET_ALL_URL = BASE_URL + "donasi/"
        val GET_BY_ID_URL = BASE_URL + "donasi/"
        val ADD_URL = BASE_URL + "donasi"
        val UPDATE_URL = BASE_URL + "donasi/"
        val DELETE_URL = BASE_URL + "donasi/"
    }
}