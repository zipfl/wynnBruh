import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }

        JDA jda = JDABuilder.createDefault(args[0], GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.playing("ligo"))
                .build();

        jda.addEventListener(new SlashCommands());
        jda.addEventListener(new GuildJoinListener());
        jda.upsertCommand("say", "less").addOption(OptionType.STRING, "message", "Message to say").queue();
        jda.upsertCommand("ping", "Calculate ping of the bot").queue();
        jda.upsertCommand("sussy", "Say a suss y quote").queue();
        jda.upsertCommand("sus", "Amogus").addOption(OptionType.STRING, "sus", "Imposter").queue();
    }
}
