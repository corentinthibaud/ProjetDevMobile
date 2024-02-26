package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PisteRemonteeModel(
    @SerializedName("piste") val piste: PisteModel,
    @SerializedName("remontee") val remontee: RemonteeModel,
    @SerializedName("state") val state: Int
): Serializable

data class PistesRemonteesModel(
    val pistesRemontees: List<PisteRemonteeModel>
)