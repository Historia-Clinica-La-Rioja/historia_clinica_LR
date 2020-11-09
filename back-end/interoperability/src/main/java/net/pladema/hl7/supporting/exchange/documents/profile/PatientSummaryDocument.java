//
//A Patient Summary gives a healthcare provider the essential information needed for health care
//coordination and, in cases of an unexpected need or when the patient consults a provider other
//than his regular contact person (e.g. the general practitioner he/she is registered with),
// this document enables continuity of care.
//

package net.pladema.hl7.supporting.exchange.documents.profile;

import net.pladema.hl7.dataexchange.IMultipleResourceFhir;
import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.concept.administration.DeviceResource;
import net.pladema.hl7.concept.administration.OrganizationResource;
import net.pladema.hl7.concept.administration.PatientResource;
import net.pladema.hl7.dataexchange.clinical.AllergyIntoleranceResource;
import net.pladema.hl7.dataexchange.clinical.ConditionResource;
import net.pladema.hl7.dataexchange.medications.ImmunizationResource;
import net.pladema.hl7.dataexchange.medications.MedicationStatementResource;
import net.pladema.hl7.dataexchange.model.domain.CompositionVo;
import net.pladema.hl7.supporting.exchange.documents.CompositionResource;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PatientSummaryDocument extends IDocumentProfile {

    public static final Coding TYPE = new Coding()
                    .setSystem(CodingSystem.LOINC)
                    .setCode("60591-5")
                    .setDisplay("Patient summary Document");

    public PatientSummaryDocument(PatientResource patientResource, DeviceResource deviceResource,
                                  OrganizationResource organizationResource, ConditionResource conditionResource,
                                  MedicationStatementResource medicationResource, ImmunizationResource immunizationResource,
                                  AllergyIntoleranceResource allergyIntoleranceResource){
        super(new ArrayList<>(){{
                add(patientResource);
                add(deviceResource);
                add(organizationResource);
            }},
            new ArrayList<>(){{
                add(conditionResource);
                add(medicationResource);
                add(immunizationResource);
                add(allergyIntoleranceResource);
            }}
        );
    }

    @Override
    public List<Bundle.BundleEntryComponent> getContent(String patientId) {
        //here are the rules for the specific composition document

        Map<ResourceType, ? extends ISingleResourceFhir> singleResources =
                getIncludedSingleResources();
        Map<ResourceType, ? extends IMultipleResourceFhir> multipleResources =
                getIncludedMultipleResources();

        //====================Fetch entries====================
        Bundle.BundleEntryComponent entryPatient = singleResources.get(ResourceType.Patient)
                .fetchEntry(patientId, new Reference[]{});
        Bundle.BundleEntryComponent entryOrganization = singleResources.get(ResourceType.Organization)
                .fetchEntry(patientId, new Reference[]{});

        Reference locationRef = new Reference();
        locationRef.setId(entryOrganization.getResource().getId());

        Bundle.BundleEntryComponent entryDevice = singleResources.get(ResourceType.Device)
                .fetchEntry(entryOrganization.getResource().getId(), new Reference[]{});

        Reference patientRef = new Reference(entryPatient.getFullUrl());
        Reference organizationRef = new Reference(entryOrganization.getFullUrl());
        Reference deviceRef = new Reference(entryDevice.getFullUrl());

        List<Bundle.BundleEntryComponent> conditionEntries = multipleResources.get(ResourceType.Condition)
                .fetchEntries(patientId, new Reference[]{patientRef});
        List<Bundle.BundleEntryComponent> immunizationEntries = multipleResources.get(ResourceType.Immunization)
                .fetchEntries(patientId, new Reference[]{patientRef, locationRef});
        List<Bundle.BundleEntryComponent> medicationEntries = multipleResources.get(ResourceType.MedicationStatement)
                .fetchEntries(patientId, new Reference[]{patientRef});
        List<Bundle.BundleEntryComponent> allergyEntries = multipleResources.get(ResourceType.AllergyIntolerance)
                .fetchEntries(patientId, new Reference[]{patientRef});

        List<Bundle.BundleEntryComponent> medicationStatementEntries = medicationEntries
                .stream()
                .filter((entry)-> entry.getResource().getResourceType().equals(ResourceType.MedicationStatement))
                .collect(Collectors.toList());

        //=======================Entries=======================
        CompositionVo data = new CompositionVo(TYPE);
        Bundle.BundleEntryComponent compositionEntry = CompositionResource.fetchEntry(
                CompositionResource
                .metadatos(data, new Reference[]{patientRef, deviceRef, organizationRef})
                .setSection(sections(conditionEntries, medicationStatementEntries, allergyEntries, immunizationEntries))
        );

        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        entries.add(compositionEntry);
        entries.add(entryPatient);
        entries.addAll(conditionEntries);
        entries.addAll(immunizationEntries);
        entries.addAll(medicationEntries);
        entries.addAll(allergyEntries);
        entries.add(entryOrganization);
        entries.add(entryDevice);
        return entries;
    }

    @SafeVarargs
    @Override
    public final List<Composition.SectionComponent> sections(List<Bundle.BundleEntryComponent>... entries) {

        return new ArrayList<>(){{
            add(CompositionResource.newSection(
                    CodingSystem.LOINC,
                    new FhirCode("11450-4", "Problem list"),
                    "Problemas",
                    entries[0]));
            add(CompositionResource.newSection(
                    CodingSystem.LOINC,
                    new FhirCode("10160-0", "Medication use"),
                    "Medicamentos",
                    entries[1]));
            add(CompositionResource.newSection(
                    CodingSystem.LOINC,
                    new FhirCode("48765-2", "Allergies and/or adverse reactions"),
                    "Alergias e intolerancias",
                    entries[2]));
            add(CompositionResource.newSection(
                    CodingSystem.LOINC,
                    new FhirCode("60484-3", "Immunization record"),
                    "Vacunas",
                    entries[3]));
        }};
    }
}
