package fr.isen.m1.devlogiciel.projetdevmobile.model

data class RemonteeModel (
    val name: String,
    val status: Int,
    val type: RemonteeTypeEnum
)

data class RemonteesModel (
    val remontees: List<RemonteeModel>
)

enum class RemonteeTypeEnum {
    TELESKI,
    TELESIEGE,
    TELECABINE
}