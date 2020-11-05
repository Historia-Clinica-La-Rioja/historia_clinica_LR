package net.pladema.hl7.dataexchange;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

@Component
public abstract class ISingleResourceFhir extends IResourceFhir {

    public abstract IBaseResource fetch(String id, Reference[] references);

    public Bundle.BundleEntryComponent fetchEntry(String id, Reference[] references) {
        return fetchEntry(fetch(id, references));
    }
}
