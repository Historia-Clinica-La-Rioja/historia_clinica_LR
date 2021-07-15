package net.pladema.snowstorm.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnowstormPort;
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

    public String mapSctidToNomivacId(String sctid) {
        logger.debug("Start map sctid {} to NomivacId", sctid);
        var result = fetchNomivacRefset.run(sctid);
        if (result == null)
            return null;
        logger.debug("Finish map sctid {} to NomivacId {}", sctid, result.getMapTarget());
        return result.getMapTarget();
    }
}
