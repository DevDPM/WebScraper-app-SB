package com.webcrawler.webcrawlerapp.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class WebCrawler {

    private String url;
    private HttpURLConnection connection;
    private int counter = 1;

    protected WebCrawler(String url) {
        this.url = url;
    }

    protected String getHTML() {
        if (url != null)
            return request();
        return null;
    }

    private String request() {
        if (connect(url)) {

            try {
                StringBuffer truncatedHTML = new StringBuffer();
                BufferedReader inputStream =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String readLine;

                while (true) {
                    readLine = inputStream.readLine();

                    if (readLine == null)
                        break;
                    truncatedHTML.append(readLine).append(" ");
                }
                return truncatedHTML.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean connect(String url) {
        try {
            URL searchUrl = new URL(url);
            connection = (HttpURLConnection) searchUrl.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i586; en-US; rv:1.7.3) Gecko/20040924 Epiphany/1.4.4 (Ubuntu)");
            connection.setRequestMethod("GET");
            connection.connect();
            return true;
        } catch (FileNotFoundException fnf) {
            System.out.println("Not found (x): " + counter);
            counter++;
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
