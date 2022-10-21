package com.walletconnect.wallet

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EchoServerService {

    @POST("clients")
    suspend fun clients(@Body pushClientsBody: PushBody): Response<PushResponse>
}