package ru.netology.nerecipeapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Recipe(
    val id: Long,
    val author: String,
    val category: Category,
    val name: String,
    val description: String,
    val time: String,
    val recipe: String,
    val like: Boolean = false,
    val picture: String = ""
) : Parcelable