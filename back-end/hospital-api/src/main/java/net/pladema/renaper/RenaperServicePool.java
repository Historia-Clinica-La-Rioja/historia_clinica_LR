package net.pladema.renaper;

import java.util.concurrent.ForkJoinPool;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import lombok.extern.slf4j.Slf4j;
import net.pladema.renaper.services.domain.RenaperServiceException;

@Slf4j
@Service
public class RenaperServicePool {

	private static final String TIMEOUT_MSG = "Timeout en WS Renaper";

	@Value("${ws.renaper.request.timeout}")
	private long requestTimeOut;

	public <T> DeferredResult<ResponseEntity<T>> run(String serviceName, ThrowingSupplier<ResponseEntity<T>> renaperTask) {

		DeferredResult<ResponseEntity<T>> deferredResult = buildDeferredResultWithCallbacks(
				serviceName,
				requestTimeOut
		);
		ForkJoinPool.commonPool().submit(
				() -> {
					try {
						deferredResult.setResult(renaperTask.get());
					} catch (Exception e) {
						deferredResult.setErrorResult(badGateway(e.getMessage()));
					}
				}
		);

		return deferredResult;
	}


	private static ResponseEntity<String> badGateway(String causa) {
		return ResponseEntity.status(HttpStatus.SC_BAD_GATEWAY).body(causa);
	}

	private static <R> DeferredResult<ResponseEntity<R>> buildDeferredResultWithCallbacks(String serviceName, long requestTimeOut) {
		DeferredResult<ResponseEntity<R>> deferredResult = new DeferredResult<>(requestTimeOut);
		deferredResult.onTimeout(() -> {
			log.warn("TimeOut en la invocación del servicio {}", serviceName);
			deferredResult.setErrorResult(badGateway(TIMEOUT_MSG));
		});
		deferredResult.onError(e -> {
			log.warn("Error invocando {} ", serviceName);
			deferredResult.setErrorResult(badGateway(e.toString()));
		});
		return deferredResult;
	}

	@FunctionalInterface
	public interface ThrowingSupplier<T> {
		T get() throws RenaperServiceException;
	}
}
