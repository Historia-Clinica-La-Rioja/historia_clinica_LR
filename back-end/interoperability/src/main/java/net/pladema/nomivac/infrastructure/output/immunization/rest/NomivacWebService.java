package net.pladema.nomivac.infrastructure.output.immunization.rest;

import ca.uhn.fhir.rest.api.MethodOutcome;
import net.pladema.hl7.supporting.conformance.FhirClientR4;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.hl7.fhir.r4.model.Immunization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Conditional(NomivacCondition.class)
public class NomivacWebService {

    private final Logger logger;

    private final FhirClientR4 fhirClientR4;

    public NomivacWebService(FhirClientR4 fhirClientR4) {
        this.fhirClientR4 = fhirClientR4;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public NomivacImmunizationPostResponse postImmunization(Immunization immunization){
        try {
            var response = fhirClientR4.postImmunizationToNomivac(immunization);
            return mapResponse(response);
        } catch (Exception e) {
            logger.error("Error api", e);
            return new NomivacImmunizationPostResponse(null, e.getMessage(), HttpStatus.BAD_GATEWAY.value(), true);
        }
    }

    private NomivacImmunizationPostResponse mapResponse(MethodOutcome methodOutcome) {
        logger.debug("Map response from MethodOutcome -> {}", methodOutcome);
        return new NomivacImmunizationPostResponse(null, null, HttpStatus.OK.value(), false);
    }
}
