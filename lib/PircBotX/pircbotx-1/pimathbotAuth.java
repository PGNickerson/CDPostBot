
import java.io.*;
import java.net.*;
import java.util.Date;
import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.managers.ListenerManager;
import com.google.gson.Gson;
import java.util.List;

public class pimathbotAuth extends ListenerAdapter
{

    static boolean omnomIRC = false;
    static PircBotX bot = new PircBotX();
    static GoogleResults googleMsgResults;

    public pimathbotAuth()
    {
    }

    public void msg(MessageEvent event, String outputMessage)
    {
        bot.sendMessage(event.getChannel(), outputMessage);
    }

    public void onMessage(MessageEvent event) throws Exception
    {
        String message = event.getMessage();
        String nick = event.getUser().getNick();
        String name = event.getChannel().getName();
        if (nick.equals("OmnomIRC"))
        {
            try
            {
                omnomIRC = true;
                String omnom = message.substring(0, message.indexOf(">"));
                omnom = omnom.substring(message.indexOf("<") + 1);
                message = message.substring(message.indexOf("> ") + 2);
            } catch (Exception exception)
            {
            }
        } else
        {
            omnomIRC = false;
        }
        if (message.equalsIgnoreCase(".time"))
        {
            msg(event, (new StringBuilder()).append("The time is: ").append(new Date()).toString());
        }
        if (message.equalsIgnoreCase(".test"))
        {
            msg(event, "test received");
        }
        if (message.startsWith(".paste"))
        {
            String s4 = message.substring(7);
            msg(event, paste(s4));
        }
        if (message.startsWith(".ping"))
        {
            msg(event, ping(message.substring(6)));
        }
        if (message.startsWith(".google"))
        {
            googleMsgResults = google(message.substring(8));
            if (googleMsgResults.getResponseData().getResults().size() != 0)
            {
                //if (omnomIRC)
                //{
                //
                //} else
                //{
                    msg(event, googleMsgResults.getResponseData().getResults().get(0).getTitle() + " - " + googleMsgResults.getResponseData().getResults().get(0).getUrl());
                //}
            }
        }
    }

    

    public static void main(String args[]) throws Exception
    {
        bot.getListenerManager().addListener(new pimathbotAuth());
        bot.setName("pimathbot");
        bot.connect("irc.omnimaga.org");
        
        bot.joinChannel("#omnimaga");
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
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String ping(String url)
    {
        if (url.startsWith("http://"))
        {
        } else if (url.startsWith("https://"))
        {
            url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        } else
        {
            url = "http://" + url;
        }

        try
        {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();

            if ((200 <= responseCode && responseCode <= 399) || (responseCode == 403))
            {
                return "successful ping with code " + responseCode;
            }
            return "unsuccessful ping with code " + responseCode;

        } catch (IOException exception)
        {
            return "unsuccessful ping with IOException";
        }
    }

    static GoogleResults google(String query) throws IOException
    {

        String address = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String charset = "UTF-8";

        URL url = new URL(address + URLEncoder.encode(query, charset));
        Reader reader = new InputStreamReader(url.openStream(), charset);
        return new Gson().fromJson(reader, GoogleResults.class);
        /*
         int total = results.getResponseData().getResults().size();
         System.out.println("total: " + total);

         // Show title and URL of each results
         for (int i = 0; i <= total - 1; i++)
         {
         System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitle());
         System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");
         }
         */
    }

}