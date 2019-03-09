package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.microsoft.signalr.*;

/**
 * Hello world!
 *
 */
public class App  {
    public static void main( String[] args ) throws Exception {
        HubConnection connection = HubConnectionBuilder
            .create("https://okazukisignalr.azurewebsites.net/api")
            .build();
        connection.on("receiveMessage", chatMessage -> {
            System.out.println("Received a message:" + chatMessage.getText());
        }, ChatMessage.class);
        connection.start().blockingAwait();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        URL url = new URL("https://okazukisignalr.azurewebsites.net/api/PostMessage");
        while(true) {
            String text = r.readLine();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStreamWriter w = new OutputStreamWriter(con.getOutputStream());
            w.write("{ \'text\': \'" + text + "\' }");
            w.flush();
            w.close();
            con.connect();
            int code = con.getResponseCode();
            if (code != 200) {
                System.out.println("Error:" + code);
            }
        }
    }
}
