package cdpostbot;

import cdpostbot.http.HTTP;
import cdpostbot.model.*;
import cdpostbot.read.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.pircbotx.*;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import com.google.gson.*;

public class CDPostBot extends ListenerAdapter {

    static PircBotX bot = new PircBotX();
    static GoogleResults googleMsgResults;

    public CDPostBot() {
    }

    public void msg(MessageEvent event, String outputMessage) {
        bot.sendMessage(event.getChannel(), outputMessage);
    }

    public void msg(String chan, String outputMessage) {
        bot.sendMessage(chan, outputMessage);
    }

    public void onMessage(MessageEvent event) throws Exception {
        String message = event.getMessage();
        String nick = event.getUser().getNick();
        String name = event.getChannel().getName();
        if (message.equalsIgnoreCase(".time")) {
            msg(event, (new StringBuilder()).append("The time is: ").append(new Date()).toString());
        }
        if (message.equalsIgnoreCase(".test")) {
            msg(event, "test received");
        }
        if (message.startsWith(".paste")) {
            String s4 = message.substring(7);
            msg(event, paste(s4));
        }
        if (message.startsWith(".ping")) {
            msg(event, ping(message.substring(6)));
        }
        if (message.startsWith(".google")) {
            googleMsgResults = google(message.substring(8));
            if (googleMsgResults.getResponseData().getResults().size() != 0) {
                String stripped = googleMsgResults.getResponseData().getResults().get(0).getTitle().replaceAll("<[^>]*>", "");
                msg(event, stripped + " - " + googleMsgResults.getResponseData().getResults().get(0).getUrl());
            }
        }
        if (message.startsWith(".cdsearch")) {
            googleMsgResults = google("site:chiefdelphi.com" + message.substring(10));
            if (googleMsgResults.getResponseData().getResults().size() != 0) {
                String stripped = googleMsgResults.getResponseData().getResults().get(0).getTitle().replaceAll("<[^>]*>", "");
                msg(event, stripped + " - " + googleMsgResults.getResponseData().getResults().get(0).getUrl());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        bot.getListenerManager().addListener(new CDPostBot());
        bot.setName("CDBot");
        bot.connect("irc.freenode.net");
        bot.joinChannel("##CDBot");
        bot.joinChannel("##FRC");
        RSSFeedParser parser = new RSSFeedParser("http://www.chiefdelphi.com/forums/external.php?type=RSS2&forumids=59,171,13,16,113,15,21,58,125,50,22,66,52,53,185,51,182,183,184,187,177,176,173,54,55,168,57,152,169,85,9,64,159,56,149,150,3,158,11,6,36,147,86,63,82,87,88,12,10,180,188,20,4,148,24,140,181,47,110,14,84,128,122,160,35,61,62,78,65,80,83,114,134,172,174,175,112,124,126,130,131,146,162,178,189,49,70,127,7,8,68,37,38,39,40,59");
        Feed previousFeed = parser.readFeed();
        System.out.println(previousFeed.getMessages().get(0).getTitle() + " | " + previousFeed.getMessages().get(0).getLink());
        bot.sendMessage("##CDBot", previousFeed.getMessages().get(0).getTitle() + " | " + previousFeed.getMessages().get(0).getLink());
        bot.sendMessage("##FRC", previousFeed.getMessages().get(0).getTitle() + " | " + previousFeed.getMessages().get(0).getLink());
        Feed feed;
        System.out.println(getTBAEventKeys());
        while (true) {
            feed = parser.readFeed();
            if (!feed.getMessages().get(0).getTitle().equals(previousFeed.getMessages().get(0).getTitle())) {
                previousFeed = feed;
                System.out.println(feed.getMessages().get(0).getTitle() + " | " + feed.getMessages().get(0).getLink());
                bot.sendMessage("##CDBot", feed.getMessages().get(0).getTitle() + " | " + feed.getMessages().get(0).getLink());
                bot.sendMessage("##FRC", feed.getMessages().get(0).getTitle() + " | " + feed.getMessages().get(0).getLink());
            }

        }
    }

    public static String paste(String pasteString) throws IOException {
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
        try {
            br = new BufferedReader(new InputStreamReader(response));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String ping(String url) {
        if (url.startsWith("http://")) {
        } else if (url.startsWith("https://")) {
            url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        } else {
            url = "http://" + url;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();

            if ((200 <= responseCode && responseCode <= 399) || (responseCode == 403)) {
                return "successful ping with code " + responseCode;
            }
            return "unsuccessful ping with code " + responseCode;

        } catch (IOException exception) {
            return "unsuccessful ping with IOException";
        }
    }

    static GoogleResults google(String query) throws IOException {

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
         System.out.println("Title: " + results.getResponseData().getResults().get(i).getTitleNoFormatting());
         System.out.println("URL: " + results.getResponseData().getResults().get(i).getUrl() + "\n");
         }
         */
    }

    static List<String> getTBAEventKeys() throws Exception {
        String address = "http://www.thebluealliance.com/api/v2/events/2014";
        List<String> keyList = new ArrayList<String>();
        
        Gson gson = new Gson();
        
        
        return keyList;
    }
    
    
}

class GoogleResults {

    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    public String toString() {
        return "ResponseData[" + responseData + "]";
    }

    static class ResponseData {

        private List<Result> results;

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public String toString() {
            return "Results[" + results + "]";
        }
    }

    static class Result {

        private String url;
        private String title;

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String toString() {
            return "Result[url:" + url + ",title:" + title + "]";
        }
    }
}