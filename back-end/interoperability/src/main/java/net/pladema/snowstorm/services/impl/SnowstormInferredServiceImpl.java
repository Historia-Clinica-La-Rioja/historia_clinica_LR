package net.pladema.snowstorm.services.impl;

import net.pladema.snowstorm.services.SnowstormInferredService;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.inferredrules.InferredAllergyAttributes;
import net.pladema.snowstorm.services.inferredrules.InferredAllergyRules;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnowstormInferredServiceImpl implements SnowstormInferredService {

    private final SnowstormService snowstormService;

    public SnowstormInferredServiceImpl(SnowstormService snowstormService){
        this.snowstormService = snowstormService;
    }

    @Override
    public InferredAllergyAttributes getInferredAllergyAttributes(String conceptId) {
        List<SnowstormItemResponse> ancestors = snowstormService.getConceptAncestors(conceptId);
        if(!ancestors.isEmpty())
            return InferredAllergyRules.inferred(ancestors);
        return null;
    }
}
