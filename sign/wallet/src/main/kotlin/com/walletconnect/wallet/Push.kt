package com.walletconnect.wallet


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PushBody(
    @Json(name = "client_id")
    val clientId: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "token")
    val token: String
)

@JsonClass(generateAdapter = true)
data class PushResponse(
    @Json(name = "status")
    val status: String
)