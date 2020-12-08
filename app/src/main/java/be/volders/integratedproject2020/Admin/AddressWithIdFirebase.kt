package be.volders.integratedproject2020.Admin

import java.sql.Date

class AddressWithIdFirebase(
    var addressId : Int,
    var lat: Double,
    var lon: Double,
    var date: Date,
    var fkSnumber: String

    ){
    override fun toString(): String {
        return "Lat: $lat  ,Lon: $lon"
    }
}

