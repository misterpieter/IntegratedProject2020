package be.volders.integratedproject2020.Model

import java.time.LocalDate

class exportdata(
        val snumber: String,
        val name: String,
        val lastname: String,
        var date: String,
        var road: String? = "NO road",
        var houseNumber: Int? = 0,
        var postcode: Int? = 0,
        var town: String? = "NO town",
        var neighbourhood: String? = "NO neighbourhood",
        var county: String? = "NO neighbourhood"
) {
    override fun toString(): String {
        return "exportdata(name='$name', lastname='$lastname', snumber='$snumber', date=$date, road=$road, houseNumber=$houseNumber, postcode=$postcode, town=$town, neighbourhood=$neighbourhood, county=$county)"
    }
}