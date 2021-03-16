package net.pladema.hl7.dataexchange;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public abstract class ISingleResourceFhir extends IResourceFhir {

    protected ISingleResourceFhir(FhirPersistentStore store){
        super(store);
    }

    public abstract IBaseResource fetch(String id, Map<ResourceType, Reference> references);

    public Bundle.BundleEntryComponent fetchEntry(String id, Map<ResourceType, Reference> references) {
        return fetchEntry(fetch(id, references));
    }
}
