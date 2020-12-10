package be.volders.integratedproject2020.Admin

import java.time.LocalDate

class AddressWithIdFirebase(
        var addressId: Int,
        var lat: Double,
        var lon: Double,
        var date: LocalDate,
        var fkSnumber: String,

        var road: String? = "NO road",
        var houseNumber: Int? = 0,
        var postcode: Int? = 0,
        var town: String? = "NO town",
        var neighbourhood: String? = "NO neighbourhood",
        var county: String? = "NO county"

    ){
    override fun toString(): String {
        return "${road} ${houseNumber}\n${postcode} ${town} (${neighbourhood})\n${county}"
    }
}