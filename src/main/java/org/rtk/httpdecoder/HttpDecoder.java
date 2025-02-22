package org.rtk.httpdecoder;

import org.rtk.httpserver.HttpMethod;
import org.rtk.logger.Logger;
import org.rtk.model.HttpRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * HttpDecoder:
 * InputStreamReader -> bytes to characters ( decoded with certain Charset ( ascii ) )
 * BufferedReader    -> character stream to text
 */
public class HttpDecoder {
    public static Optional<HttpRequest> decode(InputStream inputStream) {
        return readMessage(inputStream).flatMap(msg -> buildRequest(msg));
    }

    private static Optional<HttpRequest> buildRequest(List<String> msg) {
        if (msg.isEmpty()) {
            return Optional.empty();
        }
        String firstLine = msg.get(0);
        String[] httpInfo = firstLine.split(" ");

        if (httpInfo.length != 3) {
            return Optional.empty();
        }

        String protocolVersion = httpInfo[2];

        if (!protocolVersion.equals("HTTP/1.1")) {
            return Optional.empty();
        }

        try {
            HttpRequest request = new HttpRequest();
            request.setHttpMethod(HttpMethod.valueOf(httpInfo[0]));
            request.setUri(new URI(httpInfo[2]));
            return Optional.of(addRequestHeaders(msg, request));
        } catch (URISyntaxException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private static HttpRequest addRequestHeaders(List<String> msg, HttpRequest request) {
        //TODO implement method
        return null;
    }

    private static Optional<List<String>> readMessage(InputStream inputStream) {
        try {
            if (!(inputStream.available() > 0)) {
                return Optional.empty();
            }
            final char[] inputBuffer = new char[inputStream.available()];
            final InputStreamReader inputReader = new InputStreamReader(inputStream);
            final int read = inputReader.read(inputBuffer);

            List<String> message = new ArrayList<>();

            try (Scanner scanner = new Scanner(new String(inputBuffer))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    message.add(line);
                }
            }
            return Optional.of(message);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
