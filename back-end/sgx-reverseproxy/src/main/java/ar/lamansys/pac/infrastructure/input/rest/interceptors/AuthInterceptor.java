package ar.lamansys.pac.infrastructure.input.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthInterceptor {
    boolean process(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
