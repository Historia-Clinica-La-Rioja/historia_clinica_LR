package net.pladema.hl7.dataexchange;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class IMultipleResourceFhir extends IResourceFhir {

    public abstract List<? extends IBaseResource> fetch(String id, Reference[] references);

    public List<Bundle.BundleEntryComponent> fetchEntries(String id, Reference[] references) {
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        fetch(id, references).forEach((resource)->
                entries.add(fetchEntry(resource))
        );
        return entries;
    }
}
