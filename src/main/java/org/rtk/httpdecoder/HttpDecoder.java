package org.rtk.httpdecoder;

import org.rtk.httpserver.HttpMethod;
import org.rtk.logger.Logger;
import org.rtk.model.HttpRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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

            Logger.error("Exception when building request.", e);
            return Optional.empty();
        }
    }

    private static HttpRequest addRequestHeaders(List<String> msg, HttpRequest request) {
        final Map<String, List<String>> headers = new HashMap<>();

        if (msg.size() > 1) {
            for (int i = 0; i < msg.size(); i++) {
                String header = msg.get(i);
                int colonIndex = header.indexOf(":");
                if (!(colonIndex > 0 && header.length() > colonIndex + 1)) {
                    break;
                }
                String headerKey = header.substring(0, colonIndex);
                String headerValue = header.substring(colonIndex + 1);

                headers.compute(headerKey, (k, v) -> {
                    if (v != null){
                        v.add(headerValue);
                    } else {
                        v = new ArrayList<>();
                    }
                    return v;
                });
            }
        }

        request.setRequestHeaders(headers);
        return request;
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
            Logger.error("Exception when reading message", e);
            return Optional.empty();
        }
    }
}
