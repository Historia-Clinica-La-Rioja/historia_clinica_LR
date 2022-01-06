package net.pladema.snowstorm.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.SharedSnowstormPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortEnumException;
import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import net.pladema.snowstorm.services.domain.nomivac.SnowstormNomivacItemResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.fetchNomivacRefset.FetchNomivacRefset;
import net.pladema.snowstorm.services.impl.CalculateCie10CodesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SnowstormExternalService implements SharedSnowstormPort {

    private final Logger logger;

    private final FetchNomivacRefset fetchNomivacRefset;

    public SnowstormExternalService(FetchNomivacRefset fetchNomivacRefset) {
        super();
        this.fetchNomivacRefset = fetchNomivacRefset;
        logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    }

    @Override
    public String mapSctidToNomivacId(String sctid)  throws SnowstormPortException {
        logger.debug("Start map sctid {} to NomivacId", sctid);
        SnowstormNomivacItemResponse result = null;
        try {
            result = fetchNomivacRefset.run(sctid);
        } catch (SnowstormApiException e) {
            throw new SnowstormPortException(SnowstormPortEnumException.valueOf(e.getCode().name()), e.getStatusCode(), e.getMessage());
        }
        if (result == null)
            return null;
        logger.debug("Finish map sctid {} to NomivacId {}", sctid, result.getMapTarget());
        return result.getMapTarget();
    }
}
