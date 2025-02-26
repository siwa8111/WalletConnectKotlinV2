package com.walletconnect.android.verify.client

import com.walletconnect.android.internal.common.di.verifyModule
import com.walletconnect.android.internal.common.scope
import com.walletconnect.android.internal.common.wcKoinApp
import com.walletconnect.android.verify.data.VerifyService
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.core.KoinApplication

internal class VerifyClient(private val koinApp: KoinApplication = wcKoinApp) : VerifyInterface {
    private val verifyService get() = koinApp.koin.get<VerifyService>()

    override fun initialize(verifyUrl: String?) {
        koinApp.modules(verifyModule(verifyUrl))
    }

    override fun register(attestationId: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun resolve(attestationId: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        scope.launch {
            supervisorScope {
                try {
                    val response = verifyService.resolveAttestation(attestationId)
                    if (response.isSuccessful && response.body() != null) {
                        onSuccess(response.body()!!.origin)
                    } else {
                        onError(IllegalArgumentException(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
}