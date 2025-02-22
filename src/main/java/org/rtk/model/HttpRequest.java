package org.rtk.model;

import org.rtk.httpserver.HttpMethod;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private HttpMethod httpMethod;
    private URI uri;
    private Map<String, List<String>> requestHeaders;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, List<String>> getRequestHeaders() {
        return requestHeaders;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setRequestHeaders(Map<String, List<String>> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
}
