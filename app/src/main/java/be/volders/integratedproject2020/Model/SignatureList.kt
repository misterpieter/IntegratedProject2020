package be.volders.integratedproject2020.Model

import android.graphics.Bitmap

class SignatureList(
        var imageByteArray: Bitmap,
        var dbRoad: String,
        var dbHouseNubmer: Int,
        var dbPostCode: Int,
        var dbTown: String,
        var dbNeibhourhood: String,
        var dbCountry: String,
        var dbDatum: String,
        var dbsignatureId: Int,
        var dbSuspisious : Boolean
) {
    override fun toString(): String {
        return "SignatureList( dbRoad='$dbRoad', dbHouseNubmer=$dbHouseNubmer, dbPostCode=$dbPostCode, dbTown='$dbTown', dbNeibhourhood='$dbNeibhourhood', dbCountry='$dbCountry', dbDatum='$dbDatum')"
    }
}