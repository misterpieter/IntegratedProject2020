package be.volders.integratedproject2020.Model

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

