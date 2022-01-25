package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasureRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MonitoringListener {

	private final RestClientMeasureRepository restClientMeasureRepository;

	private final boolean enableStorage;

	public MonitoringListener(RestClientMeasureRepository restClientMeasureRepository,
							  @Value("${monitoring.rest-client.storage.enable:false}") boolean enableStorage) {
		super();
		this.restClientMeasureRepository = restClientMeasureRepository;
		this.enableStorage = enableStorage;
	}

	@Async
	@EventListener
	void handleAsyncEvent(OnRequestEvent event) {
		if (enableStorage)
			this.restClientMeasureRepository.save(event.getRestClientMeasure());
	}
}