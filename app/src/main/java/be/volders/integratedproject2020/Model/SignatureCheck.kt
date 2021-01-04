package be.volders.integratedproject2020.Model

class SignatureCheck (
        var imageId: String?,
        var imageByteArray: ByteArray,
        var fkStudent: String,
        var releaseCounter: Int,
        var vectorCounter: Int
)