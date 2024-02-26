package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName

data class RemonteeModel (
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: Int,
    @SerializedName("type") val type: RemonteeTypeEnum
)

data class RemonteesModel (
    val remontees: List<RemonteeModel>
)

enum class RemonteeTypeEnum {
    TELESKI,
    TELESIEGE,
    TELECABINE
}