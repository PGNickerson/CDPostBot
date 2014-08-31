import java.util.*;
import org.pircbotx.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import org.pircbotx.cap.*;
import org.pircbotx.exception.*;
import org.pircbotx.hooks.managers.*;
import org.pircbotx.hooks.types.*;


public class lyricsbot extends ListenerAdapter
{
	static PircBotX bot = new PircBotX();
    public void msg(MessageEvent event, String message)
    {
    	bot.sendMessage(event.getChannel(), message);
    }
    public void onMessage(MessageEvent event) throws Exception
    {
    	String message = event.getMessage();
        String sender = event.getUser().getNick();
        String channel = event.getChannel().getName();
    	if(sender.equals("OmnomIRC"))
    	{
    		try
    		{
    			sender = message.substring(0,message.indexOf(">"));
    			sender = sender.substring(message.indexOf("<")+1);
    			message = message.substring(message.indexOf("> ")+2);
    		}
    		catch(Exception e){}
    	}
        if (message.equalsIgnoreCase("bohemian rhapsody"))
        	msg(event, "Is this the real life?");
        if (message.equalsIgnoreCase("Is this just fantasy?"))
        	msg(event, "Caught in a landslide");
        if (message.equalsIgnoreCase("No escape from reality"))
        	msg(event, "Open your eyes");
        if (message.equalsIgnoreCase("Look up to the skies and see"))
        	msg(event, "I'm just a poor boy, I need no sympathy");
        if (message.equalsIgnoreCase("Because I'm easy come, easy go"))
        	msg(event, "A little high, little low");
        if (message.equalsIgnoreCase("Anyway the wind blows, doesn't really matter to me, to me"))
        	msg(event, "Mama, just killed a man");
        if (message.equalsIgnoreCase("Put a gun against his head"))
        	msg(event, "Pulled my trigger, now he's dead");
        if (message.equalsIgnoreCase("Mama, life had just begun"))
        	msg(event, "But now I've gone and thrown it all away");
        if (message.equalsIgnoreCase("Mama, ooo"))
        	msg(event, "Didn't mean to make you cry");
        if (message.equalsIgnoreCase("If I'm not back again this time tomorrow"))
        	msg(event, "Carry on, carry on, as if nothing really matters");
        if (message.equalsIgnoreCase("Too late, my time has come"))
        	msg(event, "Sends shivers down my spine");
        if (message.equalsIgnoreCase("Body's aching all the time"))
        	msg(event, "Goodbye everybody - I've got to go");
        if (message.equalsIgnoreCase("Gotta leave you all behind and face the truth"))
        	msg(event, "Mama, ooo - (anyway the wind blows)");
        if (message.equalsIgnoreCase("I don't want to die"))
        	msg(event, "I sometimes wish I'd never been born at all");
        if (message.equalsIgnoreCase("I see a little silhouetto of a man"))
        	msg(event, "Scaramouch, scaramouch will you do the fandango");
        if (message.equalsIgnoreCase("Thunderbolt and lightning - very very frightening me"))
        	msg(event, "Gallileo, Gallileo,");
        if (message.equalsIgnoreCase("Gallileo, Gallileo,"))
        	msg(event, "Gallileo Figaro - magnifico");
        if (message.equalsIgnoreCase("But I'm just a poor boy and nobody loves me"))
        	msg(event, "He's just a poor boy from a poor family");
        if (message.equalsIgnoreCase("Spare him his life from this monstrosity"))
        	msg(event, "Easy come easy go - will you let me go");
        if (message.equalsIgnoreCase("Bismillah! No - we will not let you go - let him go"))
        	msg(event, "Bismillah! We will not let you go - let him go");
        if (message.equalsIgnoreCase("Bismillah! We will not let you go - let me go"))
        	msg(event, "Will not let you go - let me go (never)");
        if (message.equalsIgnoreCase("Never let you go - let me go"))
        	msg(event, "Never let me go - ooo");
        if (message.equalsIgnoreCase("No, no, no, no, no, no, no -"))
        	msg(event, "Oh mama mia, mama mia, mama mia let me go");
        if (message.equalsIgnoreCase("Beelzebub has a devil put aside for me"))
        	msg(event, "for me");
        if (message.equalsIgnoreCase("for me"))
        	msg(event, "So you think you can stone me and spit in my eye");
        if (message.equalsIgnoreCase("So you think you can love me and leave me to die"))
        	msg(event, "Oh baby - can't do this to me baby");
        if (message.equalsIgnoreCase("Just gotta get out - just gotta get right outta here"))
        	msg(event, "Ooh yeah, ooh yeah");
        if (message.equalsIgnoreCase("Nothing really matters"))
        	msg(event, "Anyone can see");
        if (message.equalsIgnoreCase("Nothing really matters - nothing really matters to me"))
        	msg(event, "Anyway the wind blows...");
    }
	public static void main(String[] args) throws Exception
	{
		bot.getListenerManager().addListener(new lyricsbot());
		bot.setName("lyricsbot");
		bot.connect("irc.efnet.org");
		bot.joinChannel("#omnimaga-spam");
	}
}