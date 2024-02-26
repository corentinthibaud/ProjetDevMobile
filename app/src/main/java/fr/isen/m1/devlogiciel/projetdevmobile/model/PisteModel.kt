package fr.isen.m1.devlogiciel.projetdevmobile.model

data class PisteModel (
    val name: String,
    val color: PisteColorEnum,
    val state: PisteStateEnum,
    val status: Int
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