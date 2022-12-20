import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel();
        channel.sendMessage(String.format(SlashCommands.AMOGUS, event.getMember().getAsMention())).queue();
    }
}