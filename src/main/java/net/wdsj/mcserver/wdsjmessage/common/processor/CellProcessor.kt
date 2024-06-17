package net.wdsj.mcserver.wdsjmessage.common.processor

import net.wdsj.mcserver.wdsjmessage.common.Audience
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.processor.DefaultProcessor.AudienceMessage

interface CellProcessor {

    fun process(ticksPeriod : Long , cells: List<QueuedCell>) : Map<Audience<*>, AudienceMessage>


}