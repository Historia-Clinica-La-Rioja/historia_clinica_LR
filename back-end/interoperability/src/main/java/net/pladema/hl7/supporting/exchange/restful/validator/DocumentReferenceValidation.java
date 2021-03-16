package net.pladema.hl7.supporting.exchange.restful.validator;

import ca.uhn.fhir.rest.param.TokenParam;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;
import net.pladema.hl7.supporting.exchange.documents.profile.FhirDocument;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.supporting.exchange.restful.IResponseOperationOutcome;
import net.pladema.hl7.dataexchange.model.domain.BundleVo;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Component
public class DocumentReferenceValidation implements IResponseOperationOutcome {

    private final FhirDocument fhirDocument;

    public DocumentReferenceValidation(FhirDocument fhirDocument){
        super();
        this.fhirDocument=fhirDocument;
    }

    public Optional<Bundle> inputParameter(@NotNull TokenParam subject,
                               @NotNull TokenParam custodian,
                               @NotNull TokenParam type, String dominio){

        //''subject' parameter must have the system domain with a local patient ID
        if(!subject.getSystem().equals(dominio) && subject.isEmpty())
            badRequest("The URI \""+subject.getSystem()+"\" is not a valid system identifier");

        //'custodian' parameter has to be the system domain
        if(!custodian.getSystem().equals(dominio))
            badRequest("TokenParam[system="+subject.getSystem()+",value="+subject.getValue()+"] no esta federado para TokenParam[system=,value="+custodian.getSystem());

        //'type' parameter must have a loinc system
        if(!type.getSystem().equals(CodingSystem.LOINC))
            badRequest("type parameter must have a loinc system");
        else {
            if(Boolean.FALSE.equals(fhirDocument.isSupported(type))) {
                //return empty Bundle with status 200
                return Optional.of(BundleResource.empty());
            }
        }
        return Optional.empty();
    }

    public Optional<Bundle> data(BundleVo data, String patientId){
        //the patient does not exist, a warning is registered due to a possible federation error
        if(!data.existsPatient()){
            Bundle document = BundleResource.empty();
            document.addEntry(notFound("There is no matching patient for MRN "+ patientId));
            return Optional.of(document);
        }
        else if(!data.hasDocuments()){
            //the patient has no registered documents
            return Optional.of(BundleResource.empty());
        }
        return Optional.empty();
    }
}
