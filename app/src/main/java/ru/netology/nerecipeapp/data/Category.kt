package ru.netology.nerecipeapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
enum class Category : Parcelable {
    European,
    Asian,
    Panasian,
    Oriental,
    American,
    Russian,
    Mediterranean
}