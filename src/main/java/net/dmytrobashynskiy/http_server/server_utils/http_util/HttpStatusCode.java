package net.dmytrobashynskiy.http_server.server_utils.http_util;

public enum HttpStatusCode {

    //Client errors
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_405_METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CLIENT_ERROR_406_BAD_BODY(406, "Bad Body"),
    //server errors
    CLIENT_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    CLIENT_ERROR_501_METHOD_NOT_IMPLEMENTED(501, "Method Not Implemented"),
    CLIENT_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
    //response codes
    RESPONSE_200_OK_NO_REPLY_BODY(200, "OK"),
    RESPONSE_200_OK(200, "OK"),
    RESPONSE_200_OK_USER_OVERWRITTEN(200, "OK"),
    RESPONSE_201_OK_USER_WRITE_SUCCSESS(201, "Created");

    public final String reasonPhrase;
    public final int statusCode;

    HttpStatusCode(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

}
