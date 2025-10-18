package example.com.totalnba.data.network.model

import com.google.gson.annotations.SerializedName

data class NbaPlayerDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("personId")
    val personId: Int,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("teamAbbreviation")
    val teamAbbreviation: String?,
    @SerializedName("position")
    val position: String?,
    @SerializedName("rosterStatus")
    val rosterStatus: Int
)
