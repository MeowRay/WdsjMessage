package net.wdsj.mcserver.wdsjmessage.common.cell

import net.wdsj.mcserver.wdsjmessage.common.msg.Message

interface MessageCell<T> {


    fun isActive(): Boolean

    // val type : Type

    fun take(intervalMillis: Long): Message<T>



}