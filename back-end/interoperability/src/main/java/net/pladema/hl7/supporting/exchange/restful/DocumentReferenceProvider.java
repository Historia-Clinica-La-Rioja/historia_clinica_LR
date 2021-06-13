package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import net.pladema.hl7.dataexchange.model.adaptor.FhirParam;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;
import net.pladema.hl7.supporting.exchange.documents.profile.FhirDocument;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/fhir")
@Conditional(InteroperabilityCondition.class)
public class DocumentReferenceProvider implements IResourceProvider {


    private final BundleResource bundleResource;

    public DocumentReferenceProvider(BundleResource bundleResource){
        this.bundleResource = bundleResource;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return DocumentReference.class;
    }

    /**
     * @param subject valor de sistema del dominio acompañado de un identificador local de paciente
     * @param custodian valor de sistema del dominio (uri asignada al dominio por el federador nacional)
     * @param type system 'http://loinc.org' acompañado de un identificador de loinc de documentos
     * @return bundle (puede pensarse como una lista) de recursos DocumentReference
     */
    @GetMapping(value = "/DocumentReference")
    @Search
    public Bundle getExistingDocumentsReferences(
            @RequiredParam(name = DocumentReference.SP_SUBJECT) String subject,
            @RequiredParam(name = DocumentReference.SP_CUSTODIAN) String custodian,
            @OptionalParam(name = DocumentReference.SP_TYPE) String type){

        //build parameters appropriately
        TokenParam subjectParam = FhirParam.newTokenParam(subject);
        TokenParam custodianParam = FhirParam.newTokenParam(custodian);
        TokenParam typeParam = Objects.requireNonNullElseGet(
                FhirParam.newTokenParam(type), FhirDocument::defaultType);
        return bundleResource.getExistingDocumentsReferences(subjectParam, custodianParam, typeParam);
    }
}
