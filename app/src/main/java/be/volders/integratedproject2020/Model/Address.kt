package be.volders.integratedproject2020.Model

class Address(

    var lat: Double,
    var lon: Double
    ){
    override fun toString(): String {
        return "Lat: $lat  ,Lon: $lon"
    }
}

