package net.wdsj.mcserver.wdsjmessage.bukkit

import mc233.cn.wdsjlib.bukkit.WdsjLib
import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.Audience
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.msg.Message
import org.bukkit.entity.Player

class BukkitAudience private constructor(override val user: Player) : Audience<Player> {

    override fun send(type: QueuedCell.Type, message: Message<*>) {
        if (!user.isOnline) return
        when (type) {
            QueuedCell.Type.ACTION_BAR -> {
                val textComponent = message.content as? TextComponent ?: return
                WdsjLib.getInstance().titleAPI.sendActionbar(user, textComponent.toLegacyText())
            }

            QueuedCell.Type.TITLE -> {
                val textComponent = message.content as? TextComponent ?: return
                WdsjLib.getInstance().titleAPI.sendTitle(user, textComponent.toLegacyText(), 1, 20, 1)
            }

            QueuedCell.Type.SUBTITLE -> {
                val textComponent = message.content as? TextComponent ?: return
                WdsjLib.getInstance().titleAPI.sendSubtitle(user, textComponent.toLegacyText(), 1, 20, 1)
            }

            QueuedCell.Type.MESSAGE -> {
            }
        }
    }


    override fun isActive(): Boolean {
        return user.isOnline
    }

    override fun hashCode(): Int {
        return user.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Audience<*>) return false
        return user == other.user
    }

    companion object {

        @JvmStatic
        fun of(player: Player): BukkitAudience {
            return BukkitAudience(player)
        }

    }


}