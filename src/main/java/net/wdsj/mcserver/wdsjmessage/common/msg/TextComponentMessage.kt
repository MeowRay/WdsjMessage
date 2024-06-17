package net.wdsj.mcserver.wdsjmessage.common.msg

import net.md_5.bungee.api.chat.TextComponent

class TextComponentMessage(override val content: TextComponent) : Message<TextComponent> {

    private val joinerFoo = TextComponent(" ")

    override fun merge(message: Message<TextComponent>): Message<TextComponent> {
        return TextComponentMessage(TextComponent(content, joinerFoo, message.content))
    }

}