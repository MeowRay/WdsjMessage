package net.wdsj.mcserver.wdsjmessage.common.msg

interface Message<T> {

    val content: T

    fun merge(message: Message<T>) : Message<T>




}