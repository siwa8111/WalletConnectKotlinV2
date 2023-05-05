package com.walletconnect.lab.compose

import android.app.Application
import com.walletconnect.android.Core
import com.walletconnect.android.CoreClient
import com.walletconnect.android.relay.ConnectionType
import com.walletconnect.web3.modal.client.Modal
import com.walletconnect.web3.modal.client.Web3Modal

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val serverUri = "wss://relay.walletconnect.com?projectId=${BuildConfig.PROJECT_ID}"
        val appMetaData = Core.Model.AppMetaData(
            name = "Kotlin Dapp",
            description = "Kotlin Dapp Implementation",
            url = "kotlin.dapp.walletconnect.com",
            icons = listOf("https://gblobscdn.gitbook.com/spaces%2F-LJJeCjcLrr53DcT1Ml7%2Favatar.png?alt=media"),
            redirect = "kotlin-dapp-wc:/request"
        )

        CoreClient.initialize(
            relayServerUrl = serverUri,
            connectionType = ConnectionType.AUTOMATIC,
            application = this,
            metaData = appMetaData,
        ) {

        }

//        val initParams = Sign.Params.Init(core = CoreClient)

        Web3Modal.initialize(Modal.Params.Init(CoreClient),
            {}) { error ->
//            Timber.e(tag(this), error.throwable.stackTraceToString())
        }
    }
}