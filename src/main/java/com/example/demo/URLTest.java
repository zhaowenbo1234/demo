package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLTest {
    private static final String targetURL = "http://localhost:8080/index";// "http://192.168.1.16:3000/esbuilding/public/common/user/login?username='admin'&password='admin'";

    public static void main(String[] args) {

        try {

            URL targetUrl = new URL(targetURL);

            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
//            httpConnection.setRequestMethod("POST");

//            httpConnection.setRequestProperty("Content-Type", "application/json");
//            String input = "{\"username\":\"admin\",\"password\":\"admin\"}";

//            OutputStream outputStream = httpConnection.getOutputStream();
//            outputStream.write(input.getBytes());
//            outputStream.flush();

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                    (httpConnection.getInputStream())));

            String output;
            System.out.println("Output from Server:\n");
            while ((output = responseBuffer.readLine()) != null) {
                System.out.println(output);
            }

            httpConnection.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
