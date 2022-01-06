package net.pladema.snowstorm.services.impl;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.services.SnowstormInferredService;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.inferredrules.InferredAllergyAttributes;
import net.pladema.snowstorm.services.inferredrules.InferredAllergyRules;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SnowstormInferredServiceImpl implements SnowstormInferredService {

    private final SnowstormService snowstormService;

    public SnowstormInferredServiceImpl(SnowstormService snowstormService){
        this.snowstormService = snowstormService;
    }

    @Override
    public InferredAllergyAttributes getInferredAllergyAttributes(String conceptId) {
        try {
            List<SnowstormItemResponse> ancestors = snowstormService.getConceptAncestors(conceptId);
            if(!ancestors.isEmpty())
                return InferredAllergyRules.inferred(ancestors);
        } catch (SnowstormApiException e) {
            log.error("Error snowstorm service");
        }
        return null;
    }
}
