package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasureRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MonitoringListener {

	private final RestClientMeasureRepository restClientMeasureRepository;

	public MonitoringListener(RestClientMeasureRepository restClientMeasureRepository) {
		super();
		this.restClientMeasureRepository = restClientMeasureRepository;
	}

	@Async
	@EventListener
	void handleAsyncEvent(OnRequestEvent event) {
		this.restClientMeasureRepository.save(event.getRestClientMeasure());
	}
}