package net.dmytrobashynskiy.http_server.server_utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dmytrobashynskiy.http_server.server_utils.http_util.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HttpResponder {
    private Request localRequest;
    private List<User> users;
    private HttpStatusCode replyCode;
    private String usersList;

    public HttpResponder(Request localRequest, List<User> users) {
        this.localRequest = localRequest;
        this.users = users;
    }

    public Response analyze() {
        HttpMethod currentMethod = localRequest.getMethodName();
        String currentArgument = localRequest.getRequestArgument();
        HttpStatusCode currentErrorCode = localRequest.getRequestError();
        //check for general errors
        if (currentErrorCode != null &&
                currentErrorCode.equals(HttpStatusCode.CLIENT_ERROR_505_HTTP_VERSION_NOT_SUPPORTED)) {
            replyCode = currentErrorCode;
        } else if (currentErrorCode != null &&
                currentErrorCode.equals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST)) {
            replyCode = currentErrorCode;
        } else if (currentErrorCode != null &&
                currentErrorCode.equals(HttpStatusCode.CLIENT_ERROR_406_BAD_BODY)) {
            replyCode = currentErrorCode;
        } else if (currentErrorCode == null) {
            //deciding part
            switch (currentMethod) {
                case GET:
                    if (currentArgument.equals("/users")) {
                        if (users.isEmpty()) {
                            //if no users are in the list
                            replyCode = HttpStatusCode.CLIENT_ERROR_500_INTERNAL_SERVER_ERROR;
                            break;
                        } else {
                            usersList = users.toString();
                            replyCode = HttpStatusCode.RESPONSE_200_OK;
                            break;
                        }
                    } else {
                        replyCode = HttpStatusCode.RESPONSE_200_OK_NO_REPLY_BODY;
                    }
                    break;
                case POST:
                    if (currentArgument.equals("/user")) {
                        //add user to the table
                        Iterator<User> iterator = users.iterator();
                        User overwrittenUser = null;
                        while (iterator.hasNext()) {
                            User user = iterator.next();
                            if (user.getName().equals(localRequest.getUserData().getName())) {
                                if (!user.getEmail().equals(localRequest.getUserData().getEmail())) {
                                    overwrittenUser = new User();
                                    overwrittenUser.setEmail(localRequest.getUserData().getEmail());
                                    overwrittenUser.setName(localRequest.getUserData().getName());
                                    iterator.remove();
                                    replyCode = HttpStatusCode.RESPONSE_200_OK_USER_OVERWRITTEN;
                                }
                            }
                        }
                        //this actually 'overwrites' a user
                        if (overwrittenUser != null) {
                            users.add(overwrittenUser);
                        }
                        //write new user
                        if ((replyCode == null) || (!replyCode.equals(HttpStatusCode.RESPONSE_200_OK_USER_OVERWRITTEN))) {
                            users.add(localRequest.getUserData());
                            replyCode = HttpStatusCode.RESPONSE_201_OK_USER_WRITE_SUCCSESS;
                        }
                    } else {
                        replyCode = HttpStatusCode.CLIENT_ERROR_405_METHOD_NOT_ALLOWED;
                    }
                    break;
                default:
                    replyCode = HttpStatusCode.CLIENT_ERROR_501_METHOD_NOT_IMPLEMENTED;
                    break;
            }
        }
        Response readyResponse = new Response(users);
        try {
            readyResponse.setResponseLines(composeResponseText());
        } catch (JsonProcessingException e) {
            System.out.println("Failed to compose response text");
            e.printStackTrace();
        }
        return readyResponse;
    }

    private List<String> composeResponseText() throws JsonProcessingException {
        List<String> responseLines = new ArrayList<>();
        String CRLF = System.lineSeparator();
        String httpVersion = "HTTP/1.1";
        if (replyCode.equals(HttpStatusCode.RESPONSE_200_OK)) {
            System.out.println(String.format("Code: %d - users were listed to the client.\"", replyCode.statusCode));
            String usersOutput = readJsons(users);
            responseLines.addAll(writeReply(replyCode, httpVersion, CRLF));
            responseLines.add(String.format("Content-Type:text/json%s", CRLF));
            //this gets size of the content in octets(aka bytes)
            responseLines.add(String.format("Content-Length: %d%s", usersOutput.getBytes(StandardCharsets.US_ASCII).length, CRLF));
            responseLines.add(CRLF);
            responseLines.add(usersOutput);
        } else {
            responseLines.addAll(writeReply(replyCode, httpVersion, CRLF));
            responseLines.add(CRLF);
        }
        return responseLines;
    }

    private List<String> writeReply(HttpStatusCode code, String httpVersion, String CRLF) {
        List<String> localReply = new ArrayList<>();
        System.out.println(String.format("Code: %d - %s", code.statusCode, code.reasonPhrase));
        localReply.add(String.format("%s %d %s%s", httpVersion, code.statusCode, code.reasonPhrase, CRLF));
        localReply.add(String.format("Server: DmytroBashynskiyServer%s", CRLF));
        return localReply;
    }

    private String readJsons(List<User> listOfUsers) throws JsonProcessingException {
        StringBuilder builder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        builder.append("[\n");
        for (int i = 0; i < listOfUsers.size(); i++) {
            if (i == (listOfUsers.size() - 1)) {//the last/only entry
                builder.append(objectMapper.writeValueAsString(listOfUsers.get(i)));
            } else {
                builder.append(objectMapper.writeValueAsString(listOfUsers.get(i)));//any entry
                builder.append(",\n");
            }
        }
        builder.append("\n]");
        return builder.toString();
    }
}
