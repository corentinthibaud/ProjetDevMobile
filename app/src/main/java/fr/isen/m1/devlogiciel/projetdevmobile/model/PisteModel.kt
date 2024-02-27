package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
data class PisteModel (
    @SerializedName("name") val name: String? = null,
    @SerializedName("color") val color: PisteColorEnum? = null,
    @SerializedName("state") val state: PisteStateEnum? = null,
    @SerializedName("status") val status: Int? = null
)

@IgnoreExtraProperties
data class PistesModel (
    val pistes: List<PisteModel>? = null
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