import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandHelp extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && msg.indexOf("help") == Main.bot.settings.getPrefix(event.getGuild().toString()).length()) {
            String message = "🤔\n" +
                    "c - Shows the best servers to loot\n" +
                    "c <n> - Shows loot history of the server\n" +
                    "fu <n> - Force update server <n>\n" +
                    "up <n> - Show server\n" +
                    "wc <n> - Show  <n>\n" +
                    "prefix set <> - Change bot prefix\n";
            Bot.sendMessage(event.getChannel(), message);
        }
    }
}
