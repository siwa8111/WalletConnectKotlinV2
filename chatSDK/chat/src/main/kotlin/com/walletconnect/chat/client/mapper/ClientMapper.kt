@file:JvmSynthetic

package com.walletconnect.chat.client.mapper

import com.walletconnect.chat.client.Chat
import com.walletconnect.chat.engine.model.EngineDO

//TODO: Figure out what to do with models separation
@JvmSynthetic
internal fun Chat.Params.Invite.toInviteEngineDO(): EngineDO.Invite {
    return EngineDO.Invite(invite.account.value, invite.message, invite.signature)
}

@JvmSynthetic
internal fun Chat.Params.Message.toMessageEngineDO() : EngineDO.SendMessage {
    return EngineDO.SendMessage(message, media.toMediaEngineDO())
}

@JvmSynthetic
private fun Chat.Model.Media.toMediaEngineDO() : EngineDO.Media {
    return EngineDO.Media(type, data)
}