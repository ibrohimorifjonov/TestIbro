package uz.test.api.dto

data class GeneralInfo(
    val data: GeneralInfoPObject
)
data class GeneralInfoPObject(
    val typeBonusName:String,
    val currentQuantity:Double,
    val forBurningQuantity:Double,
    val dateBurning:String
)