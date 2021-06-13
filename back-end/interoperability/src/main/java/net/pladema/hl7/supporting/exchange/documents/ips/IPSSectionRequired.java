package net.pladema.hl7.supporting.exchange.documents.ips;

import net.pladema.hl7.dataexchange.clinical.AllergyIntoleranceResource;
import net.pladema.hl7.dataexchange.clinical.ConditionResource;
import net.pladema.hl7.dataexchange.medications.ImmunizationResource;
import net.pladema.hl7.dataexchange.medications.MedicationStatementResource;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.documents.CompositionResource;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Conditional(InteroperabilityCondition.class)
public class IPSSectionRequired {

    private final ConditionResource conditionResource;
    private final MedicationStatementResource medicationStatementResource;
    private final ImmunizationResource immunizationResource;
    private final AllergyIntoleranceResource allergyIntoleranceResource;

    private final List<Composition.SectionComponent> sections = new ArrayList<>();

    public IPSSectionRequired(ConditionResource conditionResource,
                              MedicationStatementResource medicationResource,
                              ImmunizationResource immunizationResource,
                              AllergyIntoleranceResource allergyIntoleranceResource){
        this.conditionResource = conditionResource;
        this.medicationStatementResource = medicationResource;
        this.immunizationResource = immunizationResource;
        this.allergyIntoleranceResource = allergyIntoleranceResource;
    }

    public List<Bundle.BundleEntryComponent> fetchEntries(String patientId,
                                                          Map<ResourceType, Reference> references){
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

        sections.clear();

        //==================Condition resource=================
        List<Bundle.BundleEntryComponent> conditionEntries = conditionResource
                .fetchEntries(patientId, references);
        sections.add(CompositionResource.newSection(
                CodingSystem.LOINC, CodingCode.Condition.ENTRY, "Problemas", conditionEntries));
        entries.addAll(conditionEntries);


        //=================Medication Resource=================
        List<Bundle.BundleEntryComponent> medicationEntries = medicationStatementResource
                .fetchEntries(patientId, references);
        entries.addAll(medicationEntries);

        List<Bundle.BundleEntryComponent> medicationStatementEntries = medicationEntries
                .stream()
                .filter(entry-> entry.getResource().getResourceType().equals(ResourceType.MedicationStatement))
                .collect(Collectors.toList());
        sections.add(CompositionResource.newSection(
                CodingSystem.LOINC, CodingCode.Medication.ENTRY, "Medicamentos", medicationStatementEntries));


        //==================Allergy Resource===================
        List<Bundle.BundleEntryComponent> allergyEntries = allergyIntoleranceResource
                .fetchEntries(patientId, references);
        sections.add(CompositionResource.newSection(
                CodingSystem.LOINC, CodingCode.Allergy.ENTRY, "Alergias e intolerancias", allergyEntries));

        entries.addAll(allergyEntries);


        //================Immunization Resource================
        List<Bundle.BundleEntryComponent> immunizationEntries = immunizationResource
                .fetchEntries(patientId, references);
        sections.add(CompositionResource.newSection(
                CodingSystem.LOINC, CodingCode.Immunization.ENTRY, "Vacunas", immunizationEntries));
        entries.addAll(immunizationEntries);

        return entries;
    }

    public List<Composition.SectionComponent> getSections(){
        return sections;
    }
}
