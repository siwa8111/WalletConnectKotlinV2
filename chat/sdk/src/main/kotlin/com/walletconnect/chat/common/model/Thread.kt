@file:JvmSynthetic

package com.walletconnect.chat.common.model

import com.walletconnect.foundation.common.model.Topic

internal data class Thread(
    val topic: Topic,
    val selfAccount: AccountId,
    val peerAccount: AccountId,
)