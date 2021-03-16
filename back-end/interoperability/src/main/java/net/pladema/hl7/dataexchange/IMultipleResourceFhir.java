package net.pladema.hl7.dataexchange;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public abstract class IMultipleResourceFhir<R extends IBaseResource> extends IResourceFhir {

    protected IMultipleResourceFhir(FhirPersistentStore store){
        super(store);
    }

    public abstract List<R> fetch(String id, Map<ResourceType, Reference> references);

    public List<Bundle.BundleEntryComponent> fetchEntries(String id, Map<ResourceType, Reference> references) {
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        fetch(id, references).forEach(resource->
                entries.add(fetchEntry(resource))
        );
        return entries;
    }
}
