package fr.isen.m1.devlogiciel.projetdevmobile.model

data class PisteRemonteeModel(
    val piste: PisteModel,
    val remontee: RemonteeModel,
    val state: Int
)

data class PistesRemonteesModel(
    val pistesRemontees: List<PisteRemonteeModel>
)