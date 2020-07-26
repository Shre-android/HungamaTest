package com.e.hungamatest.model.db.pojo


import androidx.paging.PagedList
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Cast(
    @SerializedName("cast_id")
    @Expose
    val castId: Int?=null,
    @SerializedName("credit_id")
    @Expose
    val creditId: String? = "",
    @SerializedName("gender")
    @Expose
    val gender: Int? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("name")
    @Expose
    val name: String? = "",

    @SerializedName("order")
    @Expose
    val order: Int? = null,

    @SerializedName("profile_path")
    @Expose
    val profilePath: String? = "")



data class Crew(

    @SerializedName("credit_id")
    @Expose
    val creditId: String? = "",

    @SerializedName("department")
    @Expose
    val department: String? = "",

    @SerializedName("gender")
    @Expose
    val gender: Int? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("job")
    @Expose
    val job: String? = "",

    @SerializedName("name")
    @Expose
    val name: String? = "",

    @SerializedName("profile_path")
    @Expose
    val profilePath: String? = "")



    data class CastAndCrew (
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("cast")
    @Expose
    val cast: List<Cast>? = null,

    @SerializedName("crew")
    @Expose
    val crew: List<Crew>? = null)














