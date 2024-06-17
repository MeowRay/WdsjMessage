package net.wdsj.mcserver.wdsjmessage.common.cell

import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.msg.Message
import net.wdsj.mcserver.wdsjmessage.common.msg.TextComponentMessage

class EmptyTextMessageCell private constructor(val durationMillis: Long) : MessageCell<TextComponent> {

    private var time: Long = 0


    override fun isActive(): Boolean {
        return time < durationMillis

    }

    override fun take(intervalMillis: Long): Message<TextComponent> {
        time += intervalMillis
        return TextComponentMessage(TextComponent(""))
    }


    data class Builder private constructor(
        var durationMillis: Long = 0
    ) {

        fun setDurationMillis(durationMillis: Long) = apply {
            this.durationMillis = durationMillis
        }

        fun build(): EmptyTextMessageCell {
            return EmptyTextMessageCell(durationMillis)
        }

        companion object {
            @JvmStatic
            fun newBuilder(block: Builder.() -> Unit): Builder {
                return Builder().apply(block)
            }
        }
    }

}