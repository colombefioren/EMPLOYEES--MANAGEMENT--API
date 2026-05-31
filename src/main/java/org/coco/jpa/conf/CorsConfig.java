package org.coco.jpa.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.cors.allowed-origins:}")
  private String allowedOrigins;

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    if (allowedOrigins == null || allowedOrigins.isBlank()) {
      registry
          .addMapping("/**")
          .allowedOrigins("*")
          .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
          .allowedHeaders("*")
          .exposedHeaders("X-Total-Count");
    } else {
      String[] origins = allowedOrigins.split(",");
      registry
          .addMapping("/**")
          .allowedOrigins(origins)
          .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
          .allowedHeaders("*")
          .exposedHeaders("X-Total-Count")
          .allowCredentials(true);
    }
  }
}
