package net.pladema.hl7.supporting.exchange.restful;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import io.swagger.annotations.Api;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirAddress;
import net.pladema.hl7.dataexchange.model.domain.*;
import net.pladema.hl7.dataexchange.model.domain.dto.IdentifierDto;
import net.pladema.hl7.dataexchange.model.domain.dto.OrganizationDto;
import net.pladema.hl7.dataexchange.model.domain.dto.PatientSummaryDto;
import net.pladema.hl7.supporting.conformance.FhirClientR4;
import net.pladema.hl7.supporting.exchange.documents.BundleResource;
import net.pladema.hl7.supporting.exchange.documents.profile.FhirDocument;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RestController
@RequestMapping("/masterfile-federacion-service/Patient")
@ConditionalOnProperty(value="ws.renaper.enabled", havingValue = "true")
@Api(value = "Fhir Patient Provider", tags = {"Fhir Patient Provider"})
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
            @RequestParam(name = Patient.SP_IDENTIFIER) String id) {
        //if(!id.equals("4858"))
        //    return ResponseEntity.ok(mockOrganizations());
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
                PatientSummaryDto psdto = new PatientSummaryDto(bundleResource.encodeResourceToSummary(
                        client.getResourceById(new IdType(url))));
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

    private PatientSummaryDto mockSummary(String custodian){
        PatientSummaryVo psvo = new PatientSummaryVo();

        AllergyIntoleranceVo aivo = new AllergyIntoleranceVo();
        aivo.setType("1");
        aivo.setCriticality("2");
        aivo.setSctidCode("1");
        aivo.setSctidTerm("alergia");
        aivo.setId("Alergia a las abejas");
        psvo.addAllergy(aivo);

        ConditionVo cvo = new ConditionVo();
        cvo.setId("Enfermedad interoperabilidad");
        cvo.setSctidCode("3");
        cvo.setSeverityCode("2");
        cvo.setSctidTerm("Enfermedad");
        cvo.setVerificationStatus("Verificado");

        ConditionVo cvo2 = new ConditionVo();
        cvo2.setId("Fiebre de la interoperabilidad");
        cvo2.setSctidCode("5");
        cvo2.setSeverityCode("6");
        cvo2.setSctidTerm("Enfermedad");
        cvo2.setVerificationStatus("Verificado");

        ConditionVo cvo3 = new ConditionVo();
        cvo3.setId("Infección de la interoperabilidad");
        cvo3.setSctidCode("7");
        cvo3.setSeverityCode("9");
        cvo3.setSctidTerm("Enfermedad");
        cvo3.setVerificationStatus("Verificado");

        ImmunizationVo immvo = new ImmunizationVo();
        immvo.setBatchNumber("3");
        immvo.setAdministrationDate(LocalDate.now());
        immvo.setDoseNumber(3);
        immvo.setId("Vacuna covid");
        immvo.setVaccineCode("SputnikV");
        psvo.addImmunization(immvo);

        MedicationVo mvo = new MedicationVo();
        mvo.setId("Medicación interoperabilidad");
        mvo.setStatus("55561003");

        MedicationVo mvo2 = new MedicationVo();
        mvo2.setId("Medicación interoperabilidad 2");
        mvo2.setStatus("55561003");

        MedicationVo mvo3 = new MedicationVo();
        mvo3.setId("Medicación interoperabilidad 3");
        mvo3.setStatus("73425007");


        if(custodian.contains("33")){
            psvo.addCondition(cvo);
            psvo.addCondition(cvo2);
            psvo.addMedication(mvo);
            psvo.addMedication(mvo2);
        } else {
            psvo.addCondition(cvo3);
            psvo.addMedication(mvo3);
        }

        FhirAddress fadd = new FhirAddress(null);
        fadd.setAddress("Constitucion", "350","2","b");
        fadd.setCity("Tandil");
        fadd.setCountry("Argentina");
        fadd.setPostcode("7000");
        fadd.setProvince("Buenos aires");

        String name;
        if(custodian.contains("33"))
            name = "Ministero de Salud de Santiago del Estero";
        else
            name = "Ministerio de Salud de Santa Fe";

        OrganizationVo ovo = new OrganizationVo();
        ovo.setName(name);
        ovo.setFullAddress(fadd);
        psvo.setOrganization(ovo);

        PatientVo pvo = new PatientVo();
        pvo.setFirstname("Alberto");
        pvo.setLastname("Fernández");
        pvo.setId("2");
        pvo.setGender("Male");
        psvo.setPatient(pvo);

        pvo.setFullAddress(fadd);
        pvo.setBirthdate(LocalDate.now());
        return new PatientSummaryDto(psvo);
    }

    private List<OrganizationDto> mockOrganizations(){
        List<OrganizationDto> organizationDtos = new ArrayList<>();
        FhirAddress fadd = new FhirAddress(null);
        fadd.setAddress("Constitucion", "350","2","b");
        fadd.setCity("Tandil");
        fadd.setCountry("Argentina");
        fadd.setPostcode("7000");
        fadd.setProvince("Buenos aires");

        OrganizationVo ovo = new OrganizationVo();
        ovo.setName("Institución de prueba");
        ovo.setFullAddress(fadd);
        OrganizationDto odto = new OrganizationDto(ovo);
        IdentifierDto idto = new IdentifierDto();
        idto.setValue("23");
        idto.setSystem("Institución de prueba");
        odto.setIdentifier(idto);
        organizationDtos.add(odto);

        FhirAddress fadd2 = new FhirAddress(null);
        fadd2.setAddress("Alem", "1110","1","b");
        fadd2.setCity("Tandil");
        fadd2.setCountry("Argentina");
        fadd2.setPostcode("7000");
        fadd2.setProvince("Buenos aires");

        OrganizationVo ovo2 = new OrganizationVo();
        ovo2.setName("Otra institución de prueba");
        ovo2.setFullAddress(fadd2);
        OrganizationDto odto2 = new OrganizationDto(ovo2);
        IdentifierDto idto2 = new IdentifierDto();
        idto2.setValue("33");
        idto2.setSystem("Otra institución de prueba");
        odto2.setIdentifier(idto2);
        organizationDtos.add(odto2);

        return organizationDtos;
    }
}
