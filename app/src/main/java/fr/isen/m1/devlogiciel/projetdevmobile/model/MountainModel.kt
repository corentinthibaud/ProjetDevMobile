package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MountainModel (
    @SerializedName("name") val name: String? = null,
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("type") val type: MoutainTypeEnum? = null
): Serializable {
    companion object {
        enum class MoutainTypeEnum(val string: String) {
            TELESKI("teleski"),
            TELESIEGE("telesiege")
        }
    }
}

data class MountainsModel (
    val remontees: List<MountainModel>? = null
)
