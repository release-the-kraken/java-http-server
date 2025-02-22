package org.rtk.httphandler;

import org.rtk.model.HttpResponse;
import org.rtk.model.HttpStatusCode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ResponseWriter {
    /**
     * Write a HTTPResponse to an outputstream
     * @param outputStream - the outputstream
     * @param response - the HTTPResponse
     */
    public static void writeResponse(BufferedWriter outputStream, HttpResponse response) {
        try {
            int statusCode = response.getStatusCode();
            String statusCodeDescription = HttpStatusCode.getByValue(statusCode).getDescription();
            List<String> responseHeaders = buildHeaderStrings(response.getResponseHeaders());
            outputStream.write("HTTP/1.1 " + statusCode + " " + statusCodeDescription + "\r\n");
            for (String header : responseHeaders){
                outputStream.write(header);
            }
            Optional<String> bodyString = response.getBody().flatMap(ResponseWriter::getResponseString);
            if (bodyString.isPresent()) {
                final String encodedString = new String(bodyString.get().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                outputStream.write("Content-Length: " + encodedString.getBytes().length + "\r\n");
                outputStream.write("\r\n");
                outputStream.write(encodedString);
            } else {
                outputStream.write("\r\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<String> getResponseString(Object body) {
        // Currently only supporting Strings
        if (body instanceof String) {
            try {
                return Optional.of(body.toString());
            } catch (Exception ignored) {
            }
        }
        return Optional.empty();
    }

    private static List<String> buildHeaderStrings(Map<String, List<String>> responseHeaders) {
        final List<String> responseHeadersList = new ArrayList<>();

        responseHeaders.forEach((name, values) -> {
            final StringBuilder valuesCombined = new StringBuilder();
            values.forEach(valuesCombined::append);
            valuesCombined.append(";");

            responseHeadersList.add(name + ": " + valuesCombined + "\r\n");
        });

        return responseHeadersList;
    }
}
