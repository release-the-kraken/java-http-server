package org.rtk.httpserver;

import org.rtk.httphandler.HttpHandler;
import org.rtk.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HttpServer {
    private final Map<String, RequestRunner> routes; //routes = endpoints
    private final ServerSocket socket;
    private final Executor threadPool;
    private HttpHandler handler;
    private final Logger log = new Logger();

    public HttpServer(int port) throws IOException {
        this.routes = new HashMap<>();
        this.socket = new ServerSocket(port);
        this.threadPool = Executors.newFixedThreadPool(100);
    }

    public void addRoute(HttpMethod httpMethod, String route, RequestRunner runner) {
        routes.put(httpMethod.name().concat(route), runner);
    }

    public void start() throws IOException {
        handler = new HttpHandler(routes);

        while (true) {
            Socket clientConnection = socket.accept();
            handleConnection(clientConnection);
        }
    }

    private void handleConnection(Socket clientConnection) {
        Runnable httpRequestRunner = () -> {
            try {
                handler.handleConnection();
            } catch (IOException ignored) {
                log.error("Exception during connection");
            }
        };
        threadPool.execute(httpRequestRunner);
    }
}
