package net.dmytrobashynskiy.http_util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class HttpParser {
    List<String> requestLineList;

    public HttpParser(List<String> inputRequest) {
        requestLineList = inputRequest;
    }

    public Request parse() {
        Request newRequestObj = new Request();
        Request requestLineParsed = parseRequestLine(newRequestObj, requestLineList);
        if (requestLineParsed.getMethodName() != null &&
                (!requestLineParsed.getMethodName().equals(HttpMethod.POST))) {
            return requestLineParsed;
        } else return parseRequestBody(parseRequestHeaders(requestLineParsed));
    }

    private Request parseRequestLine(Request localRequest, List<String> inputRequest) {
        try (Scanner requestFirstLineScanner = new Scanner(inputRequest.get(0))) {
            requestFirstLineScanner.useDelimiter("\\s");
            String[] firstLineRequest = new String[3];
            int count = 0;
            while (requestFirstLineScanner.hasNext()) {
                String token = requestFirstLineScanner.next();
                firstLineRequest[count] = token;
                count++;
            }
            //parsing methods
            switch (firstLineRequest[0]) {
                case "GET":
                    localRequest.setMethodName(HttpMethod.GET);
                    break;
                case "POST":
                    localRequest.setMethodName(HttpMethod.POST);
                    break;
                default:
                    localRequest.setRequestError(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    break;
            }
            //request URI
            if (firstLineRequest[1] == null || firstLineRequest[1].length() < 1) {
                localRequest.setRequestError(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            } else localRequest.setRequestArgument(firstLineRequest[1]);
            //HTTP version
            if (!firstLineRequest[2].equals("HTTP/1.1")) {
                localRequest.setRequestError(HttpStatusCode.CLIENT_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
            }
            localRequest.setHttpVersion(firstLineRequest[2]);
            inputRequest.remove(0);
            localRequest.setTempArray(inputRequest);
        } catch (NullPointerException e) {
            localRequest.setRequestError(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        return localRequest;
    }

    private Request parseRequestHeaders(Request firstPassReq) {
        List<String> headersToFind = firstPassReq.getTempArray();
        Map<String, String> headersMap = new HashMap<>();
        int crlfIndex = 0;
        for (String entry : headersToFind) {
            if (entry.equals("")) {
                crlfIndex = headersToFind.indexOf(entry);
                break;
            }
            try {
                String[] twoStr = entry.split(": ", 2);
                headersMap.put(twoStr[0], twoStr[1]);//write header name and header body into the hashmap for later use
            } catch (ArrayIndexOutOfBoundsException e) {
                //do nothing and proceed
            }
        }
        firstPassReq.setHeaders(headersMap);
        //this detects if CRLF is not the last String in Array, meaning there is content after it
        //it captures the body
        if (headersToFind.size() != crlfIndex + 1) {
            ArrayList<String> bodyArray = new ArrayList<>();
            for (int i = crlfIndex + 1; i < headersToFind.size(); i++) {
                bodyArray.add(headersToFind.get(i));
            }
            firstPassReq.setTempArray(bodyArray);
        }
        //now the temp array contains body
        return firstPassReq;
    }

    private Request parseRequestBody(Request request) {
        StringBuilder jsonFormer = new StringBuilder();
        for (String entry : request.getTempArray()) {
            jsonFormer.append(entry);
        }
        String readyJson = jsonFormer.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        if (request.getRequestError() != null
                && request.getRequestError().equals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST)) return request;
        try {
            user = objectMapper.readValue(readyJson, User.class);
            request.setUserData(user);
        } catch (JsonProcessingException procException) {
            request.setRequestError(HttpStatusCode.CLIENT_ERROR_406_BAD_BODY);
        }
        //fully populated request object
        return request;
    }
}
