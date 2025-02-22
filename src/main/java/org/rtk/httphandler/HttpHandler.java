package org.rtk.httphandler;

import org.rtk.httpserver.model.RequestRunner;

import java.util.Map;

public class HttpHandler {
    private final Map<String, RequestRunner> routes;

    public HttpHandler(Map<String, RequestRunner> routes){
        this.routes = routes;
    }

    public void handleConnection(){

    }
}
