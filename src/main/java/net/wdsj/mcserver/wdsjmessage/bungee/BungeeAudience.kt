package net.wdsj.mcserver.wdsjmessage.bungee

import net.md_5.bungee.api.connection.ProxiedPlayer
import net.wdsj.mcserver.wdsjmessage.common.Audience
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.msg.Message

class BungeeAudience(override val user: ProxiedPlayer) : Audience<ProxiedPlayer> {
    override fun send(type: QueuedCell.Type, message: Message<*>) {
        if (!user.isConnected) return
        when (type) {
            QueuedCell.Type.ACTION_BAR -> {
                val textComponent = message.content as? String ?: return
                user.sendMessage(textComponent)
            }
            QueuedCell.Type.TITLE -> {
                val textComponent = message.content as? String ?: return
                user.sendMessage(textComponent)
            }
            QueuedCell.Type.SUBTITLE -> {
                val textComponent = message.content as? String ?: return
                user.sendMessage(textComponent)
            }
            QueuedCell.Type.MESSAGE -> {
            }
        }
    }

    override fun isActive(): Boolean {
        return user.isConnected
    }


    override fun hashCode(): Int {
        return user.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Audience<*>) return false
        return user == other.user
    }

}