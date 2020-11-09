package net.pladema.hl7.dataexchange;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class IMultipleResourceFhir extends IResourceFhir {

    public IMultipleResourceFhir(FhirPersistentStore store){
        super(store);
    }

    public abstract List<? extends IBaseResource> fetch(String id, Reference[] references);

    public List<Bundle.BundleEntryComponent> fetchEntries(String id, Reference[] references) {
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        fetch(id, references).forEach((resource)->
                entries.add(fetchEntry(resource))
        );
        return entries;
    }
}
