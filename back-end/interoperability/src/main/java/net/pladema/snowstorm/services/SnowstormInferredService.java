package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.inferredrules.InferredAllergyAttributes;

public interface SnowstormInferredService {

    InferredAllergyAttributes getInferredAllergyAttributes(String conceptId);

}
