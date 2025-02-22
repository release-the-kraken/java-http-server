package org.rtk.httpserver;

import org.rtk.model.HttpResponse;
import org.rtk.model.HttpRequest;

public interface RequestRunner {
    HttpResponse run(HttpRequest request);
}
