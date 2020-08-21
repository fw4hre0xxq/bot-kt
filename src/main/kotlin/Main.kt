import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.CommandSyntaxException
import kotlinx.coroutines.runBlocking
import net.ayataka.kordis.Kordis
import net.ayataka.kordis.event.EventHandler
import net.ayataka.kordis.event.events.message.MessageReceiveEvent
import org.reflections.Reflections
import java.awt.Color


fun main() = runBlocking {
    Bot().start()
}

class Bot {
    private val dispatcher = CommandDispatcher<Cmd>()
    suspend fun start() {
        println("Starting bot!")
        val config = FileManager.readConfig<AuthConfig>(ConfigType.AUTH, false)

        if (config?.botToken == null) {
            println("Bot token not found, make sure your file is formatted correctly!. \nExiting...")
            return
        }

        val client = Kordis.create {
            token = config.botToken

            // Annotation based Event Listener
            addListener(this@Bot)
        }

        registerCommands()
        println("Initialized bot!")
    }

    /**
     * Uses reflection to get a list of classes in the commands package which extend Command
     * and register said classes's instances with Brigadier.
     */
    private fun registerCommands() {
        val reflections = Reflections("commands")

        val subTypes: Set<Class<out Command>> = reflections.getSubTypesOf(Command::class.java)

        println("Registering commands...")
        println(subTypes)

        for (command in subTypes) {
            dispatcher.register(command.getField("INSTANCE").get(null) as LiteralArgumentBuilder<Cmd>)
        }

        println("Registered commands!")
    }

    @EventHandler
    suspend fun onMessageReceive(event: MessageReceiveEvent) {
        val message = if (event.message.content[0] == ';') event.message.content.substring(1) else return
        val cmd = Cmd(event)
        try {
            val exit = dispatcher.execute(message, cmd)
            cmd.file(event)
            if (exit != 0) println("(executed with exit code $exit)")
        } catch (e: CommandSyntaxException) {
//            cmd.event.message.channel.send("Syntax error:\n```\n${e.message}\n```")
        }
    }
}

object Main {
/*    fun embed(embedTypes: EmbedTypes) {
        return when (embedTypes) {
            ERROR -> MessageBuilder().embed {
                field("Error", "You don't have permission to use this command!", true)
                color = Colors.ERROR.color
            }
        }
    }

    enum class EmbedTypes {
        ERROR
    }*/

    /**
     * Int colors, converted from here: https://www.shodor.org/stella2java/rgbint.html
     */
    enum class Colors(val color: Color) {
        BLUE(Color(10195199)),
        ERROR(Color(14565692)),
        WARN(Color(14595644)),
        SUCCESS(Color(3989082));
    }
}
