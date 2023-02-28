package com.webcrawler.webcrawlerapp.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebDocument {

    private String html = "";
    private HttpURLConnection connection;

    public void crawlUrl(String url) throws IOException {
        if (connectUrl(url)) {
            readUrl();
        }
    }

    private boolean connectUrl(String url) {
        try {
            URL searchUrl = new URL(url);
            connection = (HttpURLConnection) searchUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)");
//            connection.setRequestProperty("User-Agent", "Mozilla ...");
            connection.setRequestMethod("GET");
            connection.connect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void readUrl() {

        try {
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String readLine;
            while (true) {
                readLine = inputStream.readLine();

                if (readLine == null)
                    break;

                html += readLine + " ";

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getHTML() {
        return html;
    }
}