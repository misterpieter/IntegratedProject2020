package be.volders.integratedproject2020.Model

class SignatureHelper(
        var imageId: String?,
        var imageByteArray: ByteArray,
        var fkStudent: String,
        var locationLink: String,
        var releaseCounter: Int,
        var vectorCounter: Int,
        var suspicion : Boolean
)