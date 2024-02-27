package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PisteRemonteeModel(
    @SerializedName("piste") val piste: PisteModel? = null,
    @SerializedName("remontee") val remontee: RemonteeModel? = null,
    @SerializedName("state") val state: Boolean? = null
): Serializable

data class PistesRemonteesModel(
    val pistesRemontees: List<PisteRemonteeModel>? = null
)