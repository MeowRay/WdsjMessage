package net.wdsj.mcserver.wdsjmessage.bukkit

import mc233.cn.wdsjlib.bukkit.utils.extensions.sendActionbar
import mc233.cn.wdsjlib.bukkit.utils.extensions.sendMessage
import net.md_5.bungee.api.chat.TextComponent
import net.wdsj.mcserver.wdsjmessage.common.MessageQueueManager
import net.wdsj.mcserver.wdsjmessage.common.QueuedCell
import net.wdsj.mcserver.wdsjmessage.common.cell.CountDownTextMessageCell
import net.wdsj.mcserver.wdsjmessage.common.cell.StaticTextMessageCell
import net.wdsj.mcserver.wdsjmessage.common.util.CountDownStyle
import net.wdsj.servercore.common.command.WdsjCommand
import net.wdsj.servercore.common.command.anntations.GlobalCommand
import net.wdsj.servercore.common.command.anntations.SubCommand
import net.wdsj.servercore.utils.extensions.replacePlaceholder
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author  MeowRay
 * @date  2022/4/28 15:26
 * @version 1.0
 */
@GlobalCommand(permission = "wdsjlib.message")
class MessageCommand : WdsjCommand<CommandSender> {

    @SubCommand
    fun broadcast(sender: CommandSender, args: Array<String>) {
        val t = TextComponent(args.joinToString(separator = " "))
        Bukkit.getOnlinePlayers().forEach {
            it.sendMessage(t)
        }
    }

    @SubCommand
    fun broadcastRP(sender: CommandSender, args: Array<String>) {
        val t = TextComponent(args.joinToString(separator = " ").replacePlaceholder(null))
        Bukkit.getOnlinePlayers().forEach {
            it.sendMessage(t)
        }
    }

    @SubCommand
    fun tell(sender: CommandSender, receiver: String, args: Array<String>) {
        val player = Bukkit.getPlayerExact(receiver)?.takeIf { it.isOnline }
        player?.sendMessage(TextComponent(args.joinToString(separator = " ")))
    }

    @SubCommand
    fun tellRP(sender: CommandSender, receiver: String, args: Array<String>) {
        val player = Bukkit.getPlayerExact(receiver)?.takeIf { it.isOnline }
        player?.sendMessage(TextComponent(args.joinToString(separator = " ").replacePlaceholder(player)))
    }


    @SubCommand
    fun test(player: Player) {

        MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
            setCountdownDuration(1500)
            countdownStyle = CountDownStyle.BLOCK
            startText = TextComponent("§f§lNORMAL-1: ")
        }.build()) {
            priority = QueuedCell.Priority.NORMAL
            type = QueuedCell.Type.ACTION_BAR
            audiences.add(BukkitAudience.of(player))
        }

        MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
            setCountdownDuration(10000)
            countdownStyle = CountDownStyle.LINE
            startText = TextComponent("§f§lNORMAL-2: ")
        }.build()) {
            priority = QueuedCell.Priority.NORMAL
            type = QueuedCell.Type.ACTION_BAR
            audiences.add(BukkitAudience.of(player))
        }

        MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
            setCountdownDuration(10000)
            countdownStyle = CountDownStyle.LINE_AND_TIME
            startText = TextComponent("§e§lHIGH-2: ")
        }.build()) {
            priority = QueuedCell.Priority.HIGHEST
            type = QueuedCell.Type.ACTION_BAR
            audiences.add(BukkitAudience.of(player))
        }


        Bukkit.getScheduler().runTaskLater(WdsjMessageBukkit.INSTANCE, Runnable {
            MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
                setCountdownDuration(1000)
                startText = TextComponent("§c§lHIGHEST-1: ")
            }.build()) {
                priority = QueuedCell.Priority.HIGHEST
                type = QueuedCell.Type.ACTION_BAR
                audiences.add(BukkitAudience.of(player))
            }
        }, 50L)

        Bukkit.getScheduler().runTaskLater(WdsjMessageBukkit.INSTANCE, Runnable {
            MessageQueueManager.enqueueMessage(StaticTextMessageCell.Builder.newBuilder {
                setMessage(TextComponent("§4HIGHEST-2: §l!!!啊啊啊 很重要!!!"))
                val timeout = System.currentTimeMillis() + 2000
                setIsActiveSupplier {
                    System.currentTimeMillis() < timeout
                }
            }.build()) {
                priority = QueuedCell.Priority.HIGHEST
                type = QueuedCell.Type.ACTION_BAR
                audiences.add(BukkitAudience.of(player))
            }
        }, 30L)
    }


    @SubCommand
    fun test2(player: Player) {
        MessageQueueManager.enqueueMessage(CountDownTextMessageCell.Builder.newBuilder {
            setCountdownDuration(5000)
            countdownStyle = CountDownStyle.LINE_AND_TIME
            startText = TextComponent("§f§lNORMAL: ")
        }.build()) {
            priority = QueuedCell.Priority.NORMAL
            type = QueuedCell.Type.ACTION_BAR
            audiences.add(BukkitAudience.of(player))
        }

    }

    @SubCommand
    fun test3(player: Player) {
        player.sendActionbar("§c§l测试Actionbar")

    }


}