package org.rtk.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HttpResponse {
    private final Map<String, List<String>> responseHeaders;
    private final int statusCode;
    private final Optional<Object> body;

    public HttpResponse(Map<String, List<String>> responseHeaders, int responseCode, Optional<Object> body) {
        this.responseHeaders = responseHeaders;
        this.statusCode = responseCode;
        this.body = body;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Optional<Object> getBody() {
        return body;
    }

    public static class Builder{
        private final Map<String, List<String>> responseHeaders;
        private int statusCode;
        private Optional<Object> body;

        public Builder() {
            this.responseHeaders = new HashMap<>();
            responseHeaders.put("Server", List.of("MyServer"));
            responseHeaders.put("Date", List.of(DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC))));
        }

        public Builder setStatusCode(final int statusCode){
            this.statusCode = statusCode;
            return this;
        }

        public Builder addHeader(final String key, final String value){
            responseHeaders.put(key, List.of(value));
            return this;
        }

        public Builder setBody(final Object body){
            if(Objects.nonNull(body)){
                this.body = Optional.of(body);
            }
            return this;
        }

        public HttpResponse build(){
            return new HttpResponse(responseHeaders, statusCode, body);
        }
    }
}
