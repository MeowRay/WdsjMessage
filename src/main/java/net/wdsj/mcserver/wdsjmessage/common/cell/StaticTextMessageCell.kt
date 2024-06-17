package net.wdsj.mcserver.wdsjmessage.common.cell

import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.msg.Message
import net.wdsj.mcserver.wdsjmessage.common.msg.TextComponentMessage

class StaticTextMessageCell private constructor(
    val message: TextComponent,
    private val isActiveSupplier: () -> Boolean
) : MessageCell<TextComponent> {

    override fun isActive(): Boolean {
        return isActiveSupplier.invoke()
    }

    override fun take(intervalMillis: Long): Message<TextComponent> {
        return TextComponentMessage(message)
    }

    data class Builder private constructor(
        var message: TextComponent = TextComponent(""),
        var isActiveSupplier: () -> Boolean = { true }
    ) {

        fun setMessage(message: TextComponent) = apply {
            this.message = message
        }

        fun setIsActiveSupplier(isActiveSupplier: () -> Boolean) = apply {
            this.isActiveSupplier = isActiveSupplier
        }

        fun build(): StaticTextMessageCell {
            return StaticTextMessageCell(message, isActiveSupplier)
        }

        companion object {
            @JvmStatic
            fun newBuilder(block: Builder.() -> Unit): Builder {
                return Builder().apply(block)
            }
        }
    }
}
