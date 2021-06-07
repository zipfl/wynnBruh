import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandHelp extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && (msg.indexOf("help") == Main.bot.settings.getPrefix(event.getGuild().toString()).length() || msg.indexOf("h") == Main.bot.settings.getPrefix(event.getGuild().toString()).length())) {
            String message = "🤔\n" +
                    "c - Show chests looted in the last 2h per server\n" +
                    "c <n> - Shows loot history of server WC<n>\n" +
                    "fu <n> - Force update server <n>\n" +
                    "fu <p> - Force update player <p>\n" +
                    "up - Show server uptimes\n" +
                    "up <n> - Show uptime of WC<n>\n" +
                    "wc - Show player count per server\n" +
                    "wc <n> - Show player list of WC<n>\n" +
                    "prefix set <prefix> - Change bot prefix\n";
            Bot.sendMessage(event.getChannel(), message, true);
        }
    }
}
