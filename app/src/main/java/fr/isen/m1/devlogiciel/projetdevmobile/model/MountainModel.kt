package fr.isen.m1.devlogiciel.projetdevmobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MountainModel (
    @SerializedName("name") val name: String? = null,
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("type") val type: MountainTypeEnum? = null,
    @SerializedName("comments") var comments: List<CommentModel>? = null
): Serializable {
    companion object {
        enum class MountainTypeEnum(val string: String) {
            TELESKI("teleski"),
            TELESIEGE("telesiege")
        }
    }
}

data class MountainsModel (
    val mountains: List<MountainModel>? = null
)
