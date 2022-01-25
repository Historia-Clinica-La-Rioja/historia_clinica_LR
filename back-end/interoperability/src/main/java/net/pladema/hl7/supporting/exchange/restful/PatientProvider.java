package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.model.domain.dto.IdentifierDto;
import net.pladema.hl7.dataexchange.model.domain.dto.OrganizationDto;
import net.pladema.hl7.dataexchange.model.domain.dto.PatientSummaryDto;
import net.pladema.hl7.supporting.conformance.FhirClientR4;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.conformance.exceptions.FhirClientException;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;
import net.pladema.hl7.supporting.exchange.documents.profile.FhirDocument;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RestController
@RequestMapping("/masterfile-federacion-service/Patient")
@Conditional(InteroperabilityCondition.class)
@Tag(name = "Fhir Patient Provider", description = "Fhir Patient Provider")
public class PatientProvider {

    private final FhirClientR4 client;
    private final BundleResource bundleResource;

    @Value("${ws.federar.claims.iss}")
    private String wsIss;

    public PatientProvider(FhirClientR4 client,
                           BundleResource bundleResource){
        super();
        this.client=client;
        this.bundleResource=bundleResource;
    }

    /**
     * Es una búsqueda de dominios en los que un paciente está federado.
     * @param id identificador local del paciente en nuestro dominio
     * @return lista de dominios para que el profesional seleccione una
     */
    @GetMapping(value = "/patient-location")
    public ResponseEntity<List<OrganizationDto>> getPatientLocation(
            @RequestParam(name = Patient.SP_IDENTIFIER) String id) throws FhirClientException {
        String domainIdentifier = wsIss + "|" + id;
        List<OrganizationDto> result = new ArrayList<>();
        Bundle data = client.operationPatientLocation(new StringType(domainIdentifier));
        data.getEntry().forEach(entry -> {
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
                //!i.getValue().contains("dummy") &&
                !i.getValue().equals(IResourceFhir.getDominio());
    }

    private boolean hasHealthcareData(DocumentReference documentReference){
        return documentReference != null && documentReference.hasContent();
    }

    private DocumentReference getDocumentReference(String subject, String custodian, String type){

        //build parameters appropriately
        ReferenceParam subjectParam = new ReferenceParam(subject);
        StringParam custodianParam = new StringParam(custodian);
        ReferenceParam typeParam = new ReferenceParam(
                Objects.requireNonNullElseGet(type, FhirDocument::defaultStringType));

        try {
            return client.readDocumentReferences(subjectParam, custodianParam, typeParam);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param subject identificador local del paciente
     * @param custodian dominio en el cuál se está buscando información del paciente
     * @param type documento requerido
     * @return resumen de historia clínica del paciente registrada en otro dominio
     */
    @GetMapping
    public ResponseEntity<PatientSummaryDto> findPatient(
            @RequestParam(name = DocumentReference.SP_SUBJECT) String subject,
            @RequestParam(name = DocumentReference.SP_CUSTODIAN) String custodian,
            @RequestParam(name = DocumentReference.SP_TYPE, required = false) String type) {

        String subjectIdentifier = wsIss + "|" + subject;
        DocumentReference document = getDocumentReference(subjectIdentifier, custodian, type);
        if (hasHealthcareData(document)) {
            DocumentReference.DocumentReferenceContentComponent content = document.getContent().get(0);
            if (content.hasAttachment()) {
                String url = content.getAttachment().getUrl();
                Bundle bundle = client.getResourceById(new IdType(url));
                PatientSummaryDto psdto = new PatientSummaryDto(
                        bundleResource.encodeResourceToSummary(bundle));
                formatDto(psdto, custodian);
                return ResponseEntity.ok(psdto);
            }
        }
        return ResponseEntity.ok(PatientSummaryDto.emptyPatientSummary());
    }

    private void formatDto(PatientSummaryDto psdto, String custodian) {
        if(custodian.contains("dummy")){
            String dummyApp = "Aplicacion dummy";
            OrganizationDto dummy = new OrganizationDto();
            dummy.setName(dummyApp);
            dummy.setId(dummyApp);
            IdentifierDto dummyIdentifier = new IdentifierDto();
            dummyIdentifier.setSystem(dummyApp);
            dummyIdentifier.setValue(dummyApp);
            dummy.setIdentifier(dummyIdentifier);
            psdto.setOrganization(dummy);
        }

    }
}
