package ar.lamansys.base.application.reverseproxyrest.handler;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.NonNull;

public class RestResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(@NonNull ClientHttpResponse httpResponse) {
		// Para el proxy reverso el error se debe retornar tal cual venga
		// por lo tanto nunca son manejados como errores
		return false;
	}

	@Override
	public void handleError(@NonNull ClientHttpResponse httpResponse) {
		// Esto se ejecuta si hasError retorna true
	}
}