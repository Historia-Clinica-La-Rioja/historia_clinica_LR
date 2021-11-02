//
//A Patient Summary gives a healthcare provider the essential information needed for health care
//coordination and, in cases of an unexpected need or when the patient consults a provider other
//than his regular contact person (e.g. the general practitioner he/she is registered with),
// this document enables continuity of care.
//

package net.pladema.hl7.supporting.exchange.documents.ips;

import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.concept.administration.PatientResource;
import net.pladema.hl7.dataexchange.model.domain.CompositionVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import net.pladema.hl7.supporting.exchange.documents.CompositionResource;
import net.pladema.hl7.supporting.exchange.documents.profile.IDocumentProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingCode;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Conditional(InteroperabilityCondition.class)
public class PatientSummaryDocument implements IDocumentProfile {

    public static final Coding TYPE = new Coding()
                    .setSystem(CodingSystem.LOINC)
                    .setCode(CodingCode.Document.PATIENT_SUMMARY_DOC)
                    .setDisplay("Patient summary Document");

    private final PatientResource patientResource;
    private final IPSSectionRequired sectionRequired;
    private final IPSDemographicData demographicData;

    public PatientSummaryDocument(PatientResource patientResource,
                                  IPSDemographicData demographicData,
                                  IPSSectionRequired sectionRequired){
        super();
        this.patientResource = patientResource;
        this.demographicData = demographicData;
        this.sectionRequired = sectionRequired;
    }

    @Override
    public List<Bundle.BundleEntryComponent> getContent(String patientId) {
        //here are the rules for the specific composition document

        Map<ResourceType, Reference> references = new EnumMap<>(ResourceType.class);

        //====================Fetch entries====================
        Bundle.BundleEntryComponent entryPatient = patientResource
                .fetchEntry(patientId, new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Patient, new Reference(entryPatient.getFullUrl()));

        List<Bundle.BundleEntryComponent> demographicEntries = demographicData.fetchEntries(patientId);
        references.putAll(demographicData.getReferences());

        List<Bundle.BundleEntryComponent> requiredEntries = sectionRequired.fetchEntries(patientId, references);

        //=======================Entries=======================
        CompositionVo data = new CompositionVo(TYPE);
        Bundle.BundleEntryComponent compositionEntry = IResourceFhir.fetchEntry(
                CompositionResource
                .metadatos(data, references)
                .setSection(sectionRequired.getSections())
        );

        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        entries.add(compositionEntry);
        entries.add(entryPatient);
        entries.addAll(requiredEntries);

        //Location resource is only required when there are administered vaccines.
        boolean requiredLocation = requiredEntries.stream()
                .filter(a -> a.getResource().getResourceType().equals(ResourceType.Immunization))
                .map(r -> (Immunization) r.getResource())
                .allMatch(Immunization::hasLocation);

        if(!requiredLocation)
            demographicEntries.removeIf(r -> r.getResource().getResourceType().equals(ResourceType.Location));

        entries.addAll(demographicEntries);
        return entries;
    }
}
