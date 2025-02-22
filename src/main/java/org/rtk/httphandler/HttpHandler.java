package org.rtk.httphandler;

import org.rtk.httpdecoder.HttpDecoder;
import org.rtk.model.HttpRequest;
import org.rtk.model.HttpResponse;
import org.rtk.httpserver.RequestRunner;

import java.io.*;
import java.util.Map;
import java.util.Optional;

public class HttpHandler {
    private final Map<String, RequestRunner> routes;

    public HttpHandler(Map<String, RequestRunner> routes){
        this.routes = routes;
    }

    public void handleConnection(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        Optional<HttpRequest> request = HttpDecoder.decode(inputStream);
        request.ifPresentOrElse(req -> handleRequest(req, bufferedWriter), () -> handleInvalidRequest(bufferedWriter));

        bufferedWriter.close();
        inputStream.close();
    }

    private void handleInvalidRequest(final BufferedWriter bufferedWriter) {
        HttpResponse badRequestResponse = new HttpResponse.Builder()
                .setStatusCode(400)
                .setBody("Bad request")
                .build();
        ResponseWriter.writeResponse(bufferedWriter, badRequestResponse);
    }

    private void handleRequest(HttpRequest req, BufferedWriter bufferedWriter) {
        final String routeKey = req
                .httpMethod()
                .name()
                .concat(req.uri().getRawPath());

        if (routes.containsKey(routeKey)){
            ResponseWriter.writeResponse(bufferedWriter, routes.get(routeKey).run(req));
        } else {
            HttpResponse notFoundResponse = new HttpResponse.Builder()
                    .setStatusCode(404)
                    .setBody("Not found")
                    .build();
            ResponseWriter.writeResponse(bufferedWriter, notFoundResponse);
        }
    }
}
