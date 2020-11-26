package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import io.swagger.annotations.Api;
import net.pladema.hl7.dataexchange.model.adaptor.FhirParam;
import net.pladema.hl7.dataexchange.model.domain.dto.IdentifierDto;
import net.pladema.hl7.dataexchange.model.domain.dto.OrganizationDto;
import net.pladema.hl7.supporting.conformance.FhirClientR4;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/masterfile-federacion-service/Patient")
@ConditionalOnProperty(value="ws.renaper.enabled", havingValue = "true")
@Api(value = "Fhir Patient Provider", tags = {"Fhir Patient Provider"})
public class PatientProvider {

    private final FhirClientR4 client;

    @Value("${ws.renaper.dominio}")
    private String dominio;

    public PatientProvider(FhirClientR4 client){
        super();
        this.client=client;
    }

    /**
     * Es una búsqueda de dominios en los que un paciente está federado.
     * @param id identificador local del paciente en nuestro dominio
     * @return lista de dominios para que el profesional seleccione una
     */
    @GetMapping(value = "/patient-location")
    public ResponseEntity<List<OrganizationDto>> getPatientLocation(@RequestParam(name = Patient.SP_IDENTIFIER) String id) {
        List<OrganizationDto> result = new ArrayList<>();
        Bundle data = client.operationPatientLocation(new StringType(id));
        data.getEntry().forEach((entry)-> {
            Organization resource = ((Organization) entry.getResource());
            resource.getIdentifier()
                    .stream()
                    .filter(filter())
                    .map(IdentifierDto::new)
                    .findFirst()
                    .ifPresent(identifier -> result.add(new OrganizationDto(resource.getId(), resource.getName(), identifier)));
        });
        //It remains commented for future changes
        /*result.removeIf(organization ->
                !hasHealthcareData(getDocumentReference(
                        FhirParam.getIdentifier(id), organization.getCustodian(), FhirDocument.defaultStringType())
        ));*/
        return ResponseEntity.ok(result);
    }

    private Predicate<Identifier> filter() {
        return (Identifier i) -> i.getSystem().equals(CodingSystem.FEDERADOR) &&
                !i.getValue().contains("dummy") &&
                !i.getValue().equals(dominio);
    }

    private boolean hasHealthcareData(DocumentReference documentReference){
        return documentReference != null && documentReference.hasContent();
    }

    private DocumentReference getDocumentReference(String subject, String custodian, String type){
        //ReferenceParam subjectParam = new ReferenceParam(dominio.concat("|").concat(subject));

        //build parameters appropriately
        ReferenceParam subjectParam = FhirParam.newReferenceParam("https://www.hospitalitaliano.org.ar",subject);
        StringParam custodianParam = new StringParam(custodian);
        ReferenceParam typeParam = new ReferenceParam(type);
        try {
            return client.readDocumentReferences(subjectParam, custodianParam, typeParam);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}