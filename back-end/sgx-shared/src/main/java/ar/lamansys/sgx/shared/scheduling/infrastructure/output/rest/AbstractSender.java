package ar.lamansys.sgx.shared.scheduling.infrastructure.output.rest;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractData;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.AbstractSync;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.ESyncError;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.PersistenceService;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.SynchData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class AbstractSender< I extends Number,
									D extends AbstractData<I>,
									S extends AbstractSync<I>,
									R extends TAWSResponse,
									Y extends TAWSPayload> {

	private final PersistenceService<D, S, I> persistence;
	private final WebService<Y, R> webService;
	private final Logger logger;
	private final ESyncError eSyncErrorEntity;

	protected AbstractSender(PersistenceService<D, S, I> persistence, WebService<Y, R> webService,
						  ESyncError entity) {
		this.persistence = persistence;
		this.webService = webService;
		this.eSyncErrorEntity = entity;
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	public boolean sendBatch() {
		Optional<SynchData<D, S, I>> synchDataToSend = persistence.firstEntityToSend();
		synchDataToSend.ifPresent(synchData -> {
			try {
				if (synchData.shouldSendDelete()) {
					webService.delete(synchData.sync.getExternalId());
					persistence.markEntityAsSent(synchData.sync, synchData.sync.getExternalId());
				} else {
					Y payload = mapEntityToPayload(synchData.sync.getExternalId(), synchData.data);
					TAWSResponse dataCreated = createOrUpdate(synchData, payload);
					persistence.markEntityAsSent(synchData.sync, dataCreated.externalId);
				}
			} catch (TAWSException e) {
				persistence.markEntityWithError(synchData.sync, e.httpStatus, e.getMessage(), eSyncErrorEntity);
				logger.error(e.getMessage());
			}

		});

		return synchDataToSend.isPresent();
	}

	private TAWSResponse createOrUpdate(SynchData<D, S, I> synchData, Y payload) throws TAWSException {
		return webService.isMocked() ? new TAWSResponse(synchData.data.getId().toString(), 0)
				: webService.createOrUpdate(payload);
	}

	private Y mapEntityToPayload(String externalId, D data) {
		Y payload = mapEntityToPayload(data);
		payload.setIdExterno(externalId);
		return payload;
	}

	protected abstract Y mapEntityToPayload(D data);

}
