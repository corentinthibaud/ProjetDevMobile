package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName

data class PisteModel (
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: PisteColorEnum,
    @SerializedName("state") val state: PisteStateEnum,
    @SerializedName("status") val status: Int
)

data class PistesModel (
    val pistes: List<PisteModel>
)

enum class PisteColorEnum {
    GREEN,
    RED,
    BLUE,
    BLACK
}

enum class PisteStateEnum {
    UNREPORTED,
}