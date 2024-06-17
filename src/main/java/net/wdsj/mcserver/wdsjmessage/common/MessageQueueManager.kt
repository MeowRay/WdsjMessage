package net.wdsj.mcserver.wdsjmessage.common

import net.wdsj.mcserver.wdsjmessage.common.cell.MessageCell
import net.wdsj.mcserver.wdsjmessage.common.processor.DefaultProcessor
import net.wdsj.mcserver.wdsjmessage.common.processor.CellProcessor
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object MessageQueueManager {

    private val messageQueue: CopyOnWriteArrayList<QueuedCell> = CopyOnWriteArrayList()
    private val uuidQueue: ConcurrentHashMap<UUID, QueuedCell> = ConcurrentHashMap()

    val cellProcessors: MutableList<CellProcessor> = mutableListOf()

    lateinit var messagePlatform: MessagePlatform

    init {
        cellProcessors.add(DefaultProcessor())

    }

    fun initializePlatform(platform: MessagePlatform) {
        this.messagePlatform = platform
        platform.initializePlatform()
    }

    fun enqueueMessage(cell: MessageCell<*>, block: QueuedCell.() -> Unit): QueuedCell {
        val queuedCell = QueuedCell(cell)
        queuedCell.block()
        messageQueue.add(queuedCell)
        uuidQueue[queuedCell.uuid] = queuedCell
        return queuedCell
    }


    fun dequeueMessage(queuedCell: QueuedCell): Boolean {
        val remove = messageQueue.remove(queuedCell)
        uuidQueue.remove(queuedCell.uuid)
        return remove
    }

    fun dequeueMessage(uuid: UUID)  : Boolean {
        val remove = uuidQueue.remove(uuid)
        if (remove != null) {
            messageQueue.remove(remove)
        }
        return remove != null
    }


    fun getQueuedCell(uuid: UUID): QueuedCell? {
        return uuidQueue[uuid]
    }

    fun addProcessor(processor: CellProcessor) {
        cellProcessors.add(processor)
    }

    fun removeProcessor(processor: CellProcessor) {
        cellProcessors.remove(processor)
    }

    fun listQueuedCell(): List<QueuedCell> {
        return messageQueue
    }


}
