package net.pladema.snowstorm.services.fetchNomivacRefset;

import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.nomivac.SnowstormNomivacItemResponse;
import net.pladema.snowstorm.services.domain.nomivac.SnowstormNomivacRefsetMembersResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.impl.CalculateCie10CodesServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FetchNomivacRefset {

    private final Logger logger;

    private final SnowstormService snowstormService;

    public static final String NOMIVAC_REFERENCE_SET_ID = "401211000221103";


    public FetchNomivacRefset(SnowstormService snowstormService) {
        super();
        this.snowstormService = snowstormService;
        logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    }

    public SnowstormNomivacItemResponse run(String referencedComponentId) throws SnowstormApiException {
        final String NOMIVAC_LIMIT = "1";
        logger.debug("FetchNomivacRefset to referencedComponentId {}", referencedComponentId);
        try {
            SnowstormNomivacRefsetMembersResponse snowstormResponse = snowstormService.getRefsetMembers(referencedComponentId, NOMIVAC_REFERENCE_SET_ID, NOMIVAC_LIMIT, SnowstormNomivacRefsetMembersResponse.class);
            if (snowstormResponse.getTotal() >= 1)
                return snowstormResponse.getItems().get(0);
        } catch (SnowstormApiException e) {
            return null;
        }
        return null;
    }
}
