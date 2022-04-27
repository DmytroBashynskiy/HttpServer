package net.dmytrobashynskiy.http_server.server_utils.http_util;


import java.util.List;
import java.util.Map;

public class Request {


    /**
     * This is a request data object.
     */

    private HttpMethod methodName;
    private String requestArgument;
    private String httpVersion;
    private HttpStatusCode requestError;
    private String messageBody;
    private List<String> tempArray;
    private Map<String, String> headers;
    //private JsonNode userData;
    private User userData;

    public User getUserData() {
        return userData;
    }

    public void setUserData(User userData) {
        this.userData = userData;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }


    public List<String> getTempArray() {
        return tempArray;
    }

    public void setTempArray(List<String> tempArray) {
        this.tempArray = tempArray;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpStatusCode getRequestError() {
        return requestError;
    }

    public void setRequestError(HttpStatusCode requestError) {
        this.requestError = requestError;
    }

    public HttpMethod getMethodName() {
        return methodName;
    }

    public void setMethodName(HttpMethod methodName) {
        this.methodName = methodName;
    }

    public String getRequestArgument() {
        return requestArgument;
    }

    public void setRequestArgument(String requestArgument) {
        this.requestArgument = requestArgument;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }
}
