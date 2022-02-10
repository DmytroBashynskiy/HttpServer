package net.dmytrobashynskiy.http_server.server_utils.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputProcessor {

    private static final Logger logger
            = LoggerFactory.getLogger("DmytroServer");

    public InputDTO readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byte[] endMask = {10, 13, 10};
        byte[] jsonEndMask = {13, 10, 125};
        byte[] lastThreeBytes = {0, 0, 0};
        int charFocus = 0;
        InputDTO result = new InputDTO();
        //do{
        //readRequest(byteArray);
        /**
         * First time run always creates HeadersReader, after that if there is body
         * it is checked what kind of body, based on headers, since there is a header for that.
         *
         * Or maybe simply read headers and after the endMask is reachd but threere are more bytes in the stream
         * pack the rest into it
         */

            /*do {
                charFocus = inputStream.read();
                //this detects pings
                if (charFocus == -1) {
                    logger.info("Incoming ping detected, reopening socket.");
                    result.setPing(true);
                    return result;
                }
                byteArray.write(charFocus);
                updateBytes(lastFourBytes, charFocus);
            } while (!Arrays.equals(endMask, lastFourBytes));*/
        //}while (inputStream.available()>0);
        //NOTE: json ends with {13,10,125,10}
        readBytes(endMask, inputStream, result, byteArray);
        if (result.isPing()) return result;
        if (inputStream.available() > 0) readBytes(jsonEndMask, inputStream, result, byteArray);

        String string = byteArray.toString();
        List<String> lines = breakDownRequestIntoLines(string);
        result.setInputRequest(lines);
        return result;
    }

    //todo handle exception
    private void readBytes(byte[] endMask, InputStream inputStream, InputDTO dto, ByteArrayOutputStream byteArray) throws IOException {
        byte[] lastThreeBytes = {0, 0, 0};
        int charFocus = 0;
        do {
            charFocus = inputStream.read();
            //this detects pings
            if (charFocus == -1) {
                logger.info("Incoming ping detected, reopening socket.");
                dto.setPing(true);
                break;
            }
            byteArray.write(charFocus);
            updateBytes(lastThreeBytes, charFocus);
        } while (!Arrays.equals(endMask, lastThreeBytes));
    }

    //TODO separate request and body into two objects. Make a class that will look into the request and if there
    //is a body of certain type - pack it into respective thing, otherwise send reply code that it is not supported
    private List<String> breakDownRequestIntoLines(String input) {
        List<String> incomingRequest = new ArrayList<>();
        try (Scanner scanner = new Scanner(input)) {
            scanner.useDelimiter("\\n");
            do {
                String focusLine = scanner.nextLine();
                incomingRequest.add(focusLine);
            } while (scanner.hasNext());
        }
        return incomingRequest;
    }

    private void updateBytes(byte[] arr, int c) {
        arr[0] = arr[1];
        arr[1] = arr[2];
        arr[2] = (byte) c;
    }
}
