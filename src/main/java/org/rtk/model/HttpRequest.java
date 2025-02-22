package org.rtk.model;

import org.rtk.httpserver.HttpMethod;

import java.net.URI;
import java.util.List;
import java.util.Map;

public record HttpRequest(HttpMethod httpMethod,
                          URI uri, Map<String,
                          List<String>> requestHeaders) {
}
