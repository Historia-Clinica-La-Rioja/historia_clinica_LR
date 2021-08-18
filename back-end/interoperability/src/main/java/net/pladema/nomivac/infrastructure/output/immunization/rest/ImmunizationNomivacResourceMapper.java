package net.pladema.nomivac.infrastructure.output.immunization.rest;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.nomivac.domain.immunization.ImmunizationBo;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Conditional(NomivacCondition.class)
public class ImmunizationNomivacResourceMapper extends ISingleResourceFhir {

    @Autowired
    public ImmunizationNomivacResourceMapper(FhirPersistentStore store){
        super(store);
    }

    @Override
    public IBaseResource fetch(String id, Map<ResourceType, Reference> references) {
        return null;
    }

    public Immunization map(ImmunizationBo immunization){

        Immunization resource = new Immunization();
        resource.setId(String.valueOf(immunization.getId()));

        resource.setStatus(Immunization.ImmunizationStatus.COMPLETED);

        resource.setVaccineCode(
                IResourceFhir.newCodeableConcept(CodingSystem.SNOMED,
                        new FhirCode(immunization.getVaccineSctid(), immunization.getVaccinePt()))
        );

        resource.getPatient().setIdentifier(newIdentifier(CodingSystem.RENAPER, immunization.getPatientIdentificationNumber()));
        resource.getPatient().setDisplay(immunization.getPatientName());

        resource.getLocation().setIdentifier(newIdentifier(CodingSystem.REFES2, immunization.getLocationSisaCode()));
        resource.getLocation().setDisplay(immunization.getLocationName());

        resource.getOccurrenceDateTimeType().setValue(FhirDateMapper.toDate(immunization.getAdministrationDate()));
        resource.getOccurrenceDateTimeType().setPrecision(TemporalPrecisionEnum.DAY);

        resource.setLotNumber(immunization.getLotNumber());

        resource.setReasonCode(List.of(createReasonCode(immunization)));

        resource.setProtocolApplied(List.of(createProtocolApplied(immunization)));

        addNonRequiredAttributes(resource, immunization);
        return resource;
    }

    private CodeableConcept createReasonCode(ImmunizationBo immunization) {
        CodeableConcept result = IResourceFhir.newCodeableConcept(CodingSystem.Immunization.NOMIVACONDITIONCS,
                new FhirCode(String.valueOf(immunization.getConditionId()), immunization.getConditionDescription()));
        if (result != null)
            result.setText(immunization.getConditionDescription());
        return result;
    }

    private Immunization.ImmunizationProtocolAppliedComponent createProtocolApplied(ImmunizationBo immunization) {
        Immunization.ImmunizationProtocolAppliedComponent result = new Immunization.ImmunizationProtocolAppliedComponent();
        result.setSeries(immunization.getSchemeDescription());
        result.getSeriesElement().addExtension(createExtension(immunization));
        result.getDoseNumberPositiveIntType().setValue(immunization.getDoseOrder().intValue());
        return result;
    }

    private Extension createExtension(ImmunizationBo immunization) {
        Extension result = new Extension();
        result.setUrl(CodingProfile.Immunization.NOMIVAC.URL);
        result.setValue(new Coding(CodingSystem.Immunization.NOMIVACESCHEMA,String.valueOf(immunization.getSchemeId()), immunization.getSchemeDescription()));
        return result;
    }

    private void addNonRequiredAttributes(Immunization resource, ImmunizationBo immunization){
        resource.setPrimarySource(true);
        resource.setExpirationDate(FhirDateMapper.toDate(immunization.getExpirationDate()));
    }


    @Override
    public ResourceType getResourceType() {
        return ResourceType.Immunization;
    }


}
