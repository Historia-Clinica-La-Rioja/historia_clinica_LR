package ar.lamansys.sgx.shared.proxy.reverse.resttemplate;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class ReverseProxyResponseErrorHandler
  implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        // Para el proxy reverso el error se debe retornar tal cual venga
        // por lo tanto nunca son manejados como errores
        return false;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        // Esto se ejecuta si hasError retorna true
    }
}