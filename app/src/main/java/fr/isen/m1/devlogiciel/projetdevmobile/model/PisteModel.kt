package fr.isen.m1.devlogiciel.projetdevmobile.model

import java.io.Serializable

data class PisteModel (
    val name: String? = null,
    val color: PisteColorEnum? = null,
    val state: PisteStateEnum? = null,
    val status: Boolean? = null
): Serializable {
    companion object {
        enum class PisteColorEnum (val string: String){
            GREEN("Green"),
            RED("Red"),
            BLUE("Blue"),
            BLACK("Black")
        }

        enum class PisteStateEnum(val string: String) {
            UNREPORTED("Unreported")
        }
    }
}

data class PistesModel (
    val pistes: List<PisteModel>? = null
): Serializable