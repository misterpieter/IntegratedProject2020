package be.volders.integratedproject2020.Model

import java.sql.Date
import java.time.LocalDate

class Address(

    var lat: Double,
    var lon: Double,
    var date: Date,
    var fkSnumber: String

    ){
    override fun toString(): String {
        return "Lat: $lat  ,Lon: $lon"
    }
}

