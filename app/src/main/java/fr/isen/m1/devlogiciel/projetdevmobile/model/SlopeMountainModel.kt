package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SlopeMountainModel(
    @SerializedName("piste") val slope: SlopeModel? = null,
    @SerializedName("remontee") val mountain: MountainModel? = null,
    @SerializedName("state") val state: Int? = null
): Serializable

data class SlopesMountainsModel(
    val slopesMountains: List<SlopeMountainModel>? = null
)