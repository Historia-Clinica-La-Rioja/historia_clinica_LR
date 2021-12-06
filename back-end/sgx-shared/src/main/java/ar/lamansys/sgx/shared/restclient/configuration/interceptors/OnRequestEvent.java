package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasure;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class OnRequestEvent extends ApplicationEvent {
	/**
	 *
	 */
	private static final long serialVersionUID = 6147904039545771860L;

	private RestClientMeasure restClientMeasure;

	public OnRequestEvent(RestClientMeasure restClientMeasure) {
		super(restClientMeasure);
		this.restClientMeasure = restClientMeasure;
	}

}