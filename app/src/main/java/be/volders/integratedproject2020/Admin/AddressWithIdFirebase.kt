package be.volders.integratedproject2020.Admin

import java.time.LocalDate

class AddressWithIdFirebase(
        var addressId: Int,
        var lat: Double,
        var lon: Double,
        var date: LocalDate,
        var fkSnumber: String

    ){
    override fun toString(): String {
        return "Lat: $lat  ,Lon: $lon"
    }
}

