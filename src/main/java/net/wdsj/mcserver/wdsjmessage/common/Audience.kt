package net.wdsj.mcserver.wdsjmessage.common

import net.wdsj.mcserver.wdsjmessage.common.cell.MessageCell
import net.wdsj.mcserver.wdsjmessage.common.msg.Message

interface Audience<T> {

    val user: T


    fun send(type: QueuedCell.Type, message: Message<*>)

    fun isActive() : Boolean

    override fun hashCode(): Int

    override fun equals(other: Any?): Boolean

}