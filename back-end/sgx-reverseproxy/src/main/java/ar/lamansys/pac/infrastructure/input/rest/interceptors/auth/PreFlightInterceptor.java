package ar.lamansys.pac.infrastructure.input.rest.interceptors.auth;

import ar.lamansys.pac.infrastructure.input.rest.interceptors.AuthInterceptor;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@AllArgsConstructor
public class PreFlightInterceptor implements AuthInterceptor {

    private final AuthInterceptor nextAuthInterceptor;

    @Override
    public boolean process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isPreFlightRequest(request))
            return true;
        return nextAuthInterceptor.process(request, response);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return ("OPTIONS".equals(request.getMethod()) && request.getHeader("Access-Control-Request-Method") != null);
    }
}
