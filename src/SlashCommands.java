import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.Random;

public class SlashCommands extends ListenerAdapter {

    private static final ArrayList<String> QUOTES = new ArrayList<>();
    private static final Random rand = new Random();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String eventName = event.getName();

        if (eventName.equals("say")) {
            OptionMapping messageOption = event.getOption("message");

            String msg = "less";
            if (messageOption != null)
                msg = messageOption.getAsString();

            event.reply(msg).queue();
        }

        if (eventName.equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true)
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                    ).queue();
        }

        if (eventName.equals("sus")) {
            OptionMapping susOption = event.getOption("sus");

            String sus = "";
            if (susOption != null)
                sus = susOption.getAsString();

            event.reply(String.format(AMOGUS, sus)).queue();
        }

        if (eventName.equals("sussy")) {
            int i = rand.nextInt(QUOTES.size());
            event.reply(QUOTES.get(i)).queue();
        }
    }

    public SlashCommands() {
        QUOTES.add("Care to explain what you meant when you said it would be 'poggers' if you had a 'monster futa cock' to 'impregnate my boypussy' with, pyro-chan?");
        QUOTES.add("IM DELETING YOU, PYRO!:sob::wave::fire: ██]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]] 10% complete..... ████]]]]]]]]]]]]]]]]]]]]]]]]]]] 35% complete.... ███████]]]]]]]]]]]]]]]] 60% complete.... ███████████] 99% complete..... :no_entry_sign:ERROR!:no_entry_sign: :100:A True:100: Pyro :fire: is irreplaceable :sparkling_heart:I could never delete you Pyro!:sparkling_heart::fire: Send this to ten other :fire:Pyros:fire: who give you :sweat_drops:cummies:sweat_drops: Or never get called :cloud:retard:cloud: again:x::x::grimacing::grimacing::x::x: If you get 0 Back: no cummies for you :no_entry_sign::no_entry_sign::imp: 3 back: you're a retard :smiling_face_with_3_hearts::cloud::sweat_drops: 5 back: you're pyro's kitten:kissing_cat::angel::sweat_drops: 10+ back: Pyro :fire::stuck_out_tongue::stuck_out_tongue::two_hearts::two_hearts::sweat_drops::tongue::tongue:");
        QUOTES.add("as a person who has lots of sex all the time, i can say that this game is 100% accurate to having sex with sexy women. like i do. everyday. this game did not make me horny however. i am not gay. i just have too much sex with real women to spend more than 15 minutes in this game. on the other hand i would recommend this game to people who do not have sex (unlike me because i have lots of sex with women a lot) as there is a naked woman in it and she is naked. she kinda looks like one of my many girlfriends who i have sex with a lot. i have lots of sex. i also an very handsome and women ALWAYS want to have sex with me because i am very muscular and handsome and very good at video games. all my girlfriends say im very good at sex and playing video games and being handsome. one of my girlfriends asked me to have sex with her but i told her i was playing a sex game instead so she started crying and became a lesbian and killed herself because i did not have sex with her. i have sex with women. not men. i am not gay. i am very cool and handsome so girls always have sex with me because i am very cool and sexy. my penis is very big. all my girlfriends like my penis because it is very big and i am very good at sex with my women. every woman ive had sex with is very sexy and so am i. i have lots of sex. i am also very handsome and sexy and i have lots of sex.");
        QUOTES.add("Babe, I'm breaking up with you. It's not you, you were poggers. It's me, I'm omegalul. Im sorry if this is pepehands but it has to be done, I've just been feeling pepega and our relationship has been weirdchamp for months. It's time to end it, no kappa.");
        QUOTES.add("Rawr X3 nuzzles How are you? pounces on you you're so warm o3o notices you have a bulge someone's happy! nuzzles your necky wecky ~murr~ hehe ;) rubbies your bulgy wolgy you're so big! rubbies more on your bulgy wolgy it doesn't stop growing .///. kisses you and licks your neck daddy likes ;) nuzzle wuzzle I hope daddy likes wiggles butt and squirms I wanna see your big daddy meat! wiggles butt I have a little itch o3o wags tails can you please get my itch? put paws on your chest nyea~ it's a seven inch itch rubs your chest can you pwease? squirms pwetty pwease? :( I need to be punished runs paws down your chest and bites lip like, I need to be punished really good paws on your bulge as I lick my lips I'm getting thirsty. I could go for some milk unbuttons your pants as my eyes glow you smell so musky ;) licks shaft mmmmmmmmmmmmmmmmmmm so musky ;) drools all over your cawk your daddy meat. I like. Mister fuzzy balls. puts snout on balls and inhales deeply oh my gawd. I'm so hard rubbies your bulgy wolgy licks balls punish me daddy nyea~ squirms more and wiggles butt I9/11 lovewas an yourinside muskyjob goodness bites lip please punish me licks lips nyea~ suckles on your tip so good licks pre off your cock salty goodness~ eyes roll back and goes balls deep");
        QUOTES.add("WTF 状態 WYNNCRAFT IRL TRADE HACK??? 800 USD SALES STILL NOT BANNED? 注意中国 ー ー GETMADS IMMUNITY HACK - MODERATOR FAVOR SECRET?? 情報誌40000円 ANGRY ARAB MALDING METHOD - NO CREDIT CARD:::??::: 表示 PAYPAL DISPUTE INSTAWIN HACK - GUARANTEED QATAR OUTPLAY 400 USD TRICK @ UNDETECTED IN SAUDI ARABIA 興味深相手。今日 - MC-MARKET WYNNCRAFT INSTA RICH DARK TECHNOLOGY SCRIPT, NOT CAUGHT YET??????");
        QUOTES.add("Wynncraft Content Team are fantastic, they just need to work on coding, community outreach, bug fixing, economy management, game knowledge, quality assurance, hot fix speed, class balance, common sense, compensation rewards, content pacing, content creation, character progression, sense of humor, net coding, power creep, crates, transparency, server stability and customer support.");
        QUOTES.add("I stopped for awhile so im trying to get the gains back... I might not hit hard in Wynncraft but I hit hard IRL :rofl:");
        QUOTES.add("I really love Lari. Like, a lot. Like, a whole lot. You have no idea. I love her so much that it is inexplicable, and I'm ninety-nine percent sure that I have an unhealthy obsession. I will never get tired of doing the Realm of Light quests. It is my life goal to meet up with her in real life and just say hello to her. I fall asleep at night dreaming of her holding a personal light show for me, and then she would be so tired that she comes and cuddles up to me while we sleep together. If I could just hold her hand for a brief moment, I could die happy. If given the opportunity, I would lightly nibble on her ear just to hear what kind of sweet moans she would let out. Then, I would hug her while she clings to my body hoping that I would stop, but I only continue as she moans louder and louder. I would give up almost anything just for her to look in my general direction. No matter what I do, I am constantly thinking of her. When I wake up, she is the first thing on my mind. When I go to school, I can only focus on her. When I go come home, I go on the computer so that I can read her dialogue in the Realm of Light questline. When I go to sleep, I dream of her and I living a happy life together. She is my pride, passion, and joy. If she were to call me \"Onii-chan,\" I would probably get diabetes from her sweetness and die. I wish for nothing but her happiness. If it were for her, I would give my life without any second thoughts. Without her, my life would serve no purpose. I really love Lari.\n");
        QUOTES.add("I really love Mostima. Like, a lot. Like, a whole lot. You have no idea. I love her so much that it is inexplicable, and I'm ninety-nine percent sure that I have an unhealthy obsession. I will never get tired of listening that sweet, (fallen) angelic voice of hers. It is my life goal to meet up with her in real life and just say hello to her. I fall asleep at night dreaming of her holding my hand and take me around Terra, and then she would be sorry tired that she comes and cuddles up to me while we sleep together. If I could just hold her hand for a brief moment, I could die happy. I would give up almost anything just for her to look in my general direction. No matter what I do, I am constantly thinking of her. When I wake up, she is the first thing on my mind. When I go to work, I can only focus on her. When I come home, I get on Arknights instantly so that I can listen to her beautiful voice lines. When I go to sleep, I dream of her and I living a happy life together. She is my pride, passion, and joy. If she were to call me \"dokuta-kun,\" I would probably get diabetes from her sweetness and die. I wish for nothing but her happiness. If it were for her, I would give up my life and my 1000 hours Arknights account without any second thoughts. Without her, my life would serve no purpose. I really love Mostima.");
        QUOTES.add("I’m telling you, Xynode is as cracked as he is jacked. I saw him at a 7-11 the other day buying Coffee and adult diapers. I asked him what the diapers were for and he said ”they contain my full power so I don’t completely shit on these dragons“ then he rode a Sunspire Champion Senche-Lion out the door.");
        QUOTES.add("Hel am look veteran clear sunlightspire, am good play do 67k atronach dps tester (when not lag bit). am player good for warden stamina usage. I try clear for ice fire mount but guid have trouble for cat fighters on icing dragon. I view video of the 4 Days and no difernce make plays me and them, think am good for sunlightspire can do high dps atro for fas run mount get. I play stam warden aso play mana nitgeblade (teach by alcast), let know can play warden stam mana nigte let know not die red dodge all mech never die am good nitgeblade sunlight spire");
        QUOTES.add("As for sex with Weedy, I imagine it will go like this: You have sex with her like a normal girl but with her on top and you're holding hands, but in the height of arousal, she would insert her ovipositor into your urethra (It's a soft tentacle-like organ that extends from within her vagina). Ejaculation would ensue as you both orgasm simultaneously, and her ovipositor would meet your sperm halfway inside of your urethra, right on the prostate area (You'd feel a strong and deep sexual pressure), ensuring that your sperm would successfully fertilize her eggs with a 99% success rate (Excess fluids would be ejaculated past her ovipositor and into her vagina). This fertilizes the eggs, and these fertilized eggs, still inside you, would slowly travel backwards all throughout the vas deferens tubes, into the epididymis's and into your testicles, where they would attach themselves inside of them, safely rest, grow, and be nourished by the testicles. While they grow, they make your testicles work 10x their usual rate, and so you'd be much, much hornier than usual. This allows you to become very attached and intimate with Weedy during the period (This ensures a strong parental bond for the offspring). The eggs wouldn't grow very large at all. They are quite small, but there are up to 1500 of them, and they take from 9-45 days to finish growing. Once they're ready to hatch however, you have birthing sex with Weedy. It is very intimate and wild sex with her where at the peak of the act, you have an immensely powerful ejaculation and ejaculate the eggs into her vagina. They sit there briefly before she delivers them just like humans regularly would through labor. You pass out from the pleasure mid orgasm, and you awake to up to 1500 mini Weedy's and Weedy.");
        QUOTES.add("Xynode Gaming isn't so great? Are you kidding me? When was the last time you saw a player with such an ability and movement with Sorc? Xynode puts the game in another level, and we will be blessed if we ever see a player with his skill and passion for the game again. Kevduit breaks records. Alcast breaks records. Xynode breaks the rules. You can keep your statistics. I prefer the magic.");
        QUOTES.add("put on the fucking femboy outfit pyro");
        QUOTES.add("如果你正好在凌晨4:20在水槽中大喊大叫”amogus”69次，一个神秘的人物叫做”妈妈”会过来揍你一顿，你将在一个名为孤儿院的地方醒来");
        QUOTES.add("Well actually it was a big misunderstanding! I am the son of esteemed billionaire and founder of the Microsoft corporation, Bill Gates. While it may have seem suspicious for me to have 244 thousand shout bombs, I actually purchased them all! I hope this clears misunderstanding and that I can be unbanned from the #1 minecraft MMORPG. Thank you for your time!");
        QUOTES.add("Heatmor is many things. It’s a large, anteater-looking beast. It’s a fire-breathing animal covered in sick flames. It even has a fun name. But one thing it isn’t: A fan of butt plugs. No sir. Keep those sex toys and rubber bits away from Heatmor. And they have a good reason. If you plug up its tail (we all know it’s the butt, really) it gets very sick. So, yes, keep your butt plugs away, please.\n\n According to Pokedex entries listed on Bulbapedia, Heatmor uses its tail to the air it needs to then breath out big, jets of fire. If that intake vent, aka butt, was to be plugged shut, it would lead to Heatmor’s internal flame dying. If that sounds bad, it is bad. If this flame goes out, Heatmor becomes sick.\n\nTo be clear, I’m not here to shame anyone for liking or disliking butt plugs. We all have things we enjoy or hate when it comes to our sex life. But I think we can all agree that if a butt plug was randomly shoved up your arse and then made you very, very ill, you wouldn’t be down for that shit. So I get it, Heatmor. I do.");
        QUOTES.add("Hello, I deeply regret my actions on the Keqing Mains NSFW-chat. I realize now that they were inappropriate, and quite frankly, fairly sus (suspicious). I hope that I can use this appeal to get you to change your mind about me, as I am not an Impostor. I know that you take your task as head of the rule enforcement very seriously, but I just want you to know that I was merely trying to vent here. I can understand that you had to call in this emergency meeting, but I assure you that I will be a diligent crewmate from now on. So to sum up, I hope you can forgive me, and I will try not to get ejected again in the future.");
        QUOTES.add("Fubuki! Fubuki FUBUKI FUBUKIIIIiiiiIIIIIIiiiiaaAAAaaAAa!!! UhUUUHHHHhhHHH! Unh! Uhhhhhh! FUBUKI FUBUKI FUBUKIIIIiiiiiaaaAAAuuUUUuh!!! Ah-Kunkakunka! Kunkakunka! Suu-HA! Suu-HA! Suu-HA! SUU-HAaa! Fubuki smells so good! Nyunhahahuh! Ahun! I want to smell the white tail of Fubuki! Kun-kun! Ahh! No! I want to rub her fur! Mofmof-mofmof-mofmof-mofmof! Fubuki doing self-intro was so cute! Ahh ahh ahhHHUUUHH! It's great you got so many gifts on your birthday, Fubukiii! Ahh-you're so cute, Fubuki! Kawaii-AAAHHHHH! Congrats on getting selling your own merch! aaaaiiiYYYYYAAAAAaaaaaaa! Nyahhhhhhhh-GUEEEEHH-AAAAAA! What? VTubers are not real? Hmmm, so Matsuri and Shion aren't either... f u b u k i i s n o t r e a l...? GyaaaaAAAAaaaAAAAAaaa! WhyyYYYYyyyYYYyy! HOLOLIVE-EEEHHHH! You bastard! Goodbye! Goodbye to this goddamn world! Huh? She's looking? Fubuki on the poster is looking at Matsuri! FUBUKI ON THE POSTER IS LOOKING AT MATSURI!! Fubuki is looking at Matsuri! FUBUKI ON YOUTUBE IS LOOKING AT MATSURI!!! Fubuki on Twitter is talking with Matsuri! Thank God! The world hasn't left me! YaHOOOooo! Fubuki is still with me! I did it! I can do it! Twitter's Fubuking-YYAAAAAAAAaaaa!!!! Uuhhuhh! Haato-sama! Aki Aki! Melu melu! Roboco-senpaiiiiaaaaii!! Send Matsuri's love to Fubuki! To Fubuki from Hololive!");
        QUOTES.add("In case of an investigation by any wynncraft moderator entity or similar, I do not have any involvement with this group or with the people in it, I do not know how I am here, probably added by a third party, I do not support any actions by the member of this group.");
        QUOTES.add("I muted every girl on Twitter today. I simply cannot bear to see another $35 Mostima cosplay from Ebay hastily thrown together with glued on horns and wings, captured using a Nokia flip-phone + full-body mirror, and expertly posted with the caption “What if I landed S2, then what? ;)”");
    }

    protected static final String AMOGUS = "** **\n" +
            "⠀⠀⠀⠀⠀⠀⠀⣠⣤⣤⣤⣤⣤⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
            "⠀⠀⠀⠀⠀⢰⡿⠋⠁⠀⠀⠈⠉⠙⠻⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
            "⠀⠀⠀⠀⢀⣿⠇⠀⢀⣴⣶⡾⠿⠿⠿⢿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
            "⠀⠀⣀⣀⣸⡿⠀⠀⢸⣿⣇⠀⠀⠀⠀⠀⠀⠙⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀ \n" +
            "⠀⣾⡟⠛⣿⡇⠀⠀⢸⣿⣿⣷⣤⣤⣤⣤⣶⣶⣿⠇⠀⠀⠀⠀⠀⠀⠀⣀⠀⠀  %s\n" +
            "⢀⣿⠀⢀⣿⡇⠀⠀⠀⠻⢿⣿⣿⣿⣿⣿⠿⣿⡏⠀⠀⠀⠀⢴⣶⣶⣿⣿⣿⣆ \n" +
            "⢸⣿⠀⢸⣿⡇⠀⠀⠀⠀⠀⠈⠉⠁⠀⠀⠀⣿⡇⣀⣠⣴⣾⣮⣝⠿⠿⠿⣻⡟ \n" +
            "⢸⣿⠀⠘⣿⡇⠀⠀⠀⠀⠀⠀⠀⣠⣶⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠁⠉⠀ \n" +
            "⠸⣿⠀⠀⣿⡇⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠀⠀⠀ \n" +
            "⠀⠻⣷⣶⣿⣇⠀⠀⠀⢠⣼⣿⣿⣿⣿⣿⣿⣿⣛⣛⣻⠉⠁⠀⠀⠀⠀⠀⠀⠀ \n" +
            "⠀⠀⠀⠀⢸⣿⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀ ⠀⠀ \n" +
            "⠀⠀⠀⠀⢸⣿⣀⣀⣀⣼⡿⢿⣿⣿⣿⣿⣿⡿⣿⣿⣿";
}
