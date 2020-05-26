package ru.serj.currencyconversionservice.gateway;

import brave.Span;
import brave.Tracer;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class MyFilter extends GenericFilterBean {

    private final Tracer tracer;

    public MyFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        ((HttpServletResponse) servletResponse).addHeader("ZIPKIN-TRACE-ID", currentSpan.context().traceIdString());
        currentSpan.tag("custom", "tag");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
