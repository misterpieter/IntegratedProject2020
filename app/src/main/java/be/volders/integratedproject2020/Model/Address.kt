package be.volders.integratedproject2020.Model

import java.time.LocalDate

class Address(

        var lat: Double,
        var lon: Double,
        var date: LocalDate,
        var fkSnumber: String,

        var road: String? = "NO road",
        var houseNumber: Int? = 0,
        var postcode: Int? = 0,
        var town: String? = "NO town",
        var neighbourhood: String? = "NO neighbourhood",
        var county: String? = "NO neighbourhood"

    ){
    override fun toString(): String {
        return "${road} ${houseNumber}\n${postcode} ${town} (${neighbourhood})\n${county}"
    }
}



/*

class Address (
        val road: String? = "NO road",
        val houseNumber: Int? = 0,
        val postcode: Int? = 0,
        val town: String? = "NO town",
        val neighbourhood: String? = "NO neighbourhood",
        val county: String? = "NO neighbourhood"
){
    override fun toString(): String {
        return "${road} ${houseNumber}\n${postcode} ${town} (${neighbourhood})\n${county}"
    }
}

*/


