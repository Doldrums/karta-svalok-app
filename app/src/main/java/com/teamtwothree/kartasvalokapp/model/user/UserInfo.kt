package com.teamtwothree.kartasvalokapp.model.user

import android.arch.persistence.room.Entity

@Entity(tableName = "user_info")
data class UserInfo (val name: String,
                    val email: String,
                    val phone: String)