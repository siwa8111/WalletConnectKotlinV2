package com.walletconnect.web3.modal.data.explorer

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import com.walletconnect.web3.modal.network.model.ExplorerWalletResponse

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class MapToList

internal class ExplorerMapToListAdapter {

    @MapToList
    @FromJson
    fun fromJson(json: Map<String, ExplorerWalletResponse>): List<ExplorerWalletResponse> {
        return json.map { it.value }.toList()
    }

    @ToJson
    fun toJson(@MapToList value: List<ExplorerWalletResponse>): Map<String, ExplorerWalletResponse> {
        return value.associateBy { it.id }
    }
}
