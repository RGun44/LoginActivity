package com.example.loginactivity.Volley.entity

class Daerah (var name: String, var jarak: Float, var est_ongkir: Int) {

    companion object {
        @JvmField
        var listOfDaerah = arrayOf(
            Daerah("Bali", 2.0f ,300000),
            Daerah("Yogyakarta", 2.5f,400000),
            Daerah("Jakarta", 3.0f,500000),
            Daerah("Bandung", 3.5f,600000),
            Daerah("Surabaya", 4.0f,700000),
            Daerah("Batam", 4.5f,800000),
            Daerah("Semarang",5.0f,900000),
            Daerah("Samarinda",5.5f,1000000),
            Daerah("Medan",6.5f,1100000),
            Daerah("Aceh",7.0f,1200000)
        )
    }
}