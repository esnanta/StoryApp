package com.esnanta.storyapp.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    var email: String = "",
    var password: String = "",
    var token: String = "",
    var isLogin: Boolean = false,
) : Parcelable
