package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName

data class RemonteeModel (
    @SerializedName("name") val name: String? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("type") val type: RemonteeTypeEnum? = null
)

data class RemonteesModel (
    val remontees: List<RemonteeModel>? = null
)

enum class RemonteeTypeEnum {
    TELESKI,
    TELESIEGE,
    TELECABINE
}