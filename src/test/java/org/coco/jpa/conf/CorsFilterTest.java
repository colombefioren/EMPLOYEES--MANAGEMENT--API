package org.coco.jpa.conf;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CorsFilterTest {

  private final CorsFilter filter = new CorsFilter();

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain chain;

  @Test
  void should_set_default_origin_when_no_origin_header() throws Exception {
    when(request.getHeader("Origin")).thenReturn(null);
    when(request.getMethod()).thenReturn("GET");

    filter.doFilter(request, response, chain);

    verify(response)
        .setHeader("Access-Control-Allow-Origin", "https://employees-admin-mauve.vercel.app");
    verify(chain).doFilter(request, response);
  }

  @Test
  void should_use_request_origin_when_present() throws Exception {
    when(request.getHeader("Origin")).thenReturn("https://example.com");
    when(request.getMethod()).thenReturn("GET");

    filter.doFilter(request, response, chain);

    verify(response).setHeader("Access-Control-Allow-Origin", "https://example.com");
    verify(chain).doFilter(request, response);
  }

  @Test
  void should_return_ok_for_options_request() throws Exception {
    when(request.getHeader("Origin")).thenReturn("https://example.com");
    when(request.getMethod()).thenReturn("OPTIONS");

    filter.doFilter(request, response, chain);

    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(chain, never()).doFilter(request, response);
  }

  @Test
  void should_set_all_cors_headers() throws Exception {
    when(request.getHeader("Origin")).thenReturn("*");
    when(request.getMethod()).thenReturn("GET");

    filter.doFilter(request, response, chain);

    verify(response)
        .setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
    verify(response).setHeader("Access-Control-Allow-Headers", "*");
    verify(response).setHeader("Access-Control-Expose-Headers", "X-Total-Count");
    verify(response).setHeader("Access-Control-Allow-Credentials", "true");
    verify(response).setHeader("Access-Control-Max-Age", "3600");
  }

  @Test
  void should_wrap_exception_in_io_exception() throws Exception {
    when(request.getHeader("Origin")).thenReturn("*");
    when(request.getMethod()).thenReturn("GET");
    doThrow(new RuntimeException("chain failed")).when(chain).doFilter(request, response);

    assertThrows(java.io.IOException.class, () -> filter.doFilter(request, response, chain));
  }
}
