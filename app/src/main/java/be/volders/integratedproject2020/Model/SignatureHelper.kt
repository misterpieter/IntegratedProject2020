package be.volders.integratedproject2020.Model;

import java.util.*

data class SignatureHelper(
        val housenumber: Integer,
        val road: String,
        val town: String,
        val country: String,
        val postcode: Integer,
        val neighbrourhood: String,
        val timestamp: Date,
        val longitude: Integer,
        val lattitude: Integer,
        val fkstudent: String


) {
    override fun toString(): String {
        return "SignatureHelper(housenumber=$housenumber, road='$road', town='$town', country='$country', postcode=$postcode, neighbrourhood='$neighbrourhood', timestamp=$timestamp, longitude=$longitude, lattitude=$lattitude, fkstudent='$fkstudent')"
    }
}
