import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandPrefix extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(Main.bot.settings.getPrefix()) && msg.getContentRaw().indexOf("prefix") == Main.bot.settings.getPrefix().length()) {
            MessageChannel channel = event.getChannel();
            StringBuilder message = new StringBuilder();
            String cmd = null;
            if (msg.getContentRaw().trim().contains(" ")) {
                cmd = msg.getContentRaw().trim().split(" ")[1];
            }

            if (cmd != null && cmd.trim().equals("set")) {
                Main.bot.settings.setPrefix(msg.getContentRaw().trim().split(" ")[2]);
                message.append("Prefix set to:\n");
                message.append(msg.getContentRaw().trim().split(" ")[2]);
            } else if (cmd == null || cmd.trim().equals("get")) {
                message.append("Current prefix is:\n");
                message.append(Main.bot.settings.getPrefix());
            }

            Bot.sendMessage(channel, message.toString());
        }
    }
}
