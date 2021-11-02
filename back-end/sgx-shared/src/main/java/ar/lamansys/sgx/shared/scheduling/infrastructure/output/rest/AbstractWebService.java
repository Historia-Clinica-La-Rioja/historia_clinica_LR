package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractWebService<Y extends TAWSPayload, C, L> implements WebService<Y, C> {

	private final Logger logger;
	private final String relUrl;
	private final RestClient restClient;
	private final Class<C> typeCreateResponse;
	private final Class<L> typeListResponse;

	public AbstractWebService(String relUrl, RestClient restClient,
							  Class<C> typeCreateResponse, Class<L> typeListResponse) {
		this.restClient = restClient;
		this.relUrl = relUrl;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.typeCreateResponse = typeCreateResponse;
		this.typeListResponse = typeListResponse;
	}

	@Override
	public C createOrUpdate(Y payload) throws TAWSException {
		return payload.alreadyExists() ? restClient.exchangePut(relUrl, payload, typeCreateResponse).getBody()
				: restClient.exchangePost(relUrl, payload, typeCreateResponse).getBody();
	}

	public L list() throws TAWSException {
		return restClient.exchangeGet(relUrl, typeListResponse).getBody();
	}

	public TAWSDeleteResponse delete(String id) throws TAWSException {
		return restClient.exchangeDelete(String.format("%s/%s", relUrl, id), TAWSDeleteResponse.class).getBody();
	}

	private void delete(TAWSPayload entity) {
		try {
			TAWSDeleteResponse deleteResponse = delete(entity.getIdExterno());
			logger.debug("DELETE {} response {}", entity.getIdExterno(), deleteResponse);
		} catch (TAWSException e) {
			logger.error("DELETE {} response {}", entity.getIdExterno(), e.getMessage(), e);
		}
	}
	
	public boolean isMocked() {
		return restClient.isMocked();
	}
}
