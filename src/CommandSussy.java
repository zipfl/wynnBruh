import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class CommandSussy extends ListenerAdapter {

    private static final ArrayList<String> quotes = new ArrayList<>();
    Random rand = new Random();
    public CommandSussy() {
        quotes.add("Care to explain what you meant when you said it would be 'poggers' if you had a 'monster futa cock' to 'impregnate my boypussy' with, pyro-chan?");
        quotes.add("IM DELETING YOU, PYRO!:sob::wave::fire: ██]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]] 10% complete..... ████]]]]]]]]]]]]]]]]]]]]]]]]]]] 35% complete.... ███████]]]]]]]]]]]]]]]] 60% complete.... ███████████] 99% complete..... :no_entry_sign:ERROR!:no_entry_sign: :100:A True:100: Pyro :fire: is irreplaceable :sparkling_heart:I could never delete you Pyro!:sparkling_heart::fire: Send this to ten other :fire:Pyros:fire: who give you :sweat_drops:cummies:sweat_drops: Or never get called :cloud:retard:cloud: again:x::x::grimacing::grimacing::x::x: If you get 0 Back: no cummies for you :no_entry_sign::no_entry_sign::imp: 3 back: you're a retard :smiling_face_with_3_hearts::cloud::sweat_drops: 5 back: you're pyro's kitten:kissing_cat::angel::sweat_drops: 10+ back: Pyro :fire::stuck_out_tongue::stuck_out_tongue::two_hearts::two_hearts::sweat_drops::tongue::tongue:");
        quotes.add("as a person who has lots of sex all the time, i can say that this game is 100% accurate to having sex with sexy women. like i do. everyday. this game did not make me horny however. i am not gay. i just have too much sex with real women to spend more than 15 minutes in this game. on the other hand i would recommend this game to people who do not have sex (unlike me because i have lots of sex with women a lot) as there is a naked woman in it and she is naked. she kinda looks like one of my many girlfriends who i have sex with a lot. i have lots of sex. i also an very handsome and women ALWAYS want to have sex with me because i am very muscular and handsome and very good at video games. all my girlfriends say im very good at sex and playing video games and being handsome. one of my girlfriends asked me to have sex with her but i told her i was playing a sex game instead so she started crying and became a lesbian and killed herself because i did not have sex with her. i have sex with women. not men. i am not gay. i am very cool and handsome so girls always have sex with me because i am very cool and sexy. my penis is very big. all my girlfriends like my penis because it is very big and i am very good at sex with my women. every woman ive had sex with is very sexy and so am i. i have lots of sex. i am also very handsome and sexy and i have lots of sex.");
        quotes.add("Babe, I'm breaking up with you. It's not you, you were poggers. It's me, I'm omegalul. Im sorry if this is pepehands but it has to be done, I've just been feeling pepega and our relationship has been weirdchamp for months. It's time to end it, no kappa.");
        quotes.add("Rawr X3 nuzzles How are you? pounces on you you're so warm o3o notices you have a bulge someone's happy! nuzzles your necky wecky ~murr~ hehe ;) rubbies your bulgy wolgy you're so big! rubbies more on your bulgy wolgy it doesn't stop growing .///. kisses you and licks your neck daddy likes ;) nuzzle wuzzle I hope daddy likes wiggles butt and squirms I wanna see your big daddy meat! wiggles butt I have a little itch o3o wags tails can you please get my itch? put paws on your chest nyea~ it's a seven inch itch rubs your chest can you pwease? squirms pwetty pwease? :( I need to be punished runs paws down your chest and bites lip like, I need to be punished really good paws on your bulge as I lick my lips I'm getting thirsty. I could go for some milk unbuttons your pants as my eyes glow you smell so musky ;) licks shaft mmmmmmmmmmmmmmmmmmm so musky ;) drools all over your cawk your daddy meat. I like. Mister fuzzy balls. puts snout on balls and inhales deeply oh my gawd. I'm so hard rubbies your bulgy wolgy licks balls punish me daddy nyea~ squirms more and wiggles butt I9/11 lovewas an yourinside muskyjob goodness bites lip please punish me licks lips nyea~ suckles on your tip so good licks pre off your cock salty goodness~ eyes roll back and goes balls deep");
        quotes.add("WTF 状態 WYNNCRAFT IRL TRADE HACK??? 800 USD SALES STILL NOT BANNED? 注意中国 ー ー GETMADS IMMUNITY HACK - MODERATOR FAVOR SECRET?? 情報誌40000円 ANGRY ARAB MALDING METHOD - NO CREDIT CARD:::??::: 表示 PAYPAL DISPUTE INSTAWIN HACK - GUARANTEED QATAR OUTPLAY 400 USD TRICK @ UNDETECTED IN SAUDI ARABIA 興味深相手。今日 - MC-MARKET WYNNCRAFT INSTA RICH DARK TECHNOLOGY SCRIPT, NOT CAUGHT YET??????");
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        if (msg.startsWith(Main.bot.settings.getPrefix(event.getGuild().toString())) && msg.indexOf("sussy") == Main.bot.settings.getPrefix(event.getGuild().toString()).length() && msg.length() == Main.bot.settings.getPrefix(event.getGuild().toString()).length() + "sussy".length()) {
            int i = rand.nextInt(quotes.size());
            String message = quotes.get(i);

            Bot.sendMessage(event.getChannel(), message, false);
        }
    }
}
