package net.dmytrobashynskiy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    /**
     * THIS IS A TESTING SETUP
     */

    private static String getRequest= "GET / HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1" +

            "\n\n";
    private static String getUserRequest= "GET localhost:9090/users HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\n";

    private static String postUserRequest1 =  "POST /user HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\r\n"+
            "{\n\"name\": \"Hello World\",\n\"email\": \"hello.world@mail.com\"\n}";
    private static String postUserRequest2 =  "POST /user HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\r\n"+
            "{\n\"name\": \"Hello World\",\n\"email\": \"hello.mars@mail.com\"\n}";
    private static String headRequest = "HEAD / HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\n";
    private static String postFaultyBody =  "POST /user HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\r\n"+
            "{\n\"name\": Raven,\n\"email\": \"hello.rock@mail.com\"\n}";
    private static String postUserRequest3 =  "POST /user HTTP/1.1\n" +
            "Host: localhost:9090\n" +
            "User-Agent: Mozilla/5.0 (Windows NT 6.1; rv:93.0) Gecko/20100101 Firefox/93.0\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "DNT: 1\n" +
            "Connection: keep-alive\n" +
            "Cookie: Idea-f2a340a8=e79dc0a2-4a59-4917-b3ef-f7a229edd54a\n" +
            "Upgrade-Insecure-Requests: 1\n" +

            "\r\n"+
            "{\n\"name\": \"Raven\",\n\"email\": \"hello.rock@mail.com\"\n}";

    public static void main(String[] args) {
        boolean run = true;
        while(run){
            try(Socket socket = new Socket("localhost", 9090)){
                try(BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
                    PrintWriter output = new PrintWriter(socket.getOutputStream()))
                {
                    output.println(postUserRequest1);
                    output.flush();
                    do{
                        String str = input.readLine();
                        System.out.println(str);
                    }while(input.ready());
                    run = false;
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
