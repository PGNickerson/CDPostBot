import java.util.*;
import org.pircbotx.*;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;
import org.pircbotx.cap.*;
import org.pircbotx.exception.*;
import org.pircbotx.hooks.managers.*;
import org.pircbotx.hooks.types.*;
import java.io.*;
import java.net.*;
import javax.script.*;
import java.util.*;

public class pimathbotCemetech extends ListenerAdapter
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
        if (message.equalsIgnoreCase(".time"))
        {
        	msg(event, "The time is: " + new Date());
        }
        if (message.equalsIgnoreCase(".test"))
        {
        	msg(event, "test received");
        }
        if (message.startsWith(".paste"))
        {
        	String pastedString = message.substring(7);
        	msg(event, paste(pastedString));
        }
    }
    
	public static void main(String[] args) throws Exception
	{
		bot.getListenerManager().addListener(new pimathbot());
		bot.setName("pimathbot");
		bot.connect("irc.efnet.org");
		bot.joinChannel("#cemetech");
	}
	
	public static String paste(String pasteString) throws IOException
	{
		String api_dev_key = "f7294063e7414c56bc44a7bca6c5f38c";
		String api_paste_code = pasteString;
		String api_paste_private = "0";
		String api_paste_name = "paste by pimathbot";
		String api_paste_expire_date = "N";
		String api_paste_format = "text";
		String api_user_key = "";
		String api_option = "paste";
		api_paste_code = URLEncoder.encode(api_paste_code, "UTF-8");
		api_paste_name = URLEncoder.encode(api_paste_name, "UTF-8");
		api_paste_code = api_paste_code.replaceAll("%5C%5C", "%0A");
		String pasteURL = "http://pastebin.com/api/api_post.php";
		String urlParameters = "api_option=" + api_option + "&api_user_key=" + api_user_key + "&api_paste_private=" + api_paste_private + "&api_paste_name=" + api_paste_name + "&api_paste_expire_date=" + api_paste_expire_date + "&api_paste_format=" + api_paste_format + "&api_dev_key=" + api_dev_key + "&api_paste_code=" + api_paste_code;
		
		URL url = new URL(pasteURL);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		connection.setRequestMethod("POST");
		
		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		
		InputStream response = connection.getInputStream();
		
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try
		{
			br = new BufferedReader(new InputStreamReader(response));
			while ((line = br.readLine()) != null)
			{
				sb.append(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
}