package net.pladema.hl7.supporting.security;

import ca.uhn.fhir.rest.server.interceptor.CorsInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

public class ApiCorsInterceptor extends CorsInterceptor {

    public ApiCorsInterceptor(){
        super();
        this.setConfig(configuration());
    }

    private CorsConfiguration configuration(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader(HttpHeaders.ACCEPT);
        configuration.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
        configuration.addAllowedHeader(HttpHeaders.ORIGIN);

        configuration.addAllowedOrigin("*");

        configuration.addExposedHeader("Location");
        configuration.addExposedHeader("Content-Location");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        return configuration;
    }
}
