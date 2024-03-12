package fr.isen.m1.devlogiciel.projetdevmobile.model

import java.io.Serializable

data class SlopeModel (
    val name: String? = null,
    val color: SlopeColorEnum? = null,
    val state: SlopeStateEnum? = null,
    val status: Boolean? = null
): Serializable {
    companion object {
        enum class SlopeColorEnum (val string: String){
            GREEN("Green"),
            RED("Red"),
            BLUE("Blue"),
            BLACK("Black")
        }

        enum class SlopeStateEnum(val string: String) {
            UNREPORTED("Unreported")
        }
    }
}

data class SlopesModel (
    val pistes: List<SlopeModel>? = null
): Serializable