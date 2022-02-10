package net.dmytrobashynskiy.http_server.server_utils.processor;

import java.util.List;

public class InputDTO {

    //TODO encapsulate data on object creation?
    private List<String> inputRequest;
    private boolean isPing;

    public List<String> getInputRequest() {
        return inputRequest;
    }

    public void setInputRequest(List<String> inputRequest) {
        this.inputRequest = inputRequest;
    }

    public boolean isPing() {
        return isPing;
    }

    public void setPing(boolean ping) {
        isPing = ping;
    }


}
