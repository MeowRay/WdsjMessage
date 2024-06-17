package net.wdsj.mcserver.wdsjmessage.bukkit

import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.MessageQueueManager
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.cell.CountDownTextMessageCell
import net.wdsj.mcserver.wdsjmessage.common.util.CountDownStyle
import net.wdsj.servercore.compatible.XSound
import org.bukkit.entity.Player


object Messages{


    @JvmStatic
    @JvmOverloads
    fun Player.sendSubtitleCountdown(
        priority: Byte = QueuedCell.Priority.NORMAL,
        millis: Long,
        prefix: String,
        suffix: String = "",
        style: CountDownStyle = CountDownStyle.LINE_AND_TIME,
        finish: () -> Unit = {}
    ): QueuedCell {
        return listOf(this).sendCountdown(
            QueuedCell.Type.SUBTITLE,
            priority,
            millis,
            prefix,
            suffix,
            style,
            finish
        )
    }

    @JvmStatic
    @JvmOverloads
    fun Player.sendActionbarCountdown(
        priority: Byte = QueuedCell.Priority.NORMAL,
        millis: Long,
        prefix: String,
        suffix: String = "",
        style: CountDownStyle = CountDownStyle.LINE_AND_TIME,
        finish: () -> Unit = {}
    ): QueuedCell {
        return listOf(this).sendCountdown(
            QueuedCell.Type.ACTION_BAR,
            priority,
            millis,
            prefix,
            suffix,
            style,
            finish
        )

    }

    @JvmStatic
    @JvmOverloads
    fun Collection<Player>.sendCountdown(
        type: QueuedCell.Type,
        priority: Byte,
        millis: Long,
        prefix: String,
        suffix: String,
        style: CountDownStyle,
        finish: () -> Unit
    ): QueuedCell {
        return MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
            setCountdownDuration(millis)
            countdownStyle = style
            startText = TextComponent(prefix)
            endText = TextComponent(suffix)
            countdownFinish = {
                finish()
                forEach {
                    XSound.BLOCK_NOTE_BLOCK_HARP.play(it, 0.8F, 0.8F)
                }
            }
        }.build()) {
            this.priority = priority
            this.type = type
            this.audiences.addAll(map { BukkitAudience.of(it) })
        }

    }
}