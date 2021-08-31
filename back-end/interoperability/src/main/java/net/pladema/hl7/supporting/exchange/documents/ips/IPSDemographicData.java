package net.pladema.hl7.supporting.exchange.documents.ips;

import net.pladema.hl7.concept.administration.DeviceResource;
import net.pladema.hl7.concept.administration.LocationResource;
import net.pladema.hl7.concept.administration.OrganizationResource;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;
import org.hl7.fhir.r4.model.Bundle;
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
public class IPSDemographicData {

    private final Map<ResourceType, Reference> references = new EnumMap<>(ResourceType.class);

    private final DeviceResource deviceResource;
    private final OrganizationResource organizationResource;
    private final LocationResource locationResource;

    public IPSDemographicData(DeviceResource deviceResource,
                              OrganizationResource organizationResource,
                              LocationResource locationResource){
        this.deviceResource = deviceResource;
        this.organizationResource = organizationResource;
        this.locationResource = locationResource;
    }

    public List<Bundle.BundleEntryComponent> fetchEntries(String patientId){
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();

        //================Organization resource================
        Bundle.BundleEntryComponent entryOrganization = organizationResource
                .fetchEntry(patientId, new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Organization, new Reference(entryOrganization.getFullUrl()));

        Bundle.BundleEntryComponent entryLocation = locationResource
                .fetchEntry(patientId, new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Location, new Reference(entryLocation.getFullUrl()));

        entries.add(entryOrganization);
        entries.add(entryLocation);

        //===================Device resource===================
        Bundle.BundleEntryComponent entryDevice = deviceResource
                .fetchEntry(entryOrganization.getResource().getId(), new EnumMap<>(ResourceType.class));
        references.put(ResourceType.Device, new Reference(entryDevice.getFullUrl()));
        entries.add(entryDevice);

        return entries;
    }

    public Map<ResourceType, Reference> getReferences(){
        return references;
    }
}
