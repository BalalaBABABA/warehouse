package com.abc.warehouse.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InterceptorManager {

    private final List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void removeInterceptor(HandlerInterceptor interceptor) {
        interceptors.remove(0);
    }

    public List<HandlerInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }
}
